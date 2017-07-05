/**
 * Displays holon elements 
 * @param holonObjectId Id of holon object whose elements have to be populated
 */
function showHolonElements(holonObjectId) {
	var dataAttributes= {
			holonObjectId : holonObjectId
	}
	ajaxRequest("showHolonElements", dataAttributes, showHolonElementsCallBack, dataAttributes);
}

/**
 * callback method for ajax request from the method showHolonElements(holonObjectId)
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function showHolonElementsCallBack(data, options) {
	var holonObjectId = options['holonObjectId'];
	$("#addHolonElementTableHeader").click(function() {
		$("#hiddenHolonObjectId").val(holonObjectId);
		$("#holonElementActionState").val("Add");
		$("#elmTitle").text("Add Holon Element");
		getListHolonElementType();
		getListHolonElementState();
		openDiv('elementInfo');
	});
	$("#saveElementInfo").click(function(event){
		//This code will prevent event from firing more than once
		if(event.handled !== true) {
		  event.handled = true;
		  addHolonElement();
		}
	  });
	$("#holonElementsListBody").html(data);
	if(data.indexOf("noData") >= 0){
		var currecntPC=globalPCList.get(holonObjectId.toString());
		if(currecntPC != undefined){
			//All items have been removed.
			currecntPC.setVisible(false);
		}
	}
	openDiv("divHolonElementsDetail");
}

/**
 * To delete a holon element
 * @param holonElementId Id of elemnet to be deleted
 * @param holonObjectId  Id of the holon object which the element is member of 
 */
function deleteHolonElement(holonElementId, holonObjectId) {
	var dataAttributes= {
			holonElementId : holonElementId
	}
	var options = {
			holonObjectId : holonObjectId
	}
	swal({
		title: "Are you sure?",
		text: "Do you really want to remove the Holon Element?",
		type: "warning",
		showCancelButton: true,
		confirmButtonColor: '#DD6B55',
		confirmButtonText: 'Yes, remove!',
		cancelButtonText: "No, Don't remove!",
		closeOnConfirm: false,
		closeOnCancel: false
	},
	function(isConfirm){
	    if (isConfirm){
	    	ajaxRequest("deleteHolonElement", dataAttributes, deleteHolonElementCallBack, options);	
		} else {
	      swal("Cancelled", "Holon Element has not been deleted.", "info");
	    }
	});
}

/**
 * callback method for ajax request from the method deleteHolonElement(holonElementId, holonObjectId)
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function deleteHolonElementCallBack(data, options) {
	var resp =data.split("*");
	if(resp[0] == "true"){
		var holonObjectId = options['holonObjectId'];
		showHolonElements(holonObjectId);
		swal("Holon Element Removed!", "Holon Element has been removed", "info");
		showHolonCoIcons();
	} else {
		swal("Server Error!", "Error in deleting holon element, please check applciation logs.", "info");
	}
}

/**
 * Get list of holon element types
 * @param holonElementTypeId Id of current element type
 */
function getListHolonElementType(holonElementTypeId) {
	var options={};
	if(typeof holonElementTypeId != "undefined") {
		options={holonElementTypeId:holonElementTypeId};
	}
	ajaxRequest("getListHolonElementType", {}, getListHolonElementTypeCallBack, options);
}

/**
 * callback method for ajax request from the method getListHolonElementType(holonElementTypeId)
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function getListHolonElementTypeCallBack(data,options) {
	var holonElementTypeId =options['holonElementTypeId'];
	var listHolonElementTypeMaster= data.split("*");
	$("#holonElementType").empty();
	for(var i=0;i<listHolonElementTypeMaster.length-1;i++) {
		if((typeof holonElementTypeId != "undefined")&&(holonElementTypeId==listHolonElementTypeMaster[i].split("-")[0])){
			var options= "<option value="+listHolonElementTypeMaster[i].split("-")[0]+"id="+listHolonElementTypeMaster[i].split("-")[0]+" selected>"+listHolonElementTypeMaster[i].split("-")[1]+"</option>";
			$("#holonElementType").append(options);
		} else {
			var options= "<option value="+listHolonElementTypeMaster[i].split("-")[0]+"id="+listHolonElementTypeMaster[i].split("-")[0]+">"+listHolonElementTypeMaster[i].split("-")[1]+"</option>";
			$("#holonElementType").append(options);
		}
	}
}

/**
 * List of holon element state types
 * @param state current state of the element
 */
function getListHolonElementState(state) {
	var options={};
	if(typeof state != "undefined") {
		options={state:state};
	}
	ajaxRequest("getListHolonElementState", {}, getListHolonElementStateCallBack,options);
}


/**
 * callback method for ajax request from the method getListHolonElementState(state)
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function getListHolonElementStateCallBack(data,options) {
	var state =options['state'];
	var listHolonElementState= data.split("*");
	$("#holonElementState").empty();
	for(var i=0;i<listHolonElementState.length-1;i++) {
		if((typeof state != "undefined")&&(state==listHolonElementState[i].split("-")[0])) {
			var options= "<option value="+listHolonElementState[i].split("-")[0]+" id= "+listHolonElementState[i].split("-")[0]+" selected>"+listHolonElementState[i].split("-")[1]+"</option>";
			$("#holonElementState").append(options);
		} else {
			var options= "<option value="+listHolonElementState[i].split("-")[0]+" id= "+listHolonElementState[i].split("-")[0]+">"+listHolonElementState[i].split("-")[1]+"</option>";
			$("#holonElementState").append(options);
			}
		}
}

/**
 * Function to add a new holon element to an Object
 */
function addHolonElement() {
	var holonElementTypeId=$("#holonElementType option:selected").val();
	var holonElementStateId=$("#holonElementState option:selected").val();
	var currentCapacity=$("#currentCapacity").val();
	var usage=$("#usage").val();
	var holonObjectId = $("#hiddenHolonObjectId").val();
	var holonElementActionState = $("#holonElementActionState").val();
	var holonElementId = $("#hiddenHolonElementId").val();
	var dataAttributes = {
			holonElementTypeId : holonElementTypeId,
			holonElementStateId : holonElementStateId,
			currentCapacity : currentCapacity,
			holonObjectId : holonObjectId,
			holonElementId : holonElementId
		};
	if(holonElementActionState == "Edit") {
		ajaxRequest("editHolonElement", dataAttributes, editHolonElementCallBack, dataAttributes);
	} else {
		ajaxRequest("createHolonElement", dataAttributes, addHolonElementCallBack, dataAttributes);
	}
}
/**
 * callback method for ajax request for createHolonElement from the method addHolonElement() 
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function addHolonElementCallBack(data, options) {
	var resp =data.split("*");
	if(resp[0] == "true"){
		var holonObjectId = options['holonObjectId'];
		closeDiv('elementInfo');
		updateCoordinator(resp[1]);
		showHolonCoIcons();
		showHolonElements(holonObjectId);
		showPowerCircles(holonObjectId);
	} else {
		swal("Server Error!", "Error in adding Holon Element. Please check application logs.", "info");
	}
}

/**
 * callback method for ajax request for editHolonElement from the method addHolonElement() 
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function editHolonElementCallBack(data, options) {
	$("#holonElementActionState").val("");
	var resp =data.split("*");
	if(resp[0]  == "true"){
		var holonObjectId = options['holonObjectId'];
		closeDiv('elementInfo');
		updateCoordinator(resp[1]);
		showHolonCoIcons();
		showHolonElements(holonObjectId);
		showPowerCircles(holonObjectId);
	} else {
		swal("Server Error!","Error in editing Holon Element. Please check application logs.","info");
	}
}

/**
 * 
 * To edit holon element
 * @param holonElementId Id of the olon element 
 * @param holonElementTypeId Id of the holon element type
 * @param state Current state of the holon element
 * @param currentCapacity Current energy consumption by the holon element 
 * @param holonObjectId id of holon object associated with the holon element
 */
function editHolonElement(holonElementId,holonElementTypeId,state,currentCapacity, holonObjectId) {
	$("#hiddenHolonObjectId").val(holonObjectId);
	$("#hiddenHolonElementId").val(holonElementId);
	$("#holonElementActionState").val("Edit");	
	$("#currentCapacity").val(currentCapacity);
	$("#elmTitle").text("Edit Holon Element");
	getListHolonElementType(holonElementTypeId);
	getListHolonElementState(state);
	openDiv('elementInfo');
}

/**
 * Function to update the coordinator of a holon upon the element edit/add/remove
 * @param holonObjectId Id of the holon object whose elements have been changed 
 */
function updateCoordinator(holonObjectId) {
	var options= {
			  holonObjectId : holonObjectId,
			}
	ajaxRequest("updateCoordinator", {}, updateCoordinatorCallBack, options);
}

/**
 * callback method for ajax request for the method updateCoordinator(holonObjectId) 
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function updateCoordinatorCallBack(data,options) {
	var holonObjectId=options['holonObjectId'];
	var dataAttributes= {
			  holonObjectId : holonObjectId,
			}
	 ajaxRequest("getHolonObjectInfoWindow", dataAttributes, getHolonInfoWindowCallBack, {});
}