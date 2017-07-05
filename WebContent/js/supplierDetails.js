/**
 * This file calculates and shows the details about power suppliers 
 */
$(document).ready(function() {
	$("#closeShowSupply").click(function(event){
		closeDiv("supplierDetailsBody");
	});	
	$("#closeProducerInbox").click(function(event){
		closeDiv("producerInboxId");
	});
	$("#takeActionProducerInbox").click(function(event){
		takeActionProducerInbox();
	});
	$("#closeDistributeEnergyAmongHolonObjectsDiv").click(function(event){
		closeDiv("distributeEnergyAmongHolonObjectsDiv");
	});
	$("#distributeEnergyViaCoordinator").click(function(event){
		distributeEnergyViaCoordinator();
	});
	$("#closeHistoryDistributeEnergyAmongHolonObjectsDiv").click(function(event){
		closeDiv("historyDistributeEnergyAmongHolonObjectsDiv");
	});
	
	$(document).bind("click",".linkHolonObject",function(event) {
		var id=event.target.id;
		var holonObject = globalHoList.get(id);
		if(holonObject !=undefined && holonObject.getBounds()!= undefined && event.toElement.className=="linkHolonObject"){
			var holonObjectLocation=holonObject.getBounds().getNorthEast().lat()+"~"+holonObject.getBounds().getNorthEast().lng();
		    zoomToHolon(id,holonObjectLocation, "Holon Object")
		}
	});
	$(document).bind("click",".linkPowerSource",function(event) {
		var id=event.target.id;
		var powerSource = globalPSrcList.get(id);
		if(powerSource !=undefined && powerSource.getBounds()!= undefined && event.toElement.className=="linkPowerSource"){
			var powerSourceLocation=powerSource.getBounds().getNorthEast().lat()+"~"+powerSource.getBounds().getNorthEast().lng();
		    zoomToHolon(id,powerSourceLocation, "Power Source")
		}
	});
})

/**
 * This method gets the data required for supplier window that is accessible from holon object info window on map. 
 * @param holonObjectId holon object id
 */
function showSupplierDetails(holonObjectId) {
	var dataAttributes= {
			holonObjectId : holonObjectId
	}
	ajaxRequest("getDataForSupplierDetails", dataAttributes, getDataForSupplierDetailsCallBack, dataAttributes);
}

/**
 * callback method for ajax request in showSupplierDetails(holonObjectId) method
 * @param data data from server side
 * @param options data from client side
 */
function getDataForSupplierDetailsCallBack(data,options) {
	var contentString = "<tr><td colspan='8' style='text-decoration: underline;'>Power Supply Details for Holon Object Id: "+options.holonObjectId+"</td></tr>"+
	"<tr>"+
		"<td>Request#ID</td>"+
		"<td>Type of Supplier</td>"+
		"<td>Id of Supplier</td>"+
		"<td>Power Requested</td>"+
		"<td>Power Granted</td>"+
		"<td>Message Status</td>"+
		"<td>Connected</td>"+
		"<td>Communication Mode</td>"
	"<tr>";
	var supplierRow = data.split("!");
	var supplierColumn = "";
	var requestId = "N/A";
	var typeSupplier = "N/A";
	var idSupplier = "N/A";
	var powerRequested = "N/A";
	var powerGranted = "N/A";
	var messageStatus = "N/A";
	var isConnected = "N/A";
	var communicationMode = "N/A";
	for(var i = 0; i<supplierRow.length;i++) {
		if(supplierRow != "") {
			supplierColumn = supplierRow[i].split("~");
		}
		if(supplierColumn != "") {
			typeSupplier = supplierColumn[0];
			idSupplier = supplierColumn[1];
			powerRequested = supplierColumn[2];
			powerGranted = supplierColumn[3];
			messageStatus = supplierColumn[4]
			isConnected = supplierColumn[5];
			requestId = supplierColumn[6];
			communicationMode = supplierColumn[7];
		}
		contentString = contentString.concat("<tr><td>"+requestId+"</td><td>"+typeSupplier+"</td>");
		if(typeSupplier == "Holon Object") {
			contentString = contentString.concat("<td><a href='#' class='linkHolonObject' id='"+idSupplier+"'>"+idSupplier+"</a></td>");
		} else if(typeSupplier == "Power Source") {
			contentString = contentString.concat("<td><a href='#' class='linkPowerSource' id='"+idSupplier+"'>"+idSupplier+"</a></td>");
		} else {
			contentString = contentString.concat("<td>"+idSupplier+"</td>");
		}
		
		contentString = contentString.concat("<td>"+
				powerRequested+"</td><td>"+powerGranted+"</td>");
		if(messageStatus == "ACCEPTED") {
			contentString = contentString.concat("<td style='color:green'>"+messageStatus+"</td>")
		} else if(messageStatus == "REJECTED" || messageStatus == "CONNECTION RESET") {
			contentString = contentString.concat("<td style='color:red'>"+messageStatus+"</td>")
		} else {
			contentString = contentString.concat("<td>"+messageStatus+"</td>")
		}
		contentString = contentString.concat("<td>"+isConnected+"</td><td>"+communicationMode+"</td></tr>");
	}
	if(contentString == "") {
		contentString = "<tr><td colspan='8' style='text-align: center;'>No Power is being supplied.</td></tr>";
	}
	$("#suppLierDetailList").html(contentString);
	closeDiv('consumptionGraphBody');
	openDiv("supplierDetailsBody");
}

/**
 * This method is used to send energy messages to all connected holon objects which are producers.
 *  Energy messages are sent to immediate peers which then send to their peers and so on.
 * @param holonObjectId holon object id
 */
function sendMessageToAllProducers(holonObjectId) {
	var dataAttributes= {
			holonObjectId : holonObjectId
	}
	ajaxRequest("sendMessageToAllProducers", dataAttributes, sendMessageToAllProducersCallBack, {});
}

/**
 * callback method for ajax request in sendMessageToAllProducers(holonObjectId) method
 * @param data data from server side
 * @param options data from client side
 */
function sendMessageToAllProducersCallBack(data, options) {
	if(data == "SUCCESS") {
		swal("Message sent", "Message has been sent to all connected producers", "info");
	} else if (data == "FAILURE") {
		swal("Cannot communicate!", "Either 'Can Communicate' field is 'No' or Holon Object is not connected to any power line..", "info");
	}
}

function distributeEnergyAmongHolonObjectsFlexibilityZero() {
	swal("Flexibility insufficient or No energy required.", "Either flexibility of Holon is zero or there is no need for energy.", "info");
}

/**
 * This method is used to prepare response for inbox of the holon object.   
 * @param holonObjectId holon object id
 */
function checkInbox(holonObjectId) {
	var dataAttributes= {
			holonObjectId : holonObjectId
	}
	ajaxRequest("checkInbox", dataAttributes, checkInboxCallBack, dataAttributes);
}

/**
 * This method is used to prepare response for inbox of the holon object. 
 */
function takeActionProducerInbox() {
	var holonObjectId = $("#hiddenSupplierProducerId").val();
	var takeAction = "yes"; 
	var dataAttributes= {
			holonObjectId : holonObjectId,
			takeAction : takeAction
	}
	ajaxRequest("checkInbox", dataAttributes, checkInboxCallBack, dataAttributes);

}

/**
 * callback method for the ajax calls in  checkInbox(holonObjectId) and takeActionProducerInbox() methods 
 * @param data
 * @param options
 */
function checkInboxCallBack(data, options) {
	var supplierProducerHolonObjectId = options["holonObjectId"];
	updateCoordinator(supplierProducerHolonObjectId);
	$("#hiddenSupplierProducerId").val(supplierProducerHolonObjectId);
	var inboxRow = "";
	if(data != "") {
		inboxRow = data.split("!");
	}
	var contentString = "<tr><td colspan='8' style='text-decoration: underline;'>Messages Received</td></tr>"+
		"<tr>"+
		"<td>Request#ID</td>"+
		"<td>Consumer ID</td>"+
		"<td>Requestor Type (Priority)</td>"+
		"<td>Power Requested</td>"+
		"<td>Power Granted</td>"+
		"<td>Message Status</td>"+
		"<td>Connected</td>"+
		"<td>Communication Mode</td>"+
		"<tr>";
	var inboxColumn = "";
	var consumerId = "N/A";
	var requestorTypePriority = "N/A";
	var powerRequested = "N/A";
	var powerGranted = "N/A";
	var messageStatus = "N/A";
	var isConnected = "N/A";
	var requestId = "N/A";
	var canCommunicate = "SUCCESS";
	var communicationMode = "N/A";
	
	for(var i = 0; i<inboxRow.length;i++) {
		if(inboxRow != "") {
			inboxColumn = inboxRow[i].split("~");
		}
		if(inboxColumn != "") {
			consumerId = inboxColumn[0];
			requestorTypePriority = inboxColumn[1];
			powerRequested = inboxColumn[2];
			powerGranted = inboxColumn[3];
			messageStatus = inboxColumn[4];
			isConnected = inboxColumn[5];
			requestId = inboxColumn[6];
			canCommunicate = inboxColumn[7];
			communicationMode = inboxColumn[8];
		}
		contentString = contentString.concat("<tr><td>"+requestId+"</td><td><a href='#' class='linkHolonObject' id='"+consumerId+"'>"+consumerId+"</a></td><td>"+requestorTypePriority+"</td><td>"+
				powerRequested+"</td><td>"+powerGranted+"</td>");
		if(messageStatus == "ACCEPTED") {
			contentString = contentString.concat("<td style='color:green'>"+messageStatus+"</td>");
		} else if(messageStatus == "REJECTED" || messageStatus == "CONNECTION RESET") {
			contentString = contentString.concat("<td style='color:red'>"+messageStatus+"</td>");
		} else {
			contentString = contentString.concat("<td>"+messageStatus+"</td>");
		}
		
		contentString = contentString.concat("<td>"+isConnected+"</td><td>"+communicationMode+"</td></tr>");
	}
	if(inboxColumn == "") {
		contentString = contentString.concat("<tr><td>"+requestId+"</td><td>"+consumerId+"</td><td>"+requestorTypePriority+"</td><td>"+
				powerRequested+"</td><td>"+powerGranted+"</td><td>"+messageStatus+"</td><td>"+isConnected+"</td><td>"+communicationMode+"</td></tr>");
	}
	$("#producerInboxList").html(contentString);
	openDiv("producerInboxId");
	if(canCommunicate == "FAILURE") {
		swal("Cannot communicate!", "Necessary action can not be taken. Set the 'Can Communicate' field to 'Yes' to continue" +
				" taking actions on pending requests.", "info");
	}
}

/**
 * This method is used by holon coordinator to distribute energy among all holon objects of the same holon. 
 * @param holonObjectId holon object id
 */
function distributeEnergyAmongHolonObjects(holonObjectId) {
	var dataAttributes= {
			holonObjectId : holonObjectId
	}
	ajaxRequest("distributeEnergyAmongHolonObjects", dataAttributes, distributeEnergyAmongHolonObjectsCallBack, dataAttributes);
	
}

/**
 * This method is used by holon coordinator to distribute energy among all holon objects of the same holon.
 */
function distributeEnergyViaCoordinator() {
	var holonObjectId = $("#hiddenSupplierCoordinatorId").val();
	var takeAction = "yes"; 
	var dataAttributes= {
			holonObjectId : holonObjectId,
			takeAction : takeAction
	}
	ajaxRequest("distributeEnergyAmongHolonObjects", dataAttributes, distributeEnergyViaCoordinatorCallBack, dataAttributes);

}

/**
 * callback for ajax calls in distributeEnergyViaCoordinator() method
 * @param data data from server side
 * @param options data from client side 
 */
function distributeEnergyViaCoordinatorCallBack(data, options) {
	closeDiv("distributeEnergyAmongHolonObjectsDiv");
	updateCoordinator(options["holonObjectId"]);	
	swal("Energy distributed successfully!", "Energy has been distributed to the required holon objects. Please check their supplier details for more information.", "info");
}
/**
 * callback for ajax calls in  distributeEnergyAmongHolonObjects(holonObjectId) method
 * @param data data from server side
 * @param options data from client side 
 */
function distributeEnergyAmongHolonObjectsCallBack(data, options) {
	var holonCoordinatorId = options["holonObjectId"];
	$("#hiddenSupplierCoordinatorId").val(holonCoordinatorId);
	var inboxRow = "";
	if(data != "") {
		inboxRow = data.split("!");
	}
	var contentString = "<tr><td colspan='9' style='text-decoration: underline;'>Distribution of energy by Holon Coordinator</td></tr>"+
		"<tr>"+
		"<td>Consumer ID</td>"+
		"<td>Consumer Type (Priority)</td>"+
		"<td>Producer ID</td>"+
		"<td>Producer Type</td>"+
		"<td>Power Requested</td>"+
		"<td>Power Granted</td>"+
		"<td>Message Status</td>"+
		"<td>Connected</td>"+
		"<td>Comunication Mode</td>"+
		"<tr>";
	var inboxColumn = "";
	var consumerId = "N/A";
	var consumerType = "N/A";
	var producerId = "N/A";
	var producerType = "N/A";
	var powerRequested = "N/A";
	var powerGranted = "N/A";
	var messageStatus = "N/A";
	var isConnected = "N/A";
	var communicationMode = "N/A";
	
	for(var i = 0; i<inboxRow.length;i++) {
		if(inboxRow != "") {
			inboxColumn = inboxRow[i].split("~");
		}
		if(inboxColumn != "") {
			consumerId = inboxColumn[0];
			consumerType = inboxColumn[1];
			producerId = inboxColumn[2];
			producerType = inboxColumn[3];
			powerRequested = inboxColumn[4];
			powerGranted = inboxColumn[5];
			messageStatus = inboxColumn[6];
			isConnected = inboxColumn[7];
			communicationMode = inboxColumn[8];
		}
		contentString = contentString.concat("<tr><td><a href='#' class='linkHolonObject' id='"+consumerId+"'>"+consumerId+"</a></td><td>"+consumerType+"</td><td>"+producerId+"</td><td>"+
				producerType+"</td><td>"+powerRequested+"</td><td>"+powerGranted+"</td>");
		if(messageStatus == "ACCEPTED") {
			contentString = contentString.concat("<td style='color:green'>"+messageStatus+"</td>");
		} else if(messageStatus == "REJECTED" || messageStatus == "CONNECTION RESET") {
			contentString = contentString.concat("<td style='color:red'>"+messageStatus+"</td>");
		} else {
			contentString = contentString.concat("<td>"+messageStatus+"</td>");
		}
		
		contentString = contentString.concat("<td>"+isConnected+"</td><td>"+communicationMode+"</td></tr>");
	}
	if(inboxColumn == "") {
		contentString = contentString.concat("<tr><td>"+consumerId+"</td><td>"+consumerType+"</td><td>"+
				producerId+"</td><td>"+producerType+"</td><td>"+powerRequested+"</td><td>"+powerGranted+"</td><td>"+
				messageStatus+"</td><td>"+isConnected+"</td><td>"+communicationMode+"</td></tr>");
	}
	$("#distributeEnergyAmongHolonObjectsList").html(contentString);
	openDiv("distributeEnergyAmongHolonObjectsDiv");
}

/**
 *  This method prepares response for history module which is accessible from info window of holon coordinator.
 * @param holonObjectId holon object id
 */
function historyDistributeEnergyAmongHolonObjects(holonObjectId) {
	var dataAttributes= {
			holonObjectId : holonObjectId
	}
	ajaxRequest("historyDistributeEnergyAmongHolonObjects", dataAttributes, historyDistributeEnergyAmongHolonObjectsCallBack, dataAttributes);
}

/**
 * callback method for ajax call in historyDistributeEnergyAmongHolonObjects(holonObjectId) method
 * @param data data from server
 * @param options data from client 
 */
function historyDistributeEnergyAmongHolonObjectsCallBack(data, options) {
	var inboxRow = "";
	if(data != "") {
		inboxRow = data.split("!");
	}
	var contentString = "<tr><td colspan='9' style='text-decoration: underline;'>History of Distribution of energy by Holon Coordinator</td></tr>"+
		"<tr>"+
		"<td>Consumer ID</td>"+
		"<td>Consumer Type (Priority)</td>"+
		"<td>Producer ID</td>"+
		"<td>Producer Type</td>"+
		"<td>Power Requested</td>"+
		"<td>Power Granted</td>"+
		"<td>Message Status</td>"+
		"<td>Connected</td>"+
		"<td>Comunication Mode</td>"+
		"<tr>";
	var inboxColumn = "";
	var consumerId = "N/A";
	var consumerType = "N/A";
	var producerId = "N/A";
	var producerType = "N/A";
	var powerRequested = "N/A";
	var powerGranted = "N/A";
	var messageStatus = "N/A";
	var isConnected = "N/A";
	var communicationMode = "N/A";
	var consumerLocation;
	var consumerZoomType = "Holon Object";
	for(var i = 0; i<inboxRow.length;i++) {
		if(inboxRow != "") {
			inboxColumn = inboxRow[i].split("#");
		}
		if(inboxColumn != "") {
			consumerId = inboxColumn[0];
			consumerLocation = inboxColumn[1];
			consumerType = inboxColumn[2];
			producerId = inboxColumn[3];
			producerType = inboxColumn[4];
			powerRequested = inboxColumn[5];
			powerGranted = inboxColumn[6];
			messageStatus = inboxColumn[7];
			isConnected = inboxColumn[8];
			communicationMode = inboxColumn[9];
		}
		contentString = contentString.concat("<tr><td><a href='#' class='linkHolonObject' id='"+consumerId+"'>"+consumerId+"</a></td><td>"+consumerType+"</td>");
		if(producerType == "Holon Object") {
			contentString = contentString.concat("<td><a href='#' class='linkHolonObject' id='"+producerId+"'>"+producerId+"</a></td>");
		} else {
			contentString = contentString.concat("<td><a href='#' class='linkPowerSource' id='"+producerId+"'>"+producerId+"</a></td>");
		}
				
		contentString = contentString.concat("<td>"+
				producerType+"</td><td>"+powerRequested+"</td><td>"+powerGranted+"</td>");
		if(messageStatus == "ACCEPTED") {
			contentString = contentString.concat("<td style='color:green'>"+messageStatus+"</td>");
		} else if(messageStatus == "REJECTED" || messageStatus == "CONNECTION RESET") {
			contentString = contentString.concat("<td style='color:red'>"+messageStatus+"</td>");
		} else {
			contentString = contentString.concat("<td>"+messageStatus+"</td>");
		}
		contentString = contentString.concat("<td>"+isConnected+"</td><td>"+communicationMode+"</td></tr>");
	}
	if(inboxColumn == "") {
		contentString = contentString.concat("<tr><td>"+consumerId+"</td><td>"+consumerType+"</td><td>"+
				producerId+"</td><td>"+producerType+"</td><td>"+powerRequested+"</td><td>"+powerGranted+"</td><td>"+
				messageStatus+"</td><td>"+isConnected+"</td><td>"+communicationMode+"</td></tr>");
	}
	$("#historyDistributeEnergyAmongHolonObjectsList").html(contentString);
	openDiv("historyDistributeEnergyAmongHolonObjectsDiv");
}