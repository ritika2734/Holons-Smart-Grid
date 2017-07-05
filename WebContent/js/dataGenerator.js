$(document).ready(function() {
		ajaxRequest("factoryListHolonObjectType", {},factoryListHolonObjectTypeCallBack, {});
		ajaxRequest("factoryListHolonElementType", {},factoryListHolonElementTypeCallBack, {});
	});
	
	function factoryListHolonObjectTypeCallBack(data, options) {
		var holonObjectList = data.split("*");
		var innerHtmlHolonObject = "";
		var holonObjectTypesIDs = "";
		var holonObjectTypesPriorities = "";
		for (var i=0; i<holonObjectList.length; i++) {
			var holonObjectTypeId = holonObjectList[i].split("~")[0];
			var holonObjectTypeName = holonObjectList[i].split("~")[1];
			var holonObjectTypePriority = holonObjectList[i].split("~")[2];
			
			var htmlId = "holonObjectType_"+holonObjectTypeId;
			if(i == holonObjectList.length-1) {
				holonObjectTypesIDs += htmlId;
				holonObjectTypesPriorities += holonObjectTypePriority;
			} else {
				holonObjectTypesIDs += htmlId+"~";
				holonObjectTypesPriorities += holonObjectTypePriority+"~";
			}
			innerHtmlHolonObject +=	"<tr><td>"+holonObjectTypeName+" (Priority = "+holonObjectTypePriority+"):</td><td>"+
			"<input type=\"text\" id=\""+htmlId+"\" /></td></tr>";
		}
		$("#holonObjectTypesIDs").val(holonObjectTypesIDs);
		$("#holonObjectTypesPriorities").val(holonObjectTypesPriorities);
		
		$("#totalHolonObjectTypes").val(holonObjectList.length);
		$("#holonObjectTypeListFactory").html(innerHtmlHolonObject);
	}

	function factoryListHolonElementTypeCallBack(data, options) {
		var holonElementTypeListFactoryHtml = "<tr><td colspan=\"2\"><b><u>Holon Element Types</u></b></td></tr>";
		var holonElementTypeList = data.split("~~");
		for(var i=0; i<holonElementTypeList.length;i++) {
			holonElementTypeListFactoryHtml += "<tr><td colspan=\"2\">"+holonElementTypeList[i]+"</td></tr>";			
		}
		$("#holonElementTypeListFactory").html(holonElementTypeListFactoryHtml);
	}

	function factoryDataGenerator() {
		var holonObjectTypesPriorities = $("#holonObjectTypesPriorities").val();
		var totalHolonObjectTypes = $("#totalHolonObjectTypes").val();
		var htmlIdHolonObjectTypes = "";
		var htmlValuesHolonObjectTypes = "";
		var holonObjectTypesIds = $("#holonObjectTypesIDs").val().split("~");
		for(var i=0; i<totalHolonObjectTypes; i++){
			if(i == totalHolonObjectTypes-1){
				htmlValuesHolonObjectTypes += $("#"+holonObjectTypesIds[i]).val();
				htmlIdHolonObjectTypes += holonObjectTypesIds[i];
			} else {
				htmlValuesHolonObjectTypes += $("#"+holonObjectTypesIds[i]).val()+"~~";
				htmlIdHolonObjectTypes += holonObjectTypesIds[i]+"~~";
			}
		}
		var dataAttributes = {
			totalHolonObjectTypes : totalHolonObjectTypes,
			htmlIdHolonObjectTypes : htmlIdHolonObjectTypes,
			htmlValuesHolonObjectTypes : htmlValuesHolonObjectTypes,
			holonObjectTypesPriorities : holonObjectTypesPriorities
		};
		ajaxRequest("factoryDataGenerator", dataAttributes,
				factoryDataGeneratorCallBack, {})
	}

	function factoryDataGeneratorCallBack(data, options) {
		swal("Server Response!",data,"info");
	}