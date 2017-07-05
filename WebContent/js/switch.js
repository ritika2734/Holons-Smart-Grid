/*
 * This files contains power switch related functions
 */
$(document).ready(function() {	
	
	$("#switchOnPowerLine").click(function(event){		
		if (addSwitchonPowerLineMode==false) {
			$(this).css("background-color", "rgb(153,153,0)");
			addSwitchonPowerLineMode=true;
		}
		else
		{
			$(this).css("background-color", "rgb(26, 26, 26)");	
			addSwitchonPowerLineMode=false;
		}
	});
	
})



/**
 * create a power switch on the line
 * @param latLng location of the switch
 * @param powerLineId id of power line on which switch is being added
 */
function createPowerSwitch(latLng,powerLineId){
	var circleSwitch = new google.maps.Circle({
		 strokeColor: '#0B6121',
	     strokeOpacity: 1,
	     strokeWeight: 8,
	     fillColor: '#0B6121',
	     fillOpacity: 0.35,
	     map: map,
	     center: latLng,
	     radius: 2
	    });	
	var dataAttributes= {
			switchPositionLat : latLng.lat(),
			switchPositionLng : latLng.lng(),
			powerLineId : powerLineId
	};
	var options= {
			circleSwitch:circleSwitch,			
	};
	ajaxRequest("createPowerSwitch", dataAttributes, createPowerSwitchCallBack, options);
}

/**
 * callback method for ajax request from createPowerSwitch(latLng,powerLineId)
 * @param data data from server side 
 * @param options data from client side 
 */
function createPowerSwitchCallBack(data,options)
{
	var respData=data.split("*");
	var powerSwitchId=respData[0].trim();	
	var powerLineAId=respData[1].trim();
	var powerLineBId=respData[2].trim();
	updateGlobalPowerLineList(powerLineAId,true);
	updateGlobalPowerLineList(powerLineBId,false);
	var circleSwitch = options["circleSwitch"]; 
	addSwitchInfo(circleSwitch, powerSwitchId);
	//globalPsList[powerSwitchId]=circleSwitch;
	globalPsList.set(powerSwitchId,circleSwitch);
	
} 

/**
 * To populate power sources on the map
 */
function showSavedPowerSwitches(){
	
	ajaxRequest("getListPowerSwitch", {}, getListPowerSwitchCallBack, {});
}

/**
 * callback method for ajax request in showSavedPowerSwitches() method
 * @param data data from server side
 * @param options data from client side
 */
function getListPowerSwitchCallBack(data,options){
	var powerSwitchList= data.split("*");
	for(var i=0;i<powerSwitchList.length-1;i++)
		{		
		var individualData= powerSwitchList[i].split("^");
		var switchLat=individualData[0].replace("[","").replace(",","");
		var switchLong=individualData[1];
		var powerSwitchId=individualData[2].trim();
		var status=individualData[3];
		var switchStatus="#FF0000";		
		if(status==1){
			switchStatus="#0B6121";
		}
		var circleSwitch = new google.maps.Circle({
			 strokeColor: switchStatus,
		     strokeOpacity: 1,
		     strokeWeight: 8,
		     fillColor: switchStatus,
		     fillOpacity: 0.35,
		     map: map,
		     center: new google.maps.LatLng(switchLat, switchLong),
		     radius: 2
		    });	
		addSwitchInfo(circleSwitch, powerSwitchId);
		//globalPsList[powerSwitchId]=circleSwitch;
		globalPsList.set(powerSwitchId,circleSwitch);
		
		}
	
}

/**
 * Method to add info window and events to power switches 
 * @param circleSwitch the switch object
 * @param powerSwitchId the id of the power switch object 
 */
function addSwitchInfo(circleSwitch, powerSwitchId)
{
	
   google.maps.event.addListener(circleSwitch, 'click', function(event) { 
	   
		var dataAttributes= {
				powerSwitchId : powerSwitchId,
				}
		var options= {
				position:event.latLng,
				circleSwitch:circleSwitch,
				}
		ajaxRequest("getSwitchInfo", dataAttributes, getSwitchInfoCallBack, options);		   
	   
   });
}

/**
 * callback method for ajax request in addSwitchInfo(circleSwitch, powerSwitchId) method
 * @param data data from client side
 * @param options data from server side
 */
function getSwitchInfoCallBack(data, options)
{
	closeOtherInfoWindows();
	var individualData= data.split("^");
	var switchLat=individualData[0].replace("[","").replace(",","");
	var switchLong=individualData[1];
	var powerSwitchId=individualData[2].trim();
	var powerLineAId=individualData[3];
	var powerLineBId=individualData[4];
	var status=individualData[5].trim();
	var switchStatus="Off";
	var btnText="Switch On";
	if(status=="1")
		{
		switchStatus="On";
		btnText="Switch Off";
		}
		var contentString= "<div id='contentId' class='table'><table>"+
							"<tr><td colspan='2' style='text-decoration: underline;'>Switch Details</td></tr>" +
							"<tr><td><b>Switch Id: </b>"+powerSwitchId +"</td>"+
							"<td><b>Switch Status: </b>"+switchStatus+"</td></tr>"+
							"<tr><td><b>Connected Power Line A: </b>"+powerLineAId+"</td>"+
							"<td><b>Connected Power Line B : </b>"+powerLineBId+"</td></tr>"+
							"<tr><td colspan='2' style='text-align: center;'>" +
							"<span class='button' id='togglePowerSwitch'><i class='fa fa-circle-o-notch'></i>&nbsp;&nbsp;Turn "+btnText+"</span></td></tr></table></div>";

	var position=options["position"];
	var circleSwitch=options["circleSwitch"];
	var infowindowHolonObject = new google.maps.InfoWindow({
	      content: contentString		    
	  });
	 	infowindowHolonObject.setOptions({position:position});
		infowindowHolonObject.open(map,circleSwitch);	
		$('#togglePowerSwitch').click(function() {
			SwitchOnOff(circleSwitch,powerSwitchId,infowindowHolonObject);			
		})
	currentSwitchInfoWindow=infowindowHolonObject;
	
}


/**
 * method to on or off power switch 
 * @param circleSwitch power switch object
 * @param powerSwitchId id of power switch
 * @param infowindowHolonObject info window object of the power switch
 */
function SwitchOnOff(circleSwitch,powerSwitchId,infowindowHolonObject)
{	 

		var dataAttributes = {    			
    			powerSwitchId:powerSwitchId,
    			};	
		options = {
				circleSwitch:circleSwitch,
				infowindowHolonObject:infowindowHolonObject,
				powerSwitchId:powerSwitchId,
    			};
	
		ajaxRequest("powerSwitchOnOff", dataAttributes, powerSwitchOnOffCallBack,options);
		//globalPsList[powerSwitchId]=circleSwitch;
		globalPsList.set(powerSwitchId,circleSwitch);
	
}

/**
 * callback method for ajax request in SwitchOnOff(circleSwitch,powerSwitchId,infowindowHolonObject)
 * @param data data from client side
 * @param options data from server side 
 */
function powerSwitchOnOffCallBack(data,options){
	var circleSwitch = options["circleSwitch"];
	var infowindowHolonObject = options["infowindowHolonObject"];
	var powerSwitchId = options["powerSwitchId"];
	var content = infowindowHolonObject.getContent();
	var newSwitchStatus = data.split("*")[0];
	var newCoordinatorIds = undefined;
	if(data.split("*")[1] != undefined) {
		newCoordinatorIds = data.split("*")[1].split("!");
	}
	var oldCoordinatorIds = undefined;
	if(data.split("*")[2] != undefined) {
		oldCoordinatorIds=data.split("*")[2].split("!");
	}
	if(newSwitchStatus== 1) {
		circleSwitch.setOptions({strokeColor:'#0B6121',fillColor: '#0B6121'});
		var newContent=content.replace("<b>Switch Status: </b>Off","<b>Switch Status: </b>On").replace("Switch On","Switch Off");
		//alert("newSwitchStatus "+newSwitchStatus+" "+newContent);
		infowindowHolonObject.setContent(newContent);
		infowindowHolonObject.close();
	} else {
		circleSwitch.setOptions({strokeColor:'#FF0000', fillColor: '#FF0000'});
		var newContent=content.replace("<b>Switch Status: </b>On","<b>Switch Status: </b>Off").replace("Switch Off","Switch On");
		infowindowHolonObject.setContent(newContent);
		infowindowHolonObject.close();		
	}
	if(oldCoordinatorIds != undefined ){
		for(var i=0;i< oldCoordinatorIds.length-1; i++){
			var oldCoordinatorId= oldCoordinatorIds[i];
			removeIconFromMap(oldCoordinatorId);
		}
	}
	
	if (newCoordinatorIds != undefined){
		for(var i=0;i< newCoordinatorIds.length-1;i++){
			var newCoordinatorId= newCoordinatorIds[i];
			createIconOnMap(newCoordinatorId);
		}
	}
	infowindowHolonObject.open(map,circleSwitch);
	$('#togglePowerSwitch').click(function() {
		SwitchOnOff(circleSwitch,powerSwitchId,infowindowHolonObject);			
	});
}
