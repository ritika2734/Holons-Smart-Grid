/**
 * This javascript file is used are Connected Module of the Holon Objects to see whether the holon objects are connected or not.
 */

/**
 * This functions check whether the two Holon Objects are connected or not.
 * @param firstHolonObjectId id of the first Holon Object.
 * @param secondHolonObjectId id of the second Holon Object.
 */
function areHolonObjectsConnected(firstHolonObjectId,secondHolonObjectId)
{
	var dataAttributes= {
			firstHolonObjectId : firstHolonObjectId,
			secondHolonObjectId : secondHolonObjectId
			};
	var options ={
			firstHolonObjectId : firstHolonObjectId,
			secondHolonObjectId : secondHolonObjectId
	};
	ajaxRequest("getConnectedStatusForHolonObjects", dataAttributes, getConnectedStatusForHolonObjectsCallback, options);
	
}

/**
 * This function is the callback for getConnectedStatusForHolons
 * @callback getConnectedStatusForHolonsCallback
 * @param data data contains the connected status of the 2 holon objects
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function getConnectedStatusForHolonObjectsCallback(data,options) {
	areConnectedMode=false;
	var value="";
	if(data=="Success"){
		value="Yes";
	}else{
		value="No";
	}
	swal({
		title : "Connected::"+value,
		type : "info",
		confirmButtonText : "Okay"
	});
	$("#areConnected").css("background-color", "rgb(26, 26, 26)");
	areConnectedMode=false;
	isConnectedFirst="";
	isConnectedSecond="";
}