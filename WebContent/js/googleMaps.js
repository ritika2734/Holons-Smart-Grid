$(document)
		.ready(
				function() {
					// call initialize function
					initialize();
					window.loadHolon=true;
					window.clickedMarkerChangeImage="";
					window.createdHolonObject=null; //Save object on overlaycomplete Action to use it later to set its holon color
					window.createdPowerLineObject=null;// Power Line object which has just been created
					window.createdSubLineObject=null; // Subline object which has just been created
					window.createdPowerSourceObject=null; //Power Source Object which has just been created
					window.createdDisasterCircle=null; // The Disaster circle which has just been created 
					window.showHolons=false; //Doen't let holons load more than once without clearing the map
					window.drawPowerLineMode=false; // Draw Power Line mode indicator 
					window.drawHolonObjectMode=false; // Draw Holon Object mode indicator
					window.calculateDistanceMode=false; // Calculate Distance mode indicator
					window.addSwitchonPowerLineMode=false; //Add power Switch mode indicator
					window.connectHolonObjectToPowerLineMode=false;//Connect Holon to Powerline mode indicator
					window.areConnectedMode=false;// Are Connected? functionality mode indicator
					window.addPowerSourceMode=false;// Add Power Source mode indicator
					window.addPowerSourceToLineMode=false; //Add Power Source to line mode indicator
					window.disasterMode=false; // Disaster mode indicator
					window.deleteSelectedDisasterMode=false; //Delete selected disaster mode indicator
					window.deleteAllDisasterMode=false; // Delete all disaster mode indicator
					window.globalHoList=new Map(); //Holon Object global map
					window.globalPlList=new Map(); //Power Line object global map
					window.globalPsList=new Map(); //Power Switch object global map
					window.globalHKList=new Map(); //Holon Coordinator object global map
					window.globalPCList=new Map(); //Power Circle object global map 
					window.globalPSrcList=new Map(); //Power Source object global map 
					window.globalDisasterList= new Map(); //Disaster List object global map
					window.currentInfoWindowObject=null; // current open holon object info window object
					window.currentLineInfoWindowObject=null; // current open line infowindow object
					window.currentSwitchInfoWindow=null; // current open power switch info window object
					window.currentPsInfoWindowObject=null; // current open power source info window object
					window.ajaxReqStatus=false;
					$("#addMasterHolonElementTypeDetail").hide();
					$("#addMasterHolonElementStateDetail").hide();
					$("#addMasterHolonDetail").hide();
					$("#holonObjectTypeforMasterTables").click(function(){
						getHolonObjectTypesFromDatabase();
						});
					
					$("#addMasterHolonObjDetail").hide(); 
					$("#masterTableHolonObjectsTypes").hide();
					
					$("#holonforMasterTables").click(function(){
						openHolonforMasterTables();
					});
					$("#dataFactoryGenerator").click(function(){
					    var url = $(this).attr('href'); 
					    window.open(url, '_blank');
					});
					$("#htcDocumentation").click(function(){
					    var url = $(this).attr('href'); 
					    window.open(url, '_blank');
					});
					$("#startSimulation").click(function(){
					    var url = $(this).attr('href'); 
					    window.open(url, '_blank');
					});
					$("#buttonHolonMaster").click(function(){
						var textHolonMaster= $("#textHolonMaster").val();
						var dataAttributes = {
								holonName:textHolonMaster
						}
						
						$("#addMasterHolonDetail").popup("close");
						ajaxRequest("addHolon", dataAttributes, addHolonCallBack, {});
					});
					
					$("#holonElementTypeforMasterTables").click(function(){
						openHolonElementTypeforMasterTables();
					
					});
					
					$("#holonElementStateforMasterTables").click(function(){
						openHolonElementStateforMasterTables();
					
					});
					
					
					$("#buttonHolonElementTypeMaster").click(function(){
						var textHolonElementTypeMaster= $("#textHolonElementTypeMaster").val();
						var dataAttributes= {
								holonElementTypeName : textHolonElementTypeMaster
						}
						
						$("#addMasterHolonElementTypeDetail").popup("close");
						ajaxRequest("createHolonElementType", dataAttributes, createHolonElementTypeCallBack, {});
					});
					
					$("#buttonHolonElementStateMaster").click(function(){
						var textHolonElementStateMaster= $("#textHolonElementStateMaster").val();
						var dataAttributes= {
								holonElementStateName : textHolonElementStateMaster
						}
						
						$("#addMasterHolonElementStateDetail").popup("close");
						ajaxRequest("createHolonElementState", dataAttributes, createHolonElementStateCallBack, {});
					});
					
									
					// Callback for add a holonName 
					function addHolonCallBack(data, options)
					{
						alert("Holon Name added succesfully");
					}
					
					// Callback for add a holonElemnt Type
					function createHolonElementTypeCallBack(data,options)
					{
						alert("Holon Element Type added succesfully");
					}
					
					function createHolonElementStateCallBack(data,options)
					{
						alert("Holon Element State added succesfully");
					}
					
					$("#clear").click(function() {
						swal({
							title: "Are you sure?",
							text: "Do you really want to clear the holon Map?",
							type: "warning",
							showCancelButton: true,
							confirmButtonColor: '#DD6B55',
							confirmButtonText: 'Yes,clear it!',
							cancelButtonText: "No, Don't clear!",
							closeOnConfirm: false,
							closeOnCancel: false
						},
						function(isConfirm){
					    if (isConfirm){
					    	initialize();
							loadHolon=true;
							swal("Cleared", "Map has been cleared", "info");
					    } else {
					      swal("Cancelled", "Map has not been cleared", "info");
					    }
						});
						
					});
					//function to check if a point falls in a circle overlay for snapping never remove
					google.maps.Circle.prototype.contains = function(latLng,theCircle) {
						  return theCircle.getBounds().contains(latLng) && google.maps.geometry.spherical.computeDistanceBetween(theCircle.getCenter(), latLng) <= theCircle.getRadius();
						}
					
				});

/**
 * First called when the page is loaded
 */
function initialize() {
	mapProp = {
		center : new google.maps.LatLng(mapCenterLatitude, mapCenterLongitude),
		zoom : 18,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
	directionsService = new google.maps.DirectionsService();
	directionsDisplay = new google.maps.DirectionsRenderer();
	directionsDisplay.setMap(map);

}

/**
 * gets a list of holon object types from db
 */
function getHolonObjectTypesFromDatabase() {
var types=["Fridge","Washing Machine"];
var data="";
for(var i=0;i<types.length;i++)
	{
		data=data+"<tr><td>"+types[i]+"</td><td><button id='editMasterHolonObjectType'>Edit</button></td><td><button id='deleteMasterHolonObjectType'>Delete</button></td></tr>"
	}
data="<table border='1'><th>Data Type</th><th></th><th></th>"+data+"</table>";
$("#masterTableHolonObjectsTypes").empty();
$("#masterTableHolonObjectsTypes").append("<a href='#' data-rel='back' data-role='button' data-theme='a' data-icon='delete' data-iconpos='notext' class='ui-btn-right'>X</a>"+data);
$("#masterTableHolonObjectsTypes").append("<br><button id='addMasterHolonObjectType' onclick='addHolonObjectTypeInMaster()'>Add</button>");
$("#masterTableHolonObjectsTypes").show();
$("#masterTableHolonObjectsTypes").popup();
$("#masterTableHolonObjectsTypes").popup("open");

}

function openHolonforMasterTables() {
	$("#addMasterHolonDetail").show();
	$("#addMasterHolonDetail").popup();
	$("#addMasterHolonDetail").popup("open");
}

function openHolonElementTypeforMasterTables() {
	$("#addMasterHolonElementTypeDetail").show();
	$("#addMasterHolonElementTypeDetail").popup();
	$("#addMasterHolonElementTypeDetail").popup("open");
}

function openHolonElementStateforMasterTables(){
	$("#addMasterHolonElementStateDetail").show();
	$("#addMasterHolonElementStateDetail").popup();
	$("#addMasterHolonElementStateDetail").popup("open");
}

/**
 * Get list of holon coordinators form database 
 * @param holonCoordinatorName current coordinator name
 * @param elementId Id of the current object
 * @param divId jsp div which has to be opened
 */
function getHolonCoordinatorFromDatabase(holonCoordinatorName,elementId,divId)
{
	var options={};
	if(holonCoordinatorName.trim().length==0 || holonCoordinatorName.trim()=='Not Part of any Holon')
	{
	options={
			elementId:elementId,
			divId:divId
			};
	}
	else if(typeof holonCoordinatorName != "undefined")
		{
		options={
				holonCoordinatorName:holonCoordinatorName,
				elementId:elementId,
				divId:divId
			};
		}
	
	
	ajaxRequest("getListHolonCoordinator", {}, getHolonCoordinatorFromDatabaseCallBack,options);
}

function getHolonCoordinatorFromDatabaseCallBack(data,option)
{
	var holonCoordinatorName="undefined";
	var elementId ="#".concat(option['elementId']);
	var holonCoordinatorNameFromOption =option['holonCoordinatorName'];
	var divId =option['divId'];
	if(typeof holonCoordinatorNameFromOption !="undefined")
	{
		holonCoordinatorName=holonCoordinatorNameFromOption.split("_")[0].trim();
	}
	var listHolonCoordinator= data.split("*");
	$(elementId).empty();
	$(elementId).append("<option value=\"0\" id= \"0\" >No Holon</option>");
	for(var i=0;i<listHolonCoordinator.length-1;i++)
	{
		//alert(listHolonCoordinator[i].split("-")[1].split(" ")[0].trim());
		if((holonCoordinatorName==listHolonCoordinator[i].split("-")[1].split(" ")[0].trim())){			
			var options= "<option value="+listHolonCoordinator[i].split("-")[0]+" id= "+listHolonCoordinator[i].split("-")[0]+" selected>"+listHolonCoordinator[i].split("-")[1]+"</option>";
			$(elementId).append(options);
			}
			else
			{				
				var options= "<option value="+listHolonCoordinator[i].split("-")[0]+" id= "+listHolonCoordinator[i].split("-")[0]+">"+listHolonCoordinator[i].split("-")[1]+"</option>";
				$(elementId).append(options);
				}
		
	}
	openDiv(divId);
}

/**
 * List of holon object types from db
 * 
 * @param holonObjectTypeName current holon object type
 */
function getHolonObjectTypeFromDatabase(holonObjectTypeName)
{
	var options={};
	if(typeof holonObjectTypeName != "undefined")
		{
		options={holonObjectTypeName:holonObjectTypeName};
		}
	
	ajaxRequest("getListHolonObjectType", {}, getHolonObjectTypeFromDatabaseCallBack, options);
}

function getHolonObjectTypeFromDatabaseCallBack(data,option)
{
	var holonObjectTypeName =option['holonObjectTypeName'];
	var listHolonObjectType= data.split("*");
	$("#holonObjectType").empty();
	for(var i=0;i<listHolonObjectType.length-1;i++)
	{
		if((typeof holonObjectTypeName != "undefined")&&(holonObjectTypeName==listHolonObjectType[i].split("-")[1].trim())){
			var options= "<option value="+listHolonObjectType[i].split("-")[0]+" id= "+listHolonObjectType[i].split("-")[0]+" selected>"+listHolonObjectType[i].split("-")[1]+"</option>";
			$("#holonObjectType").append(options);
			}
			else
			{
				var options= "<option value="+listHolonObjectType[i].split("-")[0]+" id= "+listHolonObjectType[i].split("-")[0]+">"+listHolonObjectType[i].split("-")[1]+"</option>";
				$("#holonObjectType").append(options);
				}
		
	}
//	$("#holonObjectType").selectmenu('refresh', true);
}

