/**
 * This code in this file acts on a power source object
 */
$(document).ready(function() {
	var psDrawingManager;
	$('#addPowerSource').click(function(evt) {
		if (addPowerSourceMode==false){
			addPowerSourceMode=true;
			$(this).css("background-color", "rgb(153,153,0)");
			psDrawingManager = new google.maps.drawing.DrawingManager({
				drawingMode :google.maps.drawing.OverlayType.CIRCLE ,
				drawingControl : false,
				circleOptions : {
					 strokeColor:'#FF0000',
				     strokeOpacity: 1,
				     strokeWeight: 2,
				     fillColor: '#FF0000',
				     fillOpacity: 0.35,
				},
			});
			psDrawingManager.setMap(map);
		google.maps.event.addListener(psDrawingManager, 'overlaycomplete', function(event) {
				var psShape = event.overlay; // Object
				psShape.type = event.type;	
				createdPowerSourceObject=psShape;
		    	var center=psShape.getCenter();
		    	var radius=psShape.getRadius();		  	
		    	 $("#hiddenPowerObjectCenterLat").val(center.lat());
		    	 $("#hiddenPowerObjectCenterLng").val(center.lng());
		    	 $("#hiddenPowerObjectRad").val(radius);
		    	 $("#powerObjectActionState").val("Add");
		    	 $("#powerObjTitle").text("Add Power Source");
		    	 getHolonCoordinatorFromDatabase("","pwholonCoordinatorId","powerObjectDetail");
		   
		});
		} else {
			$(this).css("background-color", "rgb(26, 26, 26)");
			psDrawingManager.setMap(null);
			addPowerSourceMode=false;
		}
	})
	$("#savePowerObject").click(function(event){
		savePowerObject();						
	});
	$("#cancelPowerObject").click(function(event){
		if(createdPowerSourceObject!=null && typeof createdPowerSourceObject != 'undefined')
			{
			createdPowerSourceObject.setMap(null);
			}
		closeDiv("powerObjectDetail");
	});
})

/**
 *Method to save edited or created power source 
 */
function savePowerObject() {
	var psMaxProdCap=$("#psMaxProdCap").val();
	var psStatus=$("#psStatus option:selected").val();
	var radius=$("#hiddenPowerObjectRad").val();
	var latCenter=$("#hiddenPowerObjectCenterLat").val();
	var lngCenter=$("#hiddenPowerObjectCenterLng").val();
	var psActionState = $("#powerObjectActionState").val();
	var hiddenPowerObjectId = $("#hiddenPowerObjectId").val();
	$( "#powerObjectDetail" ).slideUp(100);
	var dataAttributes = {
			psMaxProdCap : psMaxProdCap,
			psStatus : psStatus,
			radius : radius,
			latCenter : latCenter,
			lngCenter : lngCenter,
			hiddenPowerObjectId : hiddenPowerObjectId
		};
	var options = {
			radius : radius,
			latCenter : latCenter,
			lngCenter : lngCenter,
			};
	if(psActionState == "Edit") {
		ajaxRequest("editPowerSourceObject", dataAttributes, editPowerSourceObjectCallBack, {});
	} else {
		ajaxRequest("createPowerSourceObject", dataAttributes, createPowerSourceObjectCallBack, {});							
	}
}

/**
 * callback method for ajax request createPowerSourceObject from  savePowerObject() method
 * @param data data returned from server
 * @param options data passed from client side
 */
function createPowerSourceObjectCallBack(data,options) {
	var resp = data.split("!");
	var psId=resp[0];
	var status=resp[1];
	var rad=options.radius;
	var centerLoc= new google.maps.LatLng(options.latCenter,options.lngCenter)
	var psStatusColor="#FF0000";
	if(status==1) {
		psStatusColor="#0B6121";
	}
	createdPowerSourceObject.setOptions({strokeColor:psStatusColor,fillColor: psStatusColor});
	addEventActionToPsObject(psId,createdPowerSourceObject);
	globalPSrcList.set(psId,createdPowerSourceObject);
}
/**
 * callback method for ajax request editPowerSourceObject from  savePowerObject() method
 * @param data data returned from server
 * @param options data passed from client side
 */
function editPowerSourceObjectCallBack(data,options) {
	var resp = data.split("!");
	var psId=resp[0];
	var status=resp[1];
	var psStatusColor="#FF0000";
	if(status==1) {
		psStatusColor="#0B6121";
	}
	var editedPowerSourceObject= globalPSrcList.get(psId.toString()); 
	editedPowerSourceObject.setOptions({strokeColor:psStatusColor,fillColor: psStatusColor});
	addEventActionToPsObject(psId,editedPowerSourceObject);
	var dataAttributes= {
			  psId : psId,
			}
	var option= {
			  powerSrc : editedPowerSourceObject,
			}
	ajaxRequest("getPsObjectInfoWindow", dataAttributes, getPsObjectInfoWindowCallBack, option);
	globalPSrcList.set(psId,editedPowerSourceObject);
}

/**
 * edit the power source
 * @param powerSrcId id of the power source to be edited
 * @param infowindowPsObject the infowindow object of the power source
 */
function editPowerSource(powerSrcId,infowindowPsObject) {
	var dataAttributes= {
			psId : powerSrcId
	}
	var options= {
			infowindowPsObject : infowindowPsObject
	}
	ajaxRequest("getPsObjectInfoWindow", dataAttributes, getPowerSrcDetailCallBack, options);
}

/**
 * callback method for ajax request in editPowerSource(powerSrcId,infowindowPsObject) method
 * @param data data returned from server
 * @param option data passed from client side
 */
function getPowerSrcDetailCallBack(data, option) {
	var resp =data.split("!");
	var powerSrcId=resp[0];
	var maxProd=resp[1];
	var currProd=resp[2];
	var CoHolonId=resp[3];
	var coHoLocation=resp[4];
	var powerStatus=resp[5];
	var minProd=resp[6];
	var latCenter=resp[7];
	var lngCenter=resp[8];
	var rad= resp[9];
	var coName= resp[10];
	 $("#hiddenPowerObjectCenterLat").text(coHoLocation.trim("~")[0]);
	 $("#hiddenPowerObjectCenterLng").text(coHoLocation.trim("~")[1]);
	 $("#hiddenPowerObjectRad").text(rad);
	 $("#psMaxProdCap").val(maxProd);
	 $("#psCurrentPord").val(currProd);
	 $("#powerObjectActionState").val("Edit");
	 $("#powerObjTitle").text("Edit Power Source");
	 $("#hiddenPowerObjectId").val(powerSrcId);
	 $("#psStatus").empty();
	 var selOptions= "<option value=1 selected>Yes</option><option value=0>No</option>"
		 if(powerStatus=="No")
			{
			 selOptions= "<option value=1 >Yes</option><option value=0 selected>No</option>"
			}
		$("#psStatus").append(selOptions);
	getHolonCoordinatorFromDatabase(coName,"pwholonCoordinatorId","powerObjectDetail");	
}

/**
 * adding info window and event objects to power source object 
 * @param psId power source id
 * @param createdPowerSourceObject power source object 
 */
function addEventActionToPsObject(psId,createdPowerSourceObject) {
	google.maps.event.addListener(createdPowerSourceObject, 'click', function(event) {
		if(addPowerSourceToLineMode==true) {
			connectPowerSourceOrHolonObjectToPowerLine(event.latLng,psId,"PSObject");
		} else {
			var dataAttributes= {
			  psId : psId,
			}
			var option= {
			  powerSrc : createdPowerSourceObject,
			}
			ajaxRequest("getPsObjectInfoWindow", dataAttributes, getPsObjectInfoWindowCallBack, option);
		}
	  });
}

/**
 * callback method for ajax request in addEventActionToPsObject(psId,createdPowerSourceObject)method
 * @param data data returned from server
 * @param option data passed from client side
 */
function getPsObjectInfoWindowCallBack(data,option) {
	var resp =data.split("!");
	var powerSrcId=resp[0];
	var maxProd=resp[1];
	var currProd=resp[2];
	var CoHolonId=resp[3];
	var coHoLocation=resp[4];
	var powerStatus=resp[5];
	var minProd=resp[6];
	var latCenter=resp[7];
	var lngCenter=resp[8];
	var holonName = resp[10];
	var flexibility = resp[11];
	var powerSrc=option['powerSrc'];
	var powerStatusVal="Producing";
	if(powerStatus=="false") {
		powerStatusVal="Not Producing";
	}
	var contentString=
		"<div class='table'><table>"+
		"<tr><td colspan='2' style='text-decoration: underline;'>Power Source Details</td></tr>"+
		"<tr><td><b>PowerSource Id: "+powerSrcId +"</td>"+
		"<td>Status: "+powerStatusVal+"</td></tr>"+
		"<tr><td>Maximum Production Capacity: "+maxProd+"</td>"+
		"<td>Current Production: "+currProd+"</td></tr>";
		if(CoHolonId==0) {
			contentString=contentString.concat("<tr><td>Coordinator Id: Not Part of any Holon</td>");
		} else {
			contentString=contentString.concat("<tr><td>Coordinator Id: <a href='#' id='CoHolonId'>"+CoHolonId+"</a> ("+holonName+" Holon)</td>");
		}
		contentString=contentString.concat("<td>Minimum Production Capacity: "+minProd+"</td></tr>");
		if(flexibility > 0) {
			contentString = contentString.concat("<tr><td colspan='2' style='color:green'>Flexibility: "+flexibility+"</td></tr>");
		} else {
			contentString = contentString.concat("<tr><td colspan='2'>Flexibility: "+flexibility+"</td></tr>");
		}
		
		contentString = contentString.concat("</table>"+
				"<br /><hr>"+
				"<table><tr><td colspan='2' style='text-align: center;'>");
		if(powerStatus=="true") {
			contentString = contentString.concat("<span class='button' id='powerOnOff' ><i class='fa fa-circle-o-notch'>&nbsp; Turn Power Off </i></span>");
		} else {
				contentString = contentString.concat("<span class='button' id='powerOnOff' ><i class='fa fa-circle-o-notch'>&nbsp; Turn Power On </i></span>");	
		}
		contentString = contentString.concat("&nbsp;&nbsp;&nbsp;&nbsp;<span class='button' id='editPowerSource' title='Edit Power Source'>"+
				"<i class='fa fa-pencil-square-o'>&nbsp; Edit Power Source</i></span></td></tr></table><hr>");
		closeOtherInfoWindows();
		var infowindowPsObject = new google.maps.InfoWindow({
		      content: contentString,
		      position:new google.maps.LatLng(latCenter,lngCenter)
		  });
		infowindowPsObject.open(map,map);
		$('#editPowerSource').click(function() {
			editPowerSource(powerSrcId,infowindowPsObject);			
		})
		
		$('#powerOnOff').click(function() {
			powerSourceOnOff(powerSrc,powerSrcId,infowindowPsObject);			
		})
			$('#CoHolonId').click(function() {
				zoomToHolon(CoHolonId,coHoLocation,"Holon Object");			
		})
				
		currentPsInfoWindowObject=infowindowPsObject;
}

/**
 * switch on or switch off the power source
 * @param powerSrc the power source to be acted upon
 * @param powerSrcId id of the power source to be acted upon
 * @param infowindowPsObject info window object of power source to be acted upon
 */
function powerSourceOnOff(powerSrc,powerSrcId,infowindowPsObject) {
	var	dataAttributes = {
					powerSrcId:powerSrcId,
	    			};
		var options = {
			powerSrc:powerSrc,
			infowindowPsObject:infowindowPsObject,
			powerSrcId:powerSrcId,
			};
		ajaxRequest("powerSourceOnOff", dataAttributes, powerSourceOnOffCallBack, options);
		globalPSrcList.set(powerSrcId,powerSrc);
}
/**
 * callback method for ajax request in powerSourceOnOff(powerSrc,powerSrcId,infowindowPsObject) method
 * @param data data returned from server
 * @param option data passed from client side
 */
function powerSourceOnOffCallBack(data, options) {
	var powerSrc = options["powerSrc"];
	var infowindowPsObject = options["infowindowPsObject"];
	var powerSrcId = options["powerSrcId"];
	var content = infowindowPsObject.getContent();
	var resp = data.split("!");
	var coHolonId = resp[0];
	var coHoLocation = resp[1];
	var newPowerSrcStatus = resp[2];
	var maxProd = resp[3];
	var minProd = resp[4];
	var currentProd = resp[5];
	var flexibility = resp[6];
	var powerStatusVal;
	if(newPowerSrcStatus== 1) {
		powerSrc.setOptions({strokeColor:'#0B6121',fillColor: '#0B6121'});
		powerStatusVal = "Producing";
		currentProd = maxProd;
		flexibility = maxProd;
	} else {
		powerSrc.setOptions({strokeColor:'#FF0000', fillColor: '#FF0000'});
		powerStatusVal = "Not Producing";
		currentProd = 0;
		flexibility = 0;
	}

	var contentString=
		"<div class='table'><table>"+
		"<tr><td colspan='2' style='text-decoration: underline;'>Power Source Details</td></tr>"+
		"<tr><td><b>PowerSource Id: "+powerSrcId +"</td>"+
		"<td>Status: "+powerStatusVal+"</td></tr>"+
		"<tr><td>Maximum Production Capacity: "+maxProd+"</td>"+
		"<td>Current Production: "+currentProd+"</td></tr>";
		if(coHolonId==0) {
			contentString=contentString.concat("<tr><td>Coordinator Id: Not Part of any Holon</td>");
		} else {
			contentString=contentString.concat("<tr><td>Coordinator Id: <a href='#' id='CoHolonId'>"+coHolonId+"</a></td>");
		}
		
		contentString = contentString.concat("<td>Minimum Production Capacity: "+minProd+"</td></tr>");
		if(flexibility > 0) {
			contentString = contentString.concat("<tr><td style='color:green'>Flexibility: "+flexibility+"</td><td></td></tr>");
		} else {
			contentString = contentString.concat("<tr><td>Flexibility: "+flexibility+"</td><td></td></tr>");
		}
		contentString =  contentString.concat("</table>"+
				"<br /><hr>"+
				"<table><tr><td colspan='2' style='text-align: center;'>");
		if(newPowerSrcStatus == 1) {
			contentString = contentString.concat("<span class='button' id='powerOnOff' ><i class='fa fa-circle-o-notch'>&nbsp; Turn Power Off </i></span>");
			} else {
				contentString = contentString.concat("<span class='button' id='powerOnOff' ><i class='fa fa-circle-o-notch'>&nbsp; Turn Power On </i></span>");	
			}
		contentString = contentString.concat("&nbsp;&nbsp;&nbsp;&nbsp;<span class='button' id='editPowerSource' title='Edit Power Source'><i class='fa fa-pencil-square-o'>&nbsp; Edit Power Source</i></span>"+
		"</td></tr></table><hr>");
		infowindowPsObject.setContent(contentString);
		infowindowPsObject.close();	
	infowindowPsObject.open(map,powerSrc);
	$('#editPowerSource').click(function() {
		editPowerSource(powerSrcId,infowindowPsObject);			
	})
	$('#powerOnOff').click(function() {
		powerSourceOnOff(powerSrc,powerSrcId,infowindowPsObject);			
	})
	$('#CoHolonId').click(function() {
		zoomToHolon(coHolonId,coHoLocation,"Holon Object");			
	})
	currentPsInfoWindowObject=infowindowPsObject;
}



/**
 *Show saved power sources 
 */
function showSavedPowerSources() {
	ajaxRequest("showPowerSources", {}, showPowerSourcesCallBack, {});
}

/**
 * callback method for ajax request in showSavedPowerSources() method
 * @param data data returned from server
 * @param option data passed from client side
 */
function showPowerSourcesCallBack(data,option) {
	var res = data.replace("[", "").replace("]", "").split(',').join("");
	//alert(res);
	var psObjectsList = res.split("*");
	for (var i=0; i<psObjectsList.length-1; i++) {
		var psObject = psObjectsList[i];
		var pwSrcId =  psObject.split("#")[0].trim();
		var radius = psObject.split("#")[1].trim();	
		var floatRad=parseFloat(radius);
		var center = psObject.split("#")[2];
		var status = psObject.split("#")[3];
		var center_lat = center.split("~")[0].replace("[","").replace(",","");
		var center_lng = center.split("~")[1];
		var pscolor="red";
		if(status=="true")
			{
			pscolor="green";
			}
		//alert("a+"+color+"+b")
	    var savedPowerSourceFromDB = new google.maps.Circle({
			 strokeColor: pscolor,
		     strokeOpacity: 1,
		     strokeWeight: 2,
		     fillColor: pscolor,
		     fillOpacity: 0.35,
		     map: map,
		     center: new google.maps.LatLng(center_lat,center_lng),
		     radius: floatRad
		    });
	    addEventActionToPsObject(pwSrcId, savedPowerSourceFromDB);
	    globalPSrcList.set(pwSrcId,savedPowerSourceFromDB);
	}
	
}

/**
 * Create power source circle on the map : not used 
 * @param event
 */
function createPowerSource(event) {
	var pSource = new google.maps.Circle({
		 strokeColor:'#FF0000',
	     strokeOpacity: 0.8,
	     strokeWeight: 2,
	     fillColor: '#FF0000',
	     fillOpacity: 0.35,
	     map: map,
	     center: event.latlng,
	     radius: 3
	});
}