/**
 * Java Script file for functions related to holon objects
 */

var holonEditOptions = {};
var isConnectedFirst="";
var isConnectedSecond="";
var hoShape;

$(document).ready(function() {

	showHolonObjects();
	var holonDrawingManager;
	
	$('#areConnected').click(function(evt) {
		if (areConnectedMode==false){
			areConnectedMode=true;
			$(this).css("background-color", "rgb(153,153,0)");
		} else {
			$(this).css("background-color", "rgb(26, 26, 26)");
			areConnectedMode=false;
			isConnectedFirst="";
			isConnectedSecond="";
		}
	});
	
	$('#addHolonObject').click(function() {
	if (drawHolonObjectMode==false) {
			$(this).css("background-color", "rgb(153,153,0)");
			drawHolonObjectMode=true;
			//Creates a new drawing manager object for first time
			holonDrawingManager = new google.maps.drawing.DrawingManager({
    	    drawingMode: google.maps.drawing.OverlayType.RECTANGLE,
    	    drawingControl: false,
    	    rectangleOptions: {
                geodesic:true,
                clickable: true,
                strokeColor:"black",
                zIndex : 10
            }
    	    });
     // Setting the layout on the map 
			holonDrawingManager.setMap(map);
     // Event when the overlay is complete 
      google.maps.event.addListener(holonDrawingManager, 'overlaycomplete', function(event) {
    	  hoShape = event.overlay; // Object
    	  hoShape.type = event.type;	// Rectangle
    	  createdHolonObject=hoShape;
    	  var latNorthEast = hoShape.getBounds().getNorthEast().lat(); //get lat of northeast
    	  var lngNorthEast = hoShape.getBounds().getNorthEast().lng();	//get longitude of north east
    	  var latSouthWest = hoShape.getBounds().getSouthWest().lat();
    	  var lngSouthWest = hoShape.getBounds().getSouthWest().lng();
    	  $("#holonObjectLatitudeNE").text(latNorthEast);
    	  $("#holonObjectLongitudeNE").text(lngNorthEast);
    	  $("#holonObjectLatitudeSW").text(latSouthWest);
    	  $("#holonObjectLongitudeSW").text(lngSouthWest);
    	  $("#hoObjTitle").text("Add Holon Object");
    	  $("#holonObjectActionState").val("Add");
    	  getHolonObjectTypeFromDatabase();
    	  getHolonCoordinatorFromDatabase("","holonCoordinatorId","holonObjectDetail");
    	 
  	});
     //END of overlay Complete 
	}
	else
	{
		$(this).css("background-color", "rgb(26, 26, 26)");
		drawHolonObjectMode=false;
		holonDrawingManager.setMap(null);
		
	}
	
	})
		
	$("#showHolonObjects").click(showHolonObjects);
	
	$("#saveHolonObject").click(function(event){
		saveHolonObject();						
	});
	
	$("#cancelHolonObject").click(function(event){
		if(createdHolonObject!=null && typeof createdHolonObject != 'undefined')
			{
			createdHolonObject.setMap(null);
			}
		closeDiv("holonObjectDetail");
	});	
	
	$('#showHolonObjects').hover(function() {  // change cursor 
		$('#showHolonObjects').css('cursor','pointer');
			  });
	
	$("#close").click(function(){ //close pop ups
		$(this).parent().fadeOut("slow", function(c) {
        });
	})
})

/**
 * To save edited or created holon object
 */
function saveHolonObject(){
	//alert("saveHolonObject");
	var holonObjectPriority=$("#holonObjectPriority").val();
	var holonObjectType=$("#holonObjectType option:selected").val();
	var holonCoordinatorId=$("#holonCoordinatorId option:selected").val();
	var canCommunicate=$("#canCommunicate option:selected").val();
	var latNE=$("#holonObjectLatitudeNE").text();
	var lngNE=$("#holonObjectLongitudeNE").text();
	var latSW=$("#holonObjectLatitudeSW").text();
	var lngSW=$("#holonObjectLongitudeSW").text();
	var holonObjectActionState = $("#holonObjectActionState").val();
	var hiddenHolonObjectId = $("#hiddenHolonObjectId").val();
	$( "#holonObjectDetail" ).slideUp(100);
	var dataAttributes = {
			holonObjectType : holonObjectType,
			holonCoordinatorId : holonCoordinatorId,
			canCommunicate:canCommunicate,
			latNE : latNE,
			lngNE : lngNE,
			latSW : latSW,
			lngSW : lngSW,
			holonObjectPriority : holonObjectPriority,
			hiddenHolonObjectId : hiddenHolonObjectId
		};
	if(holonObjectActionState == "Edit"){
		ajaxRequest("editHolonObject", dataAttributes, editHolonObjectCallBack, holonEditOptions);
	} else {
		ajaxRequest("createHolonObject", dataAttributes, createHolonObjectCallBack, {});							
	}
}



/**
 * Edit existing holon object
 * @param holonObjectId Id of holon object to be edited
 * @param infowindowHolonObject infowindow object of the holon object to be edited
 */
function editHolonObject(holonObjectId,infowindowHolonObject) {
	//alert("editHolonObject");
	var dataAttributes= {
			holonObjectId : holonObjectId
	}
	var options= {
			infowindowHolonObject : infowindowHolonObject
	}
	ajaxRequest("getHolonObjectInfoWindow", dataAttributes, getHolonDetailCallBack, options);
}
/**
 * callback method for ajax request for the method editHolonObject(holonObjectId,infowindowHolonObject) 
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function getHolonDetailCallBack(data, option) {
	holonEditOptions = option;
	var dataArray = data.split("!");
	var holonObjectId = dataArray[0];
	var holonCoordinatorName_Holon= dataArray[1];	
	var holonObjectTypeName= dataArray[2];
	var ne_location= dataArray[3];
	var sw_location= dataArray[4];
	var lineConnectedState= dataArray[5];
	var holonColor= dataArray[6];
	var holonManagerName= dataArray[7];
	var canCommunicate=dataArray[24];
	var ne_latlng=ne_location.split("~");
	var sw_latlng=sw_location.split("~");
	 $("#holonObjectLatitudeNE").text(ne_latlng[0]);
	 $("#holonObjectLongitudeNE").text(ne_latlng[1]);
	 $("#holonObjectLatitudeSW").text(sw_latlng[0]);
	 $("#holonObjectLongitudeSW").text(sw_latlng[1]);
	 $("#holonObjectActionState").val("Edit");
	 $("#hoObjTitle").text("Edit Holon Object");
	 $("#hiddenHolonObjectId").val(holonObjectId);
	 $("#holonManagerName").val(holonManagerName);
	 $("#canCommunicate").empty();
	 var selOptions= "<option value=1 selected>Yes</option><option value=0>No</option>"
		 if(canCommunicate=="No")
			{
			 selOptions= "<option value=1 >Yes</option><option value=0 selected>No</option>"
			}
		$("#canCommunicate").append(selOptions);
	 getHolonObjectTypeFromDatabase(holonObjectTypeName);
	 getHolonCoordinatorFromDatabase(holonCoordinatorName_Holon.trim(),"holonCoordinatorId","holonObjectDetail");	
}

/**
 * callback method for ajax request editHolonObject from  the method saveHolonObject() 
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function editHolonObjectCallBack(data, options){
	
	var resp=data.split("!");
	var holonColor= resp[0];
	var holonObjectId=resp[1];
	
	var dataAttributes= {
			  holonObjectId : holonObjectId,
			}
	 ajaxRequest("getHolonObjectInfoWindow", dataAttributes, getHolonInfoWindowCallBack, {});
	 showHolonCoIcons();
	var editedHolonObject=globalHoList.get(holonObjectId.toString());
	editedHolonObject.setOptions({strokeColor:holonColor,fillColor: holonColor});
	attachMessage(holonObjectId,editedHolonObject);
	globalHoList.set(holonObjectId,editedHolonObject);

}

/**
 * callback method for ajax request createHolonObject from  the method saveHolonObject() 
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function createHolonObjectCallBack(data, options) {
	var dataArray = data.split("!");
	var holonObjectId = dataArray[0];	
	var isCoord= dataArray[1];
	var hc_ne_location= dataArray[2];
	var noOfHolons= dataArray[3];
	var holonColor= dataArray[4];
	createdHolonObject.setOptions({strokeColor:holonColor,fillColor: holonColor});
	 //When Rectangle is clicked
	 attachMessage(holonObjectId, createdHolonObject);
		showHolonCoIcons();
		globalHoList.set(holonObjectId,createdHolonObject);
	}
/**
 * callback method for ajax request for method editHolonObjectCallBack(data, options) and attachMessage(holonObjectId, rectangleFromFactory) 
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function getHolonInfoWindowCallBack(data,options) {
	var dataArray = data.split("!");
	var holonObjectId = dataArray[0];
	var holonCoordinatorName_Holon= dataArray[1];	
	var holonObjectTypeName= dataArray[2];
	var ne_location= dataArray[3];
	var sw_location= dataArray[4];
	var lineConnectedState= dataArray[5];
	var holonColor= dataArray[6];
	var coordHolonId= dataArray[7];
	var nOfElmInHolon=dataArray[8];
	var minEnergyHoObj=dataArray[9];
	var maxEnergyHoObj=dataArray[10];
	var originalEnergyHoObj=dataArray[11];
	var minEnergyProdObj=dataArray[12];
	var maxEnergyProdObj=dataArray[13];
	var cuEnergyProdObj=dataArray[14];	
	var nOfHolonObj=dataArray[15];
	var minEnergyHo=dataArray[16];
	var maxEnergyHo=dataArray[17];
	var cuEnergyHo=dataArray[18];
	var minEnergyProd=dataArray[19];
	var maxEnergyProd=dataArray[20];
	var cuEnergyProd=dataArray[21];
	var hoList=dataArray[22];
	var canCommunicate=dataArray[23];
	var coOrd_ne_location=dataArray[24];
	var createdFromFactory = dataArray[25];
	var currentEnergyRequired = dataArray[26];
	var flexibility = dataArray[27];
	var originalEnergyRequiredHolon = dataArray[28];
	var flexibilityHolon = dataArray[29];
	var lat=ne_location.split("~");
	var coordinatorCompetency = dataArray[30];
	var trustValue = dataArray[31];
	//alert(holonColor);
	var contentString=
			"<div class='table'><table>"+
			"<tr><td colspan='2' style='text-decoration: underline;'>Holon Object Details</td></tr>"+
			"<tr><td><b>Holon Object Id: "+holonObjectId +"</td>"+
			"<td>Holon Object Type: "+holonObjectTypeName+"</td></tr>"+
			"<tr><td>Line Connected State: "+lineConnectedState+"</td>";
	if(coordHolonId==0) {
		contentString=contentString.concat("<td>Coordinator Id: No Coordinator</td>");
	} else if(coordHolonId==-1) {
		contentString=contentString.concat("<td style='color:red'>Coordinator Id: Not connected</td>");
	} else {
		contentString=contentString.concat("<td>Coordinator Id: <a href='#' id='hoCoId'>"+coordHolonId+"</a></td>");
	}
	contentString = contentString.concat("</tr><tr><td>Coordinator Competency: "+coordinatorCompetency+"</td><td>Trust Value: "+trustValue+"</td></tr>");
	contentString = contentString.concat("<tr><td>Minimum Energy Req: "+minEnergyHoObj+"</td>"+
			"<td>Maximum Energy Req: "+maxEnergyHoObj+"</td></tr>"+
			"<tr><td>Original Energy Req: "+originalEnergyHoObj+"</td>"+
			"<td>Minimum Production capacity: "+minEnergyProdObj+"</td></tr>"+
			"<tr><td>Maximum Production Capacity: "+maxEnergyProdObj+"</td>"+
			"<td>Current Production: "+cuEnergyProdObj+"</td></tr>" +
			"<tr><td>Can Communicate: "+canCommunicate+"</td>");
	if(flexibility > 0) {
		contentString = contentString.concat("<td style='color:green'>Flexibility: "+flexibility+"</td></tr>");
	} else {
		contentString = contentString.concat("<td>Flexibility: "+flexibility+"</td></tr>");
	}
			
	if(currentEnergyRequired > 0) {
		contentString = contentString.concat("<tr><td style='color:red'>Current Energy Required: "+currentEnergyRequired+
				"&nbsp;&nbsp;&nbsp;&nbsp;<span class = 'button' id = 'sendMessageToAllProducers' title='Send message to peers'>"+
				"<img src='css/images/inbox.png' /></span></td>");
	} else {
		contentString = contentString.concat("<tr><td>Current Energy Required: "+currentEnergyRequired+"</td>");
	}
	contentString = contentString.concat(
			"<td>Created from factory: "+createdFromFactory+"</td></tr>" +
			"<tr rowspan='2'><td colspan='2' style='text-align: center;'>"+
			"<span class='button' id='supplierDetails' title='Show Supplier Details' onclick='showSupplierDetails("+holonObjectId+")'><i class='fa fa-bolt'>&nbsp;Supplier Details</i></span>"+
			"&nbsp;&nbsp;&nbsp;&nbsp;"+
			"<span class='button' id='consumptionGraph' title='Show Consumption' onclick='showConsumptionGraph("+holonObjectId+","+holonObjectId+","+'"ho"'+")'><i class='fa fa-line-chart'>&nbsp;Consumption</i></span>"+
			"&nbsp;&nbsp;&nbsp;&nbsp;"+
			"<span class='button' id='editHolonObject' title='Edit Holon Object'><i class='fa fa-pencil-square-o'>&nbsp;Edit Holon Object</i></span>"+
			"&nbsp;&nbsp;&nbsp;&nbsp;"+
			"<span class='button' id='showHolonElement' title='Show Holon Elements' onclick='showHolonElements("+holonObjectId+")'><i class='fa fa-info'>&nbsp;Holon Elements</i></span>" +
			"&nbsp;&nbsp;&nbsp;&nbsp;"+
			"<span class='button' id='checkInbox' title='Inbox'><i class='fa fa-list'>&nbsp;Inbox</i></span>"+
			"&nbsp;&nbsp;&nbsp;&nbsp;");
	if(holonColor == "black") {
		contentString = contentString.concat("<span class='button' id='doNotStartDynamicHolon' title='Start dynamic holon module'>" +
		"<i class='fa fa-object-ungroup'>&nbsp;Start Dynamic Holon</i></span>");
	} else {
		contentString = contentString.concat("<span class='button' id='startDynamicHolon' title='Start dynamic holon module'>" +
				"<i class='fa fa-object-ungroup'>&nbsp;Start Dynamic Holon</i></span>");
	}
	contentString = contentString.concat("</td></tr></table>");
	if(coordHolonId===holonObjectId) {
		contentString = contentString.concat(
				"<hr /><table>"+
				"<tr><td colspan='2' style='text-decoration: underline;'>Holon Details ("+holonColor+")</td></tr>" +
				"<tr><td>Number of Holon Objects: "+nOfHolonObj +"</td>"+
				"<td>Minimum Energy Req:"+minEnergyHo +"</td></tr>"+
				"<tr><td>Maximum Energy Req: "+maxEnergyHo +"</td>");
		if(cuEnergyHo > 0) {
			contentString = contentString.concat("<td style='color:red'>Current Energy Req: "+cuEnergyHo +"</td></tr>");
		} else {
			contentString = contentString.concat("<td>Current Energy Req: "+cuEnergyHo +"</td></tr>");
		}
		contentString = contentString.concat(
				"<tr><td>Minimum Production capacity: "+minEnergyProd +"</td>"+
				"<td>Maximum Production Capacity: "+maxEnergyProd +"</td></tr>"+
				"<tr><td>Current Production: "+cuEnergyProd +"</td>"+
				"<td>Original Energy Required: "+originalEnergyRequiredHolon +"</td></tr>");
		if(flexibilityHolon > 0 && cuEnergyHo > 0) {
			contentString = contentString.concat("<tr><td style='color:green'>Flexibility: "+flexibilityHolon +"</td>");
			contentString = contentString.concat("<td><span class='button' id='distributeEnergyAmongHolonObjects' " +
				"title='Distribute Holon energy among holon objects'><img src='css/images/distribute_energy.png'/><b>&nbsp;&nbsp;Distribute Energy</b></span>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='button' id='historyDistributeEnergyAmongHolonObjects' " +
				"title='History of Holon energy distribution among holon objects'><img src='css/images/history.png'/><b>&nbsp;&nbsp;History</b></span>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;<span class='button' id='dontDissolveHolonID' " +
				"title='Dissolve holon with other holon if flexibility is zero and current energy requirement is greater than zero.'>" +
				"<img src='css/images/dissolveHolon.png'/><b>&nbsp;&nbsp;Dissolve Holon</b></span>" +
				"</td>");
			contentString = contentString.concat("</tr>");
		} else {
			contentString = contentString.concat("<tr><td>Flexibility: "+flexibilityHolon +"</td>");
			contentString = contentString.concat("<td><span class='button' id='distributeEnergyAmongHolonObjectsFlexibilityZero' " +
					"title='Distribute Holon energy among holon objects'><img src='css/images/distribute_energy.png'/><b>&nbsp;&nbsp;Distribute Energy</b></span>" +
					"&nbsp;&nbsp;&nbsp;&nbsp;<span class='button' id='historyDistributeEnergyAmongHolonObjects' " +
					"title='History of Holon energy distribution among holon objects'><img src='css/images/history.png'/><b>&nbsp;&nbsp;History</b></span>" +
					"&nbsp;&nbsp;&nbsp;&nbsp;<span class='button' id='dissolveHolonID' " +
					"title='Dissolve holon with other holon if flexibility is zero and current energy requirement is greater than zero.'>" +
					"<img src='css/images/dissolveHolon.png'/><b>&nbsp;&nbsp;Dissolve Holon</b></span>" +
					"</td>");
				contentString = contentString.concat("</tr>");
		}
		
		contentString = contentString.concat("</table>"+
		"<table><tr><td colspan='2' style='text-align: center;'>Holon Objects in "+holonColor+" holon:&nbsp;&nbsp;"+
		"<select align = \"center\" name=\"infoWindowHolonList\" id=\"infoWindowHolonList\">"+hoList+"</select></td></tr>"+
		"</table>"
		);
	}
	contentString.concat("</div>");

	closeOtherInfoWindows();
	var infowindowHolonObject = new google.maps.InfoWindow({
	      content: contentString,
	      position:new google.maps.LatLng(lat[0],lat[1])
	  });
	infowindowHolonObject.open(map,map);
	$('#editHolonObject').click(function() {
		editHolonObject(holonObjectId,infowindowHolonObject);			
	});
	$('#hoCoId').click(function() {
			zoomToHolon(coordHolonId,coOrd_ne_location,"Holon Object");			
	});
	$('#checkInbox').click(function() {
		checkInbox(holonObjectId, canCommunicate);
	});
	$("#distributeEnergyAmongHolonObjects").click(function(){
		distributeEnergyAmongHolonObjects(holonObjectId);
	});
	$("#distributeEnergyAmongHolonObjectsFlexibilityZero").click(function(){
		distributeEnergyAmongHolonObjectsFlexibilityZero();
	});
	
	$("#historyDistributeEnergyAmongHolonObjects").click(function(){
		historyDistributeEnergyAmongHolonObjects(holonObjectId);
	});
	$('#sendMessageToAllProducers').click(function() {
		sendMessageToAllProducers(holonObjectId);
	});
	$('#startDynamicHolon').click(function() {
		startDynamicHolon(currentEnergyRequired, holonObjectId);
	}); 
	$('#doNotStartDynamicHolon').click(function() {
		doNotStartDynamicHolon();
	});
	$('#dissolveHolonID').click(function() {
		dissolveHolon(cuEnergyHo, coordHolonId);
	});
	$('#dontDissolveHolonID').click(function() {
		dontDissolveHolon();
	});
	
	$('#infoWindowHolonList').change(function(){
      if(jQuery("#infoWindowHolonList option:selected").val()!='Select Holon')
    	  {
    	  	zoomToHolon(jQuery("#infoWindowHolonList option:selected").val(),ne_location,"Holon Object");
    	  }
    });
	
	currentInfoWindowObject=infowindowHolonObject;
}

/**
 * zoom the map to a paricular location 
 * @param holonObjectId holon object id to which the map has to be zoomed 
 * @param neLoc ne coordinates of the holon object/ centre of the power source
 * @param type type of the object eg, holon object or power source 
 */
function zoomToHolon(holonObjectId,neLoc, type) {
	var location = new google.maps.LatLng(neLoc.split("~")[0], neLoc.split("~")[1]);
	if(type=="Holon Object") {
		var dataAttributes= {
				  holonObjectId : holonObjectId
				};
		 ajaxRequest("getHolonObjectInfoWindow", dataAttributes, getHolonInfoWindowCallBack, {});
	} else if(type=="Power Source"){
		var dataAttributes= {
				  psId : holonObjectId,
				};
		  var option= {
				  powerSrc : globalPSrcList.get(holonObjectId.toString()),
				};
		 ajaxRequest("getPsObjectInfoWindow", dataAttributes, getPsObjectInfoWindowCallBack, option);			
	}
	map.setCenter(location);
	map.setZoom(18);
}

/**
 *Function to load holons on the map  
 */
function showHolonObjects() {
	if(loadHolon){
	$("#showHolonObjects").css("background-color", "rgb(153,153,0)");
	ajaxRequest("showHolonObjects", {}, showHolonObjectsCallBack, {});
	showSavedPowerLines();
	showSavedPowerSwitches();
	showSavedPowerSources();
	showSavedDisasters();
	loadHolon=false;
	}else{
		$("#showHolonObjects").css("background-color", "rgb(26, 26, 26)");
		swal({
			title : "Holon already loaded.",
			text : "The Holon has already been loaded. Please clear the map first to reload it.",
			type : "info",
			confirmButtonText : "Okay."
		});
		
	}
}
/**
 * callback method for ajax request for method showHolonObjects()  
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function showHolonObjectsCallBack(data, options){
	//alert(data);
	var res = data.replace("[", "").replace("]", "").split(',').join("");
	//alert(res);
	var hoObjectsList = res.split("*");
	for (var i=0; i<hoObjectsList.length-1; i++) {
		var holonObject = hoObjectsList[i];
		var holonObjectId =  holonObject.split("#")[0].trim();
		var color = holonObject.split("#")[1].trim();	
		var location = holonObject.split("#")[2];
		var isCoord=holonObject.split("#")[3];
		var hasPower =holonObject.split("#")[4];
		var hasPowerOn=holonObject.split("#")[5];
		var ne_location_lat = location.split("^")[0].split("~")[0].replace("[","").replace(",","");
		var ne_location_lng = location.split("^")[0].split("~")[1];
		var sw_location_lat = location.split("^")[1].split("~")[0];
		var sw_location_lng = location.split("^")[1].split("~")[1];
		//alert("a+"+color+"+b")
	    var rectangleFromFactory = new google.maps.Rectangle({
		      map: map,
		      strokeColor :color,
		      fillColor:color,
		      bounds: new google.maps.LatLngBounds(
		    	      new google.maps.LatLng(sw_location_lat, sw_location_lng),
		    	      new google.maps.LatLng(ne_location_lat, ne_location_lng)),
    	      zIndex : 10
		    });
	    showPowerCircles(holonObjectId);
	    attachMessage(holonObjectId, rectangleFromFactory);
	 	globalHoList.set(holonObjectId,rectangleFromFactory);
	}
	

	 showHolonCoIcons();
	 $("#showHolonObjects").css("background-color", "rgb(26, 26, 26)");
}


/**
 * method to show power icons on the holon objects 
 * @param holonObjectId id of holon object on which icon has to be displayed
 */
function showPowerCircles(holonObjectId)
{
	var dataAttributes= {
			  holonObjectId : holonObjectId
			};
	var options=dataAttributes;
	ajaxRequest("getDetailForPowerSourceIcon", dataAttributes, getDetailForPowerSourceIconCallBack, options);
}
/**
 * callback method for ajax request for method showPowerCircles(holonObjectId)  
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function getDetailForPowerSourceIconCallBack(data,options)
{
	var resp = data.split("*");
	var hasPower = resp[0];
	var hasPowerOn=resp[1];
	var ne_location_lat=resp[2];
	var sw_location_lng=resp[3];
	var holonObjectId = options["holonObjectId"];	
	var powerColor = '#FF0000';
	var iconImage="css/images/bolt_off.png";
	//alert(hasPowerOn);
	   if(hasPowerOn=='true')
		   {
		   powerColor = '#336600'; // Green
		   iconImage = "css/images/bolt_on.png"
		   }
   // var labelTxt=  '<i style="color:'+powerColor+';" class="map-icon-electrician"></i>';
	var currecntPC=globalPCList.get(holonObjectId.toString());
	if(typeof(currecntPC) === "undefined")
		{
		if(hasPower=="true")
		 { 
			    currecntPC = new Marker({
					map: map,
					title: 'Power Producer',
					position: new google.maps.LatLng(ne_location_lat, sw_location_lng),
					zIndex: 9,
					icon: {
						path: ROUTE,
						fillColor: '#0E77E9',
						fillOpacity: 0,
						strokeColor: '',
						strokeWeight: 0,
						scale: 1/100
					},
					icon: iconImage
				});
			   
			    }
			
			}else
				{
					if(hasPower=="true"){
						//remove old icon
						currecntPC.setVisible(false);
						//Create new Icon
						currecntPC = new Marker({
							map: map,
							title: 'Power Producer',
							position: new google.maps.LatLng(ne_location_lat, sw_location_lng),
							zIndex: 9,
							icon: {
								path: ROUTE,
								fillColor: '#0E77E9',
								fillOpacity: 0,
								strokeColor: '',
								strokeWeight: 0,
								scale: 1/100
							},
							icon: iconImage
						});
						currecntPC.setIcon(iconImage);
						console.log(currecntPC);
						
						//currntPC.setOptions({strokeColor:powerColor, fillColor:powerColor,center:new google.maps.LatLng(ne_location_lat, sw_location_lng)});
					}else
						{
							currecntPC.setMap(null);
						
						}
					
				}
	globalPCList.set(holonObjectId,currecntPC);	
}

/**
 * Function to create info window and attach event actions to the holon object
 * @param holonObjectId  id of holon object for which info window has to be attached
 * @param rectangleFromFactory the holon object from the map 
 */
function attachMessage(holonObjectId, rectangleFromFactory) {
	google.maps.event.addListener(rectangleFromFactory, 'click', function(event) {	
		if(connectHolonObjectToPowerLineMode)
		{
			connectPowerSourceOrHolonObjectToPowerLine(event.latLng,holonObjectId,"HolonObject");
		}else if(areConnectedMode){
			if(isConnectedFirst=="" && isConnectedSecond=="")
				{
				isConnectedFirst=holonObjectId;
				}
			else if(isConnectedFirst!="" && isConnectedSecond=="")
				{
				 isConnectedSecond=holonObjectId;
				 areHolonObjectsConnected(isConnectedFirst,isConnectedSecond);
				 isConnectedFirst="";
				 isConnectedSecond="";
				}
			else 
				{
				alert("Please close the isConnected Mode" );
				}
			
		}/*else if(disasterModeDelete){
			var disasterId=holonObjectId;
			var disasterMarker= globalDisasterList.get(disasterId);
			showInfoWindowForDisater(disasterId,disasterMarker);
			
		}*/else{
			var dataAttributes={
					holonObjectId:holonObjectId	
			};
  ajaxRequest("getHolonObjectInfoWindow", dataAttributes, getHolonInfoWindowCallBack, {});		
		}
		
	  });
}

/**
 * Function to show holon coordinator Icons after any change
 */
function showHolonCoIcons()
{
	var redIcon= globalHKList.get("red");
	var blueIcon= globalHKList.get("blue");
	var greenIcon= globalHKList.get("green");
	var yellowIcon= globalHKList.get("yellow");
	if(typeof redIcon != "undefined")
	{		
		redIcon.setMap(null);
	}
	if(typeof blueIcon != "undefined")
	{		
		blueIcon.setMap(null);
	}
	if(typeof greenIcon != "undefined")
	{		
		greenIcon.setMap(null);
	}
	if(typeof yellowIcon != "undefined")
	{		
		yellowIcon.setMap(null);
	}
	ajaxRequest("getHoCoIcons", {}, getHoCoIconsCallBack, {});	

}

/**
 * callback method for ajax request for method showHolonCoIcons()
 * @param data  data returned from server 
 * @param options data passed from client side
 */
function getHoCoIconsCallBack(data,options) {
	var result=data.split("*");
	var holonObjectId="";
	var holonObjectColor="";
	var coordinatorLocation="";
	var coordinatorIcon="";
	for(var i=0;i<result.length;i++) {
		holonObjectId=globalHoList.get(result[i].split("~")[0]);
		holonObjectColor=result[i].split("~")[1];
		if(holonObjectId!= null && holonObjectId.getBounds() != null ){
			coordinatorLocation=holonObjectId.getBounds().getNorthEast();
			coordinatorIcon=createCoIcon(coordinatorLocation);
			globalHKList.set(result[i].split("~")[0],coordinatorIcon);
		}
		
	}
}

/**
 * create co ordinator icon
 * @param cLocation location of the icon 
 * @returns {Marker} created icon 
 */
function createCoIcon(cLocation) {
	 var coOrdCircle= new Marker({
			map: map,
			title: 'Holon Coordinator',
			position: cLocation,
			zIndex: 9,
			icon: {
				path: ROUTE,
				fillColor: '#0E77E9',
				fillOpacity: 0,
				strokeColor: '',
				strokeWeight: 0,
				scale: 1/100
			},
			icon : 'css/images/coordinator.png'
		});
	 
	 return coOrdCircle;

}


function deleteHolonObject(holonObjectId) {
	
}

/**
 * to close a div
 * @param id div id to be closed
 */
function closeDiv(id) {
	$("#"+id).slideUp(100);
}

/**
 *   to open  a div
 * @param id div id to be opened
 */
function openDiv(id) {
	$("#"+id).slideDown(100);
}

/**
 * Removing icon from map
 * @param objectId id of the object from which icon has to be removed
 */
function removeIconFromMap(objectId){
	var holonObjectMarkerIcon = globalHKList.get(objectId);
	console.log(holonObjectMarkerIcon.icon);
	var holonObject= globalHoList.get(objectId);
	if(holonObjectMarkerIcon.icon="css/images/coordinator.png"){
		holonObjectMarkerIcon.setVisible(false);
	}
	holonObjectMarkerIcon.setIcon("css/images/no_coordinator.png");
	holonObjectMarkerIcon.setTitle("No Coordinator");
	/*holonObjectMarkerIcon.setMap(null);
	holonObjectMarkerIcon.setVisible(false)
	delete globalHKList.objectId;
	delete globalHoList.objectId;
	globalHoList.set(holonObject);*/
	
	
}

/**
 *  adding icon from map
 * @param objectId id of the object on which icon has to be placed
 */
function createIconOnMap(objectId){
	
	var holonObject= globalHoList.get(objectId);
	var holonObjectPosition= holonObject.getBounds().getNorthEast();
	if(globalHKList.get(objectId)== null){
		var coordinatorIcon= new Marker({
			map: map,
			title: 'Holon Coordinator',
			position: holonObjectPosition,
			zIndex: 9,
			icon: {
				path: ROUTE,
				fillColor: '#0E77E9',
				fillOpacity: 0,
				strokeColor: '',
				strokeWeight: 0,
				scale: 1/100
			},
			icon : 'css/images/coordinator.png'
		});
		globalHKList.set(objectId,coordinatorIcon);
	}
	
	if(globalHKList.get(objectId).visible){
		
	}else{
		globalHKList.get(objectId).visible=true;
		globalHKList.get(objectId).setVisible(true);
		globalHKList.get(objectId).setIcon("css/images/coordinator.png");
		globalHKList.get(objectId).setTitle("Holon Coordinator");
		
	}
 
 
}