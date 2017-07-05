/**
 * This javascript file is used to reset the database.
 */

/**
 * This method is used to reset the database. An ajax request to resetDatabase is made.
 * 
 */

$(document).ready(function(){
	$("#resetDatabase").click(function(event) {
		resetDatabase();
	});
})
function resetDatabase() {
	swal({
		title: "Proceed with resetting entire database?",
		text: "This action will reset the entire database. Do you want to continue?",
		type: "warning",
		showCancelButton: true,
		confirmButtonColor: '#DD6B55',
		confirmButtonText: 'Yes, reset database!',
		cancelButtonText: "No, don't do anything!",
		closeOnConfirm: true,
		closeOnCancel: false
	},
	function(isConfirm) {
	    if (isConfirm) {
	    	ajaxRequest("resetDatabase", {}, resetDatabaseCallBack, {});	
		} else {
			swal("Cancelled", "Reset Database operation has been cancelled.", "info");
	    }
	});
}

/**
 * @callback resetDatabaseCallBack.
 * This is the callback function of the resetDatabase function.
 * @param data data from database if true the database is reset else it is cleaned or Reset error
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function resetDatabaseCallBack(data, options) {
	if(data == "true") {
    	initialize(); // Function in googleMaps.js
		loadHolon = true; // Global variable
		swal("Database Reset!", "Database has been successfully reset to initial state and map has been cleared.", "info");
	} else if(data == "resetAlreadyCompleted") {
		swal("Database Already Cleaned!", "Database has already been successfully reset to initial state.", "info");
	} else {
		swal("Database Reset Error!", data, "info");
	}
}