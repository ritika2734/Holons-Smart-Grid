var intervalFlag = 0;
var myDynamicTimer;
var timeInMilliSeconds = 10000;

/**
 * This function is called when we don't want to start dynamic holon module 
 */
function doNotStartDynamicHolon() {
	swal("Cannot Start Dynamic Holon!", "Holon object is not part of any holon. Connect this holon object to a main line and then try again.", "info");
}

/**
 * This function is called when we want to start dynamic holon module
 * @param currentEnergyRequired the current energy of the concerned holon object
 * @param holonObjectId the holon object ID
 */
function startDynamicHolon(currentEnergyRequired, holonObjectId) {
	$("#dynamicHolonLegend").html("This Holon Object will send a request after every "+(timeInMilliSeconds/1000)+" seconds");
	if(currentEnergyRequired > 0 && intervalFlag == 0) {
		checkTimerDynamicHolon(currentEnergyRequired, holonObjectId);
		openDiv("dynamicHolonDiv");
	} else if(currentEnergyRequired > 0 && intervalFlag == 1) {
		openDiv("dynamicHolonDiv");
		myDynamicTimer = setInterval(function () {checkTimerDynamicHolon(currentEnergyRequired,holonObjectId)}, timeInMilliSeconds);
		} else if(currentEnergyRequired > 0 && intervalFlag < 6){
			openDiv("dynamicHolonDiv");
		} else {
			intervalFlag = 0;//Re-initializing interval timer for new requests.
			swal("Cannot Start Dynamic Holon!", "Current energy required must be greater than zero for this module to work.", "info");
			closeDiv("dynamicHolonDiv");
			$("#dynamicHolonDivTable").html("<tr style='display: none'><td colspan='2'></td></tr>");
			}
}

/**
 * This method is used by start dynamic holon module and it checks for the current energy requirement of the holon object. 
 * @param currentEnergyRequired current required energy
 * @param holonObjectId id of holon object 
 */
function checkTimerDynamicHolon(currentEnergyRequired,holonObjectId) {
	var dataAttributes= {
			holonObjectId : holonObjectId,
			currentEnergyRequired : currentEnergyRequired
	}
	ajaxRequest("checkDynamicCurrentEnergyRequired", dataAttributes, checkDynamicCurrentEnergyRequiredCallBack, dataAttributes);
}

/**
 * Callback method for ajax request in checkTimerDynamicHolon(currentEnergyRequired,holonObjectId) method
 * @param data data from server side
 * @param options data from client side
 */
function checkDynamicCurrentEnergyRequiredCallBack(data, options) {
	intervalFlag++;
	var holonObjectId = options["holonObjectId"];
	var currentEnergyRequired = options["currentEnergyRequired"];
	var dynamicCurrentEnergyRequired = data.split("~")[0];
	var originalEnergyRequiredAfterCurrentProduction = data.split("~")[1];
	var dataAttributes= {
			holonObjectId : holonObjectId,
			dynamicCurrentEnergyRequired : dynamicCurrentEnergyRequired,
			originalEnergyRequiredAfterCurrentProduction : originalEnergyRequiredAfterCurrentProduction
	}
	if(dynamicCurrentEnergyRequired > 0 && intervalFlag <= 5) {
		var tempOptions = {
				intervalFlag:intervalFlag,
				currentEnergyRequired : currentEnergyRequired,
				holonObjectId : holonObjectId
		}
		ajaxRequest("sendMessageToAllProducers", options, dynamicHolonSendMessageToAllProducersCallBack, tempOptions);
	} else if(dynamicCurrentEnergyRequired > 0 && intervalFlag == 6) {
		intervalFlag = 0;//Re-initializing interval timer for new requests.
		clearTimeout(myDynamicTimer);
		//Code to start dynamic holon merger
		swal({
			title: "Proceed with finding a new holon for this holon object?",
			text: "This holon object could not find a supplier after 5 attempts. Do you want to find a new holon for this holon object?",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: '#DD6B55',
			confirmButtonText: 'Yes, find a new holon!',
			cancelButtonText: "No, don't do anything!",
			closeOnConfirm: true,
			closeOnCancel: false
		},
		function(isConfirm) {
		    if (isConfirm) {
		    	ajaxRequest("startDynamicHolonMerger", dataAttributes, startDynamicHolonMergerCallBack, dataAttributes);	
			} else {
				swal("Cancelled", "Dynamic Holon merger has been cancelled.", "info");
		    }
		});
	} else {
		clearTimeout(myDynamicTimer);
		$("#dynamicHolonDivTable").append("<tr><td>Request#"+intervalFlag+"-</td><td><b>Energy supplied!</b> Either, required energy has been provided to the holon object or timer has expired.</td></tr>");
		swal("Energy supplied!","Either, required energy has been provided to the holon object or timer has expired.","info");
		intervalFlag = 0;//Re-initializing interval timer for new requests.
	}
}
/**
 * callback method for ajax request sendMessageToAllProducers in checkDynamicCurrentEnergyRequiredCallBack(data, options) method
 * @param data data from server side
 * @param options data from client side
 */
function dynamicHolonSendMessageToAllProducersCallBack(data, options) {
	if(data == "SUCCESS") {
		$("#dynamicHolonDivTable").append("<tr><td>Request#"+intervalFlag+"-</td><td><b>Message sent:</b> Message has been sent to all connected producers.</td></tr>");
	} else {
		swal("Cannot communicate!", "Either 'Can Communicate' field is 'No' or Holon Object is not connected to any power line.", "info");
		$("#dynamicHolonDivTable").append("<tr><td>Request#"+intervalFlag+"-</td><td><b>Cannot communicate!</b> Either 'Can Communicate' field is 'No' or Holon " +
				"Object is not connected to any power line.</td></tr>");
		intervalFlag = 0;//Re-initializing interval timer for new requests.
	}
	var tempIntervalFlag = options["intervalFlag"];
	var holonObjectId = options["holonObjectId"];
	var currentEnergyRequired = options["currentEnergyRequired"];
	if(tempIntervalFlag == 1) {
		startDynamicHolon(currentEnergyRequired, holonObjectId);
	}
}

/**
 * callback method for ajax request startDynamicHolonMerger in checkDynamicCurrentEnergyRequiredCallBack(data, options) method
 * @param data data from server side
 * @param options data from client side
 */
function startDynamicHolonMergerCallBack(data, options) {
	if(data == "false") {
		swal("Cannot find a new holon!", "Could not find another holon with sufficient energy requirements.", "info");
	} else {
		var startDynamicHolonMergerResponse = data.split("!");
		var oldCoordinatorId = startDynamicHolonMergerResponse[0];
		var newHolonColor = startDynamicHolonMergerResponse[1];
		var holonObjectId = startDynamicHolonMergerResponse[2];
		if(oldCoordinatorId > 0) {
			removeIconFromMap(oldCoordinatorId);
		}
		if(holonObjectId > 0) {
			var holonObject= globalHoList.get(holonObjectId);
			holonObject.setOptions({strokeColor:newHolonColor,fillColor:newHolonColor});
			swal("New holon assigned successfully!", "This holon object has been succesfully assigned to a new holon with sufficient energy.", "info");			
		}
	}
	intervalFlag = 0;//Re-initializing interval timer for new requests.
}

/**
 * This method is used to dissolve an entire holon into a suitable holon which fulfills the energy requirements of the holon that wants to dissolve.
 *  Only Holon Coordinator has access to this module.
 *  Holon will dissolve only if the flexibility of current holon is zero and current energy requirement is greater than zero.
 * @param currentEnergyRequirementHolon Current energy requirement of the holon 
 * @param holonCoordinatorId holon co ordinator id
 */
function dissolveHolon(currentEnergyRequirementHolon, holonCoordinatorId) {
	if(currentEnergyRequirementHolon > 0) {
		var dataAttributes= {
				holonCoordinatorId : holonCoordinatorId
		}
		ajaxRequest("dissolveHolon", dataAttributes, dissolveHolonCallBack, dataAttributes);
	} else {
		swal("Cannot dissolve!", "This option works only when flexibility of Holon is zero and current energy requirement is greater than zero.", "info");
	}
}

/**
 * callback method for ajax request in dissolveHolon(currentEnergyRequirementHolon, holonCoordinatorId) method
 * @param data data from server side
 * @param options data from client side
 */
function dissolveHolonCallBack(data, options) {
	if(data == "false") {
		swal("Server response:Cannot dissolve!", "Either flexibility is greater than zero or current energy requirement is zero.", "info");
	} else if(data == "noOtherHolonFound") {
		swal("Server response:Cannot dissolve!", "No other holon found with sufficient energy requirements.", "info");
	} else {
		var oldCoordinatorId = data.split("*")[0].split("!")[0];
		var newHolonColor = data.split("*")[0].split("!")[1];
		var holonObjectsList;
		removeIconFromMap(oldCoordinatorId);
		if(data.split("*")[1] != undefined) {
			holonObjectsList = data.split("*")[1].split("~");
		}
		if(holonObjectsList.length > 0) {
			for(var i=0;i< holonObjectsList.length-1; i++){
				var holonObjectId = holonObjectsList[i];
				var holonObject= globalHoList.get(holonObjectId);
				holonObject.setOptions({strokeColor:newHolonColor,fillColor:newHolonColor});
			}
		}
		swal("Holons dissolved successfully!", "Holons have been dissolved successfully. Check the coordinator details for more information", "info");
	}
}

/**
 * This method is called when we do not want to start the dissolve holon module 
 */
function dontDissolveHolon() {
	swal("Cannot dissolve!", "This option works only when flexibility of Holon is zero and current energy requirement is greater than zero", "info");
}

/**
 * This method is called when we want to abort subsequent requests in dynamic holon module. This function also closes the window in which messages were appearing.
 * @param divToClose the window which needs to be closed
 */
function abortDynamicHolonRequests(divToClose) {
	intervalFlag = 0;//Re-initializing interval timer for new requests.
	$("#dynamicHolonDivTable").html("<tr style='display: none'><td colspan='2'></td></tr>");
	clearTimeout(myDynamicTimer);
	closeDiv(divToClose);
}
