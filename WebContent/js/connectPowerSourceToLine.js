/**
 * This javascript file is used to connect Power source to Line.
 */
$(document).ready(function() {
	
	// Click event to enable and disable the mode to Connect Power Source to Main Power Line
	$('#connectPowerSource').click(function(evt) {
		if (addPowerSourceToLineMode==false){
			addPowerSourceToLineMode=true;
			$(this).css("background-color", "rgb(153,153,0)");
		} else {
			$(this).css("background-color", "rgb(26, 26, 26)");
			addPowerSourceToLineMode=false;
		}
	});
	
});

