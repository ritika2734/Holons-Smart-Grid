/**
 * This method is used by all methods to call their respective AJAX actions defined in struts.xml file.
 * @param actionName The name of the action that needds to be called.
 * @param dataAttributes The data attributes that needs to be sent to the concerned action.
 * @param callBack The method that will be called once AJAX request is complete.
 * @param options The options JSON which is used by call back method. Any relevant information can be sent in this.
 */
function ajaxRequest(actionName, dataAttributes, callBack, options) {
	$.ajax({
		  url: actionName,
		  type: "POST",
		  cache: false,
		  data:dataAttributes,
		  dataType:"text",
		  context: document.body,
		  beforeSend:function (){
			  $("#spinner").show();
		  },
		  complete:function (){
			  $("#spinner").hide();
		  },
		  error: function(){
			  swal("Server Error!", "There is a problem with AJAX request. " +
			  		"Please check tomcat server status and database connection.", "info");
			  $("#spinner").hide();
		  },
		  success: function(data) {
			  $("#spinner").hide();
		  	  callBack(data,options);	
		  }
	});	
}