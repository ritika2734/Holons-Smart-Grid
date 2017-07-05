/**
 * This javascript file is used for the disaster Module
 */

$(document).ready(function(){
	// Click event for Create Disaster Mode in the left menu.
	$("#disasterMode").click(function(){
		disasterModeSelected();
	});
	
	//Click event for Remove Selected Disaster item on the left menu
	$("#removeSelectedDisaster").click(function(){
		deleteSelectedDisaster();
	});
	
	//Click event for Remove All Disasters item on the left menu.
	$("#removeAllDisaster").click(function(){
		deleteAllDisaster();
	});
});

/**
 * This function is used if the user clicks on the disaster Mode option present in the left menu.
 * Using this function the user is able to create a disaster circle on the map.
 */
function disasterModeSelected() {
	if (disasterMode==false){
		disasterMode=true;
		$("#disasterMode").css("background-color", "rgb(153,153,0)");
		disasterModeDrawingManager = new google.maps.drawing.DrawingManager({
			drawingMode :google.maps.drawing.OverlayType.CIRCLE ,
			drawingControl : false,
			circleOptions : {
				 strokeColor:'#888888',
			     strokeOpacity: 1,
			     strokeWeight: 2,
			     fillColor: '#888888',
			     fillOpacity: 0.35,
			},
		});
		disasterModeDrawingManager.setMap(map);
		google.maps.event.addListener(disasterModeDrawingManager, 'overlaycomplete', function(event) {
			var disasterObjectShape = event.overlay; // Object
			disasterObjectShape.type = event.type;	// Circle
			createdDisasterCircle=disasterObjectShape;
			//event.setVisible(false);
			var lat=event.overlay.center.lat();
			var lng=event.overlay.center.lng();
			var radius=event.overlay.radius;
			var dataAttributes= {
					latitude : lat,
					longitude : lng,
					radius : radius,
					zIndex:-1
					};
			ajaxRequest("createDisasterCircle", dataAttributes, createDisasterCircleCallback, {});
	});
	} else {
		$("#disasterMode").css("background-color", "rgb(26, 26, 26)");
		disasterModeDrawingManager.setMap(null);
		disasterMode=false;
	}

}

/**
 * This method is the callback function of the create disaster function.
 * This function sets the disaster in the global list of disaster to be able to access the same.
 * @callback createDisasterCircleCallback
 * @param data data from the database containing the disasterId,Latitude,Longitude of the disaster,
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function createDisasterCircleCallback(data,options){
	var response= data.split("*");
	var disasterCircleID= response[0];
	var diasaterCircleLatitude= response[1];
	var disasterCircleLongitude= response[2];
	var disasterColor= '#888888';
	createdDisasterCircle.setOptions({strokeColor:disasterColor,fillColor: disasterColor});
	attachClickEventForDisater(disasterCircleID,diasaterCircleLatitude,disasterCircleLongitude,createdDisasterCircle);
	globalDisasterList.set(disasterCircleID,createdDisasterCircle);
	
}

/**
 * This method shows all the saved disasters from the database.
 */
function showSavedDisasters(){
	ajaxRequest("getAllSavedDisasters", {}, getAllSavedDisastersCallback, {});
}

/**
 * This method is the callback function of getAllSavedDisasters
 * @callback getAllSavedDisastersCallback
 * @param data data contains all the information about the saved disasters in the database.
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function getAllSavedDisastersCallback(data,options){
	if(data != "[]"){
	var response=data.split("*");
	var disasterColor='#888888';
	$.each(response,function(index,value){
			disasterValues=value.replace("[","").replace("]","").split("^");
			if(disasterValues!= null || disasterValues!= ""){
			disasterCircleId=disasterValues[0];
			disasterCircleRadius=disasterValues[1];
			disasterValuesLatitude=disasterValues[2];
			disasterValuesLongitude=disasterValues[3];
			var disasterMarker= new google.maps.Circle({
				 strokeColor: disasterColor,
			     strokeOpacity: 1,
			     strokeWeight: 1,
			     fillColor: disasterColor,
			     fillOpacity: 0.35,
			     map: map,
			     center: new google.maps.LatLng(disasterValuesLatitude, disasterValuesLongitude),
			     radius: parseInt(disasterCircleRadius),
			     zIndex:-1
			    });	
			attachClickEventForDisater(disasterCircleId,disasterValuesLatitude,disasterValuesLongitude,disasterMarker);
			globalDisasterList.set(disasterCircleId,disasterMarker);
		}
	});
	}
}

/**
 * This method attaches the click event for the disaster.
 * This click event is used while deleting the particular disaster.
 * @param disasterId id of the disaster
 * @param disasterCircleLatitude latitude of the disaster
 * @param disasterCircleLongitude longitude of the disaster
 * @param createdDisasterCircle marker object of the created object on the map.
 */
function attachClickEventForDisater(disasterId,disasterCircleLatitude,disasterCircleLongitude,createdDisasterCircle){
	google.maps.event.addListener(createdDisasterCircle, 'click', function(event) {
		if(deleteSelectedDisasterMode){
			var dataAttributes={
					disasterId: disasterId
			}
			ajaxRequest("deleteDisasterCircleFromDatabase", dataAttributes, deleteDisasterCircleFromDatabaseCallback, {});
			
		}
   });
}


/**
 * This function enables the user to delete particular disaster by clicking on it.
 */
function deleteSelectedDisaster(){
	if(deleteSelectedDisasterMode == false){
		deleteSelectedDisasterMode =true;
		$("#removeSelectedDisaster").css("background-color", "rgb(153,153,0)");
	}else{
		$("#removeSelectedDisaster").css("background-color", "rgb(26, 26, 26)");
		deleteSelectedDisasterMode=false;
	}
}

/**
 * This function deletes all the disasters from the database.
 */
function deleteAllDisaster(){
	if(deleteAllDisasterMode== false){
		deleteAllDisasterMode=true;
		$("#removeAllDisaster").css("background-color", "rgb(153,153,0)");
		swal({
			title: "This will remove all disasters permanently!",
			text: "This action will remove all disasters from database. Do you want to continue?",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: '#DD6B55',
			confirmButtonText: 'Yes, remove all disasters!',
			cancelButtonText: "No, don't do anything!",
			closeOnConfirm: true,
			closeOnCancel: true
		},
		function(isConfirm) {
		    if (isConfirm) {
		    	ajaxRequest("deleteAllDisasterCircleFromDatabase", {}, deleteAllDisasterCircleFromDatabaseCallback, {});	
			} else {
				$("#removeAllDisaster").css("background-color", "rgb(26, 26, 26)");
				deleteAllDisasterMode=false;
			}
		});
	} else {
		$("#removeAllDisaster").css("background-color", "rgb(26, 26, 26)");
		deleteAllDisasterMode=false;
	}
}

/**
 * This method is the callback of the deleteAllDisasterCircleFromDatabase.
 * @callback deleteAllDisasterCircleFromDatabaseCallback
 * @param data data contains all the deleted disater Id's to delete them from global list. 
 * @param option This is used by the callback method. Any relevant information can be sent in this.
 */
function deleteAllDisasterCircleFromDatabaseCallback(data,option){
	$("#removeAllDisaster").css("background-color", "rgb(26, 26, 26)");
	deleteAllDisasterMode=false;
	//Remove all disaster IDs from the global list
	if(data!= undefined && data!= null && data!="failure"){
		var disasterIds=data.split("*");
		$.each(disasterIds,function(key,value){
			if(value!= null && value != ""){
				deleteDisasterMarkerFromGlobalList(value);
			}
		});
	}
}

/**
 * This function is the callback of the function deleteDisasterCircleFromDatabase
 * @callback deleteDisasterCircleFromDatabaseCallback
 * @param data data contains the id of the disaster.
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function deleteDisasterCircleFromDatabaseCallback(data,options){
	var disasterId= data.split("*");
	if(data!="failure" && disasterId[0] != undefined && disasterId[0] != null){
		deleteDisasterMarkerFromGlobalList(disasterId[0]);	
	}
}

/**
 * This function removes the disaster Circle from the map.
 * @param disasterId id of the disaster to be made invisible.
 */
function deleteDisasterMarkerFromGlobalList(disasterId){
	var disasterCircleMarker= globalDisasterList.get(disasterId);
	if(disasterCircleMarker!= null && disasterCircleMarker!= undefined && disasterCircleMarker!= ""){
		disasterCircleMarker.setVisible(false);
		delete globalDisasterList[disasterId];
	}
}