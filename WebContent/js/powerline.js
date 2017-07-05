/**
 * This JS file contains code related to drawing a power line on the map.
 */
var lineShape;
$(document).ready(function(){
	var lineDrawingManager;
	var lineArray=[];
	//Click event to add new Power Line
	$('#addPowerLine').click(function() {
		var bgColor=$(this).css("background-color");
		if (drawPowerLineMode==false){
			drawPowerLineMode=true;
			$(this).css("background-color", "rgb(153,153,0)");
			lineDrawingManager = new google.maps.drawing.DrawingManager({
				// Draw the power line on the map.
				drawingMode :google.maps.drawing.OverlayType.POLYLINE ,
				drawingControl : false,
				polylineOptions : {
					geodesic : true,
					clickable : true,
					strokeColor : "rgb(0, 0, 0)",
					strokeOpacity : 2.0,
					strokeWeight : 4.0
				}
			});
			// Setting the layout on the map 
			lineDrawingManager.setMap(map);
			google.maps.event.addListener(lineDrawingManager, 'overlaycomplete', function(event) {
				lineShape = event.overlay; // Object
				lineShape.type = event.type;	
				var start=lineShape.getPath().getAt(0);
				var end=lineShape.getPath().getAt(1);
				var latLangArr=[start,end];
				lineArray.push(latLangArr);		    	
				createdPowerLineObject=lineShape;
				var snappedStart= findSnappedLocation(start);		    	
				var snappedEnd= findSnappedLocation(end);		    	
				$("#powerLineStartLat").text(snappedStart.lat());
				$("#powerLineStartLng").text(snappedStart.lng());
				$("#powerLineEndLat").text(snappedEnd.lat());
				$("#powerLineEndLng").text(snappedEnd.lng());
				$("#powerLineTitle").text("Add Power Line");
				$("#powerLineType").text("MAINLINE");
				openDiv("lineObjectDetail");
			});
		} else {
			$(this).css("background-color", "rgb(26, 26, 26)");
			lineDrawingManager.setMap(null);
			drawPowerLineMode=false;
		}

	})
	// Calls savePowerLineObject() function to save the Power Line Object in Database
	$("#savePowerLineObject").click(function(event){
		savePowerLineObject();						
	});
	// Click event on Cancel Button of Draw Power Line 
	$("#cancelPowerLine").click(function(event){
		if(createdPowerLineObject!=null && typeof createdPowerLineObject != 'undefined')
		{
			createdPowerLineObject.setMap(null);
		}
		closeDiv("lineObjectDetail");
	});
})

/**
 * This function save the Power Line object into the database.
 * The saved powerLine can be either a newly created power line or an existing power line which is edited.
 * It uses drawPowerLine to save newly created power line & editPowerLine to save edited value
 */
function savePowerLineObject(){
	var startLat=$("#powerLineStartLat").text();
	var startLng=$("#powerLineStartLng").text();
	var endLat=$("#powerLineEndLat").text();
	var endLng=$("#powerLineEndLng").text();
	var maxCapacity=$("#powerLineCapacity").val();
	var powerLineObjectActionState = $("#powerLineObjectActionState").text();
	var powerLineId = $("#powerLineIdHidden").text();
	var holonObjectId = $("#powerLineHolonObjectIdHidden").text();
	var powerLineForSubLine=$("#powerLineIdForSubLine").text();
	var powerLineType=$("#powerLineType").text();
	$( "#lineObjectDetail" ).slideUp(100);
	$("#powerLineObjectActionState").text("ADD");
	$("#powerLineIdHidden").text("");
	$("#powerLineCapacity").text("");
	$("#powerLineType").text("");
	$("#powerLineHolonObjectIdHidden").text("");
	$("#powerLineIdForSubLine").text("");
	var dataAttributes = {
			powerLineType : powerLineType,
			maxCapacity : maxCapacity,
			latStart : startLat,
			lngStart : startLng,
			latEnd : endLat,
			lngEnd : endLng,
			isConnected :false,
			reasonDown : "",
			powerLineId:powerLineId,
			holonObjectId:holonObjectId,
			powerLineForSubLine:powerLineForSubLine
	};	
	var options = {
			lineShape:createdPowerLineObject,
			path:[new google.maps.LatLng(startLat, startLng),new google.maps.LatLng(endLat,endLng)],
	};	
	if(powerLineObjectActionState=="Edit"){
		ajaxRequest("editPowerLine", dataAttributes, editPowerLineObjectCallBack,{});
	}else {
		ajaxRequest("drawPowerLine", dataAttributes, drawPoweLineCallBack,options);
	}
}

/**
 * This function is the callback of editPowerLine
 * @callback editPowerLineObjectCallBack
 * @param data data contains information about the power Line to be shown in the info window. 
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function editPowerLineObjectCallBack(data, options){
	var powerLineId =data.split("*")[1];
	var content = getLineInfoWindowContent(data);
	var color = data.split("*")[9];
	var newLineShape=globalPlList.get(powerLineId.toString());
	newLineShape.setOptions({strokeColor:color});
	currentLineInfoWindowObject.setContent(content);
	$('#editPowerLineObject').click(function() {
		editPowerLine(powerLineId);			
	})
	// Setting global list of power Line 
	globalPlList.set(powerLineId, newLineShape);
}

/**
 * This function is the callback of drawPoweLine function
 * @callback drawPoweLineCallBack
 * @param data data contains information of the power line 
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function drawPoweLineCallBack(data, options){
	var newLineShape = options["lineShape"];
	var path = options["path"];
	var dataArray = data.split("!");
	var powerLineId = dataArray[0];
	var color = dataArray[1];
	newLineShape.setOptions({strokeColor:color,path: path});
	addMessageWindow(newLineShape,powerLineId)
	globalPlList.set(powerLineId, newLineShape);
	var powerLineAId = dataArray[2];
	var powerLineBId = dataArray[3];
	var holonObjectColor= dataArray[4];
	var holonObjectId = dataArray[5];
	var isCoordinator = dataArray[6];
	var coordinatorLocation="";
	var coordinatorIcon="";
	if(typeof powerLineAId != 'undefined' && typeof powerLineBId != 'undefined' && powerLineAId!= 0) {
		
		updateGlobalPowerLineList(powerLineAId,true);
		updateGlobalPowerLineList(powerLineBId,false);
	}
	
	if(typeof holonObjectColor != 'undefined' && typeof holonObjectId != 'undefined' && holonObjectId != "null" ){
		 var holonObject= globalHoList.get(holonObjectId);
		 holonObject.setOptions({strokeColor:holonObjectColor,fillColor:holonObjectColor });
		}
	if(typeof isCoordinator != 'undefined' && typeof holonObjectId != 'undefined'){
		if(isCoordinator=="Yes"){
			createIconOnMap(holonObjectId);
		}
		else{
			var newCoordinatorIds= dataArray[7].split("~");
			var oldCoordinatorIds= dataArray[8].split("~");
			
			if(oldCoordinatorIds != undefined)
				{
						for (var i=0;i< oldCoordinatorIds.length-1;i++){
							var oldCoordinatorId= oldCoordinatorIds[i];
							removeIconFromMap(oldCoordinatorId);
						}
				}
			if(newCoordinatorIds != undefined){
				for(var i=0;i< newCoordinatorIds.length-1; i++){
					var newCoordinatorId =  newCoordinatorIds[i];
					createIconOnMap(newCoordinatorId);
				}
			}
						
						
				
				
			
		}
	}
}

/**
 * This function is called whwn the user edit the power line and call updatePowerLine action to update the new data
 * @param powerLineId id of the power line
 */
function editPowerLine(powerLineId){
	var dataAttributes= {
			powerLineId : powerLineId,
	}
	ajaxRequest("updatePowerLine", dataAttributes, editPowerLineCallBack, {});	
}

/**
 * This function is the callback of updatePowerLine
 * @editPowerLineCallBack
 * @param data data contains information about the power line
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function editPowerLineCallBack(data,options){
	var powerLine = data.split("!");
	var powerLineId=powerLine[2].trim();
	var maxCapacity=powerLine[3].trim();
	$("#powerLineIdHidden").text(powerLineId);
	$("#powerLineCapacity").val(maxCapacity);
	$("#powerLineTitle").text("Edit Power Line");
	$("#powerLineObjectActionState").text("Edit");
	openDiv('lineObjectDetail');
}

/**
 * This function finds the snapped location from the database
 * @param lineLocation location to be snapped
 * @returns location object
 */
function findSnappedLocation(lineLocation){
	var finalLocation=lineLocation;
	var listSize=globalPlList.size;
	for(var i=1;i<=listSize;i++){
		var line = globalPlList.get(i.toString());
		if(line!=undefined){
			var startPath=line.getPath().getAt(0);
			var endPath=line.getPath().getAt(1);
			var tempCircleStart=new google.maps.Circle({
				center: startPath,
				radius: 10
			});	
			var tempCircleEnd=new google.maps.Circle({
				center: endPath,
				radius: 10,
			});	
			var isStartPath = google.maps.Circle.prototype.contains(lineLocation, tempCircleStart);
			var isEndPath = google.maps.Circle.prototype.contains(lineLocation, tempCircleEnd);
			if(isStartPath){
				finalLocation=startPath;
				return finalLocation;
			}
			else if(isEndPath){
				finalLocation=endPath;
				return finalLocation;
			}
		}
		else{
			listSize++; //To increase the size if a number is missing from id seq like 1,2,4,5,7 . Here Ids 3 and 6 are missing
		}
	}
	return finalLocation;
}

/**
 * This function shows the saved power lines from the database 
 */
function showSavedPowerLines(){
	ajaxRequest("showPowerLines", {}, showPowerLinesCallBack, {});
}

/**
 * This function is the callback of showPowerLines
 * @callback showPowerLinesCallBack
 * @param data data contains information about the saved power line.
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function showPowerLinesCallBack(data, options){
	var powerLineList = data.split("*");
	var powerLines=[];
	for (var i=0; i<powerLineList.length-1; i++) {
		var powerLine = powerLineList[i].split("!");
		var location = powerLine[0];
		var color=powerLine[1];
		var powerLineId=powerLine[2].trim();
		var startLat = location.split("^")[0].split("~")[0].replace("[","").replace(",","");
		var startLng = location.split("^")[0].split("~")[1];
		var endLat = location.split("^")[1].split("~")[0];
		var endLng = location.split("^")[1].split("~")[1];
		var line = new google.maps.Polyline({
			path: [
			       new google.maps.LatLng(startLat, startLng), 
			       new google.maps.LatLng(endLat, endLng)
			       ],
			       strokeColor:color,
			       strokeOpacity: 2.0,
			       strokeWeight: 4,
			       map: map
		});
		addMessageWindow(line,powerLineId);
		//Setting global list of Power Line
		globalPlList.set(powerLineId, line);
	}	
}

/**
 * This method adds the click event to the power line
 * @param line marker of the power line
 * @param powerLineId id of that marker(Power line) object
 */
function addMessageWindow(line,powerLineId){		
	google.maps.event.addListener(line, 'click', function(event) {
		if(connectHolonObjectToPowerLineMode==true || addPowerSourceToLineMode==true){
			connectPowerSourceOrHolonObjectToPowerLine(event.latLng,powerLineId.trim(),"PowerLine");
		}else if(addSwitchonPowerLineMode==true){
			createPowerSwitch(event.latLng,powerLineId.trim());
		}else{
				var dataAttributes= {
				powerLineId : powerLineId,
			}
			var options= {
					position:event.latLng,
					powerLineId : powerLineId
			}
			// ajax request to get information about the power line
			ajaxRequest("getPowerLineInfo", dataAttributes, getPowerLineInfoCallBack, options);		
		}
	});
}

/**
 * This functions closes all the other info windows present on the map.
 */
function closeOtherInfoWindows(){
	if(typeof currentSwitchInfoWindow != 'undefined' && currentSwitchInfoWindow != null){
		currentSwitchInfoWindow.close();
	}
	if(typeof currentInfoWindowObject != 'undefined' && currentInfoWindowObject != null){
		currentInfoWindowObject.close();
	}
	if(typeof currentLineInfoWindowObject != 'undefined' && currentLineInfoWindowObject != null){
		currentLineInfoWindowObject.close();
	}
	if(typeof currentPsInfoWindowObject != 'undefined' && currentPsInfoWindowObject != null){
		currentPsInfoWindowObject.close();
	}
}

/**
 * This function is the callback function of getPowerLineInfo
 * @callback getPowerLineInfoCallBack
 * @param data data contains information related to the power line
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function getPowerLineInfoCallBack(data,options){
	closeOtherInfoWindows();
	var position=options["position"];
	var powerLineId =options["powerLineId"];
	var content= getLineInfoWindowContent(data);	

	var infowindowHolonObject = new google.maps.InfoWindow({
		content: content,		    
	});
	infowindowHolonObject.setOptions({position:position});
	infowindowHolonObject.open(map,map);
	$('#editPowerLineObject').click(function() {
		editPowerLine(powerLineId);			
	})
	currentLineInfoWindowObject=infowindowHolonObject;
}


/**
 * This function returns the content to be displayed on the info window.
 * @param data data contains related information about the power line.
 * @returns {String} the content string to be shown
 */
function getLineInfoWindowContent(data){
	var respStr= data.split("*");
	var isConnected=respStr[0];
	var powerLineId=respStr[1];
	var maximumCapacity=respStr[2];
	var currentCapacity=respStr[3];
	var lineType=respStr[4];
	var source=respStr[5];
	var dest=respStr[6];
	var holonObjectIdForSubline=respStr[7];
	var powerSrcIdForSubline=respStr[8];
	var content= "<div class='table'><table>"+
	"<tr><td colspan='2' style='text-decoration: underline;'>Power Line Detail</td></tr>" +
	"<tr><td><b>PowerLine Id: </b>"+powerLineId +"</td>"+
	"<td><b>Connected: </b>"+isConnected+"</td></tr>"+
	"<tr><td><b>Maximum Capacity: </b>"+maximumCapacity+"</td>"+
	"<td><b>Current Capacity: </b>"+currentCapacity+"</td></tr>"+
	"<tr><td><b>PowerLine Type: </b>"+lineType+"</td>";
	if(lineType==="SUBLINE"){
		content = content.concat("<td><b>Connected Holon Object Id: </b>"+holonObjectIdForSubline+"</td></tr></div>");
	} else if(lineType==="POWERSUBLINE"){
		content = content.concat("<td><b>Connected Power Source Id: </b>"+powerSrcIdForSubline+"</td></tr></div>");
	} else{
		content = content.concat("<td></td></td></tr></div>");
	}
	content = content.concat("<div class = 'table'><table><tr><td colspan='2'>Connected Power Lines</td></tr>")
	var powerLineIds = respStr[10];
	var powerLineIdsArray = [];
	if(powerLineIds != "") {
		powerLineIdsArray = powerLineIds.split("~");
	}
	var powerLineLocationLatitude;
	var powerLineLocationLongitude;
	var connectedPowerLineId;
	for(var i = 0; i < powerLineIdsArray.length; i+=2) {
		connectedPowerLineId = powerLineIdsArray[i].split("!")[0];
		powerLineLocationLatitude = powerLineIdsArray[i].split("!")[1].split("^")[0];
		powerLineLocationLongitude = powerLineIdsArray[i].split("!")[1].split("^")[1];
		content = content.concat("<tr><td>Power Line: <a href='#' onclick='zoomToPowerLine("+connectedPowerLineId+","+powerLineLocationLatitude+","+powerLineLocationLongitude+")' id='connectedPowerLineId_"+connectedPowerLineId+"'>"+connectedPowerLineId+"</a></td>");
		if(typeof powerLineIdsArray[i+1] != 'undefined') {
			connectedPowerLineId = powerLineIdsArray[i+1].split("!")[0];
			powerLineLocationLatitude = powerLineIdsArray[i+1].split("!")[1].split("^")[0];
			powerLineLocationLongitude = powerLineIdsArray[i+1].split("!")[1].split("^")[1];
			content = content.concat("<td>Power Line: <a href='#' onclick='zoomToPowerLine("+connectedPowerLineId+","+powerLineLocationLatitude+","+powerLineLocationLongitude+")' id='connectedPowerLineId_"+connectedPowerLineId+"'>"+connectedPowerLineId+"</a></td></tr>");
		} else {
			content = content.concat("<td>&nbsp;</td></tr>");
		}
	}
	content = content.concat("<tr><td colspan='2' style='text-align: center;'>" +
			"<span class='button' id='editPowerLineObject'><i class='fa fa-pencil-square-o'></i>&nbsp;&nbsp;Edit Power Line</span></td></tr>" +
			"</div>");
	return content;
}

/**
 * This options update the power line global list 
 * @param powerLineId id of the power line 
 * @param toDelete is a boolean variable
 */
function updateGlobalPowerLineList(powerLineId,toDelete){
	var dataAttributes= {
			powerLineId : powerLineId,
	}	
	var options= {
			toDelete : toDelete,
	}	
	ajaxRequest("updatePowerLine", dataAttributes, updatePowerLineCallBack, options);
}

/**
 * This function is the callback of updatePowerLine. it update the power line global list by setting the
 * value to null for the one power line whose toDelte is true
 * @callback updatePowerLineCallBack
 * @param data information related to the power line
 * @param options This is used by the callback method. Any relevant information can be sent in this. Contains toDelete value.
 */
function updatePowerLineCallBack(data, options){
	var toDelete=options['toDelete'];
	var powerLine = data.split("!");
	var location = powerLine[0];
	var color=powerLine[1];
	var powerLineId=powerLine[2].trim();
	var startLat = location.split("^")[0].split("~")[0].replace("[","").replace(",","");
	var startLng = location.split("^")[0].split("~")[1];
	var endLat = location.split("^")[1].split("~")[0];
	var endLng = location.split("^")[1].split("~")[1];
	if(toDelete){
		//Remove power line from the map.
		globalPlList.get(powerLineId).setMap(null);
	}
	var line = new google.maps.Polyline({
		path: [
		       new google.maps.LatLng(startLat, startLng), 
		       new google.maps.LatLng(endLat, endLng)
		       ],
		       strokeColor:color,
		       strokeOpacity: 2.0,
		       strokeWeight: 4,
		       map: map
	});
	addMessageWindow(line,powerLineId);
	globalPlList.set(powerLineId,line);
}

/**
 * This function zooms to a particular power line
 * @param powerLineId id of the power line
 * @param powerLineLocationLatitude latitude of the power line where to zoom
 * @param powerLineLocationLongitude longitude of the power line where to zoom
 */
function zoomToPowerLine(powerLineId, powerLineLocationLatitude, powerLineLocationLongitude) {
	var location = new google.maps.LatLng(powerLineLocationLatitude, powerLineLocationLongitude);
	var dataAttributes= {
			powerLineId : powerLineId,
		}
	var options= {
			position:location,
			powerLineId : powerLineId
	}
	ajaxRequest("getPowerLineInfo", dataAttributes, getPowerLineInfoCallBack, options);		
	map.setCenter(location);
	map.setZoom(18);
	
}
