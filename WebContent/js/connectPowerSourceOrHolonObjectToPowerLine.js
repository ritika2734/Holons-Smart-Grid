/**
 * This javscript file contains code for connecting either Holon Object or Power Source to Power Line.
 */
var lineLocationForSubline="";
var lineIdForSubline="";
var hoLocationForSubline="";
var hoIdForSubline="";
var pSrcObjInd=false;
var hoObjInd=false;

$(document).ready(function() {
//Enable the connecting functionality of Holon Object to Power Line.
	$('#connectHolonObjectToPowerLine').click(function(evt) {
		if (connectHolonObjectToPowerLineMode==false){
			connectHolonObjectToPowerLineMode=true;
			$(this).css("background-color", "rgb(153,153,0)");
		} else {
			$(this).css("background-color", "rgb(26, 26, 26)");
			connectHolonObjectToPowerLineMode=false;
		}
	})


})

/**
 * This function draws a power Line between Holon Object or Power Source.
 * It captures the clicked object Type, it's id and its 'location
 * @param latLng position of the Power Source or Holon Object or Power Line clicked
 * @param objectId id of the Power Source or Holon Object or Power Line clicked
 * @param objectType Power Source or Holon Object or Power Line clicked
 */
function connectPowerSourceOrHolonObjectToPowerLine(latLng,objectId,objectType)
{
	if(objectType=="PowerLine" )
	{
		if(lineIdForSubline.toString().length==0)
		{
			lineIdForSubline=objectId;
			lineLocationForSubline=latLng;
		}
		else 
		{
			alert("line clicked already");
		}

	}
	else if(objectType=="HolonObject" || objectType=="PSObject")
	{
		if( objectType=="PSObject")
		{
			pSrcObjInd=true;
		}
		if( objectType=="HolonObject")
		{
			hoObjInd=true;
		}
		if(hoIdForSubline.toString().length==0) 
		{
			hoIdForSubline=objectId;
			hoLocationForSubline=latLng;
		}
		else 
		{
			alert("Holon Object/Power Source clicked already");

		}
	}
	if(hoIdForSubline.toString().length!=0 && lineIdForSubline.toString().length!=0)
	{
		var lineType="";
		if(pSrcObjInd)
			{
			lineType="POWERSUBLINE";
			}else
				{
				lineType="SUBLINE";
				}
		pSrcObjInd=false;
		hoObjInd=false;
		openDivForCapacity(lineLocationForSubline,hoLocationForSubline,hoIdForSubline,lineIdForSubline,lineType);
		// Draw line between the Holon Object/ Power source and Power Line.
		var line = new google.maps.Polyline({
			path: [
			       lineLocationForSubline, 
			       hoLocationForSubline
			       ],
			       strokeColor: "rgb(0, 0, 0)",
			       strokeOpacity: 2.0,
			       strokeWeight: 4.0,
			       map: map
		});
		createdPowerLineObject=line;
		lineLocationForSubline="";
		lineIdForSubline="";
		hoLocationForSubline="";
		hoIdForSubline="";

	}

}

/**
 * This function opens the form to enter the capacity of the drawn Power line.
 * @param lineLocationForSubline location of the line to draw the subline.
 * @param hoLocationForSubline location of the Holon/Power source to draw the subline.
 * @param hoIdForSubline id of the object.
 * @param lineIdForSubline id of line
 * @param lineType type of line
 */
function openDivForCapacity(lineLocationForSubline,hoLocationForSubline,hoIdForSubline,lineIdForSubline,lineType)
{
	$("#powerLineStartLat").text(hoLocationForSubline.lat());
	$("#powerLineStartLng").text(hoLocationForSubline.lng());
	$("#powerLineEndLat").text(lineLocationForSubline.lat());
	$("#powerLineEndLng").text(lineLocationForSubline.lng());
	$("#powerLineHolonObjectIdHidden").text(hoIdForSubline);
	$("#powerLineIdForSubLine").text(lineIdForSubline);
	$("#powerLineType").text(lineType);
	openDiv('lineObjectDetail');
}

