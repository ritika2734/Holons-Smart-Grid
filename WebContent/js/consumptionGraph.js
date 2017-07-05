/**
 * This javascript file is used for the consumption graph for both Holon Objects & Holon Elements.
 * Highcharts.js is used for displaying the graph.
 */


/** 
 * This method get the consumption of Holon Object or Holon Element.
 * The current consumption of Holon Element is fetched directly and showGraphForHolonTypes is called to display graph.
 * The current total consumption of Holon Object is fetched from database using showHolonObjectConsumption callback
 * @param holonElementId id of the Holon Element
 * @param holonObjectId id of the Holon Object
 * @param holonType current Consumption incase of Holon Element & "ho" in case of Holon Object
 */
function showConsumptionGraph(holonElementId,holonObjectId,holonType)
{
	if(holonType=="ho"){
		var dataAttributes= {
				holonObjectId : holonObjectId
		}
		ajaxRequest("showHolonObjectConsumption", dataAttributes, getDataForConsumptionGraphCallBack, dataAttributes);
	}else{
		var currrentConsumption=holonType;
		showGraphForHolonTypes(holonElementId,parseInt(currrentConsumption),"Holon Element");
	}	
}

/**
 * @callback getDataForConsumptionGraphCallBack
 * This method get sum of Consumption for all Holon Elements of the Holon Object 
 * i.e. Total Consumption of the Holon Object and calls the showGraphForHolonTypes method to show the graph
 * @param data data from the database having Holon Object Id & current Consumption of Holon Object
 * @param options This is used by the callback method. Any relevant information can be sent in this.
 */
function getDataForConsumptionGraphCallBack(data,options)
{
	var graphForConsumption;
	var holonObjectId=data.split("*")[1];
	var currentConsumption=parseInt(data.split("*")[0]);
	showGraphForHolonTypes(holonObjectId,currentConsumption,"Holon Object");
	
}

/**
 * This method shows the consumption graph of Holon Object /Holon Element 
 * @param id id of Holon Object/ Holon Element
 * @param currentConsumption value of the consumption by Holon Object/Holon Element
 * @param holonType HolonType states whether it is Holon Object/Holon Element. 
 */
function showGraphForHolonTypes(id,currentConsumption,holonType)
{
	if(currentConsumption==0 || id==undefined ){
		alert("No Data to show");
	}
	else{
		openDiv("consumptionGraphBody");
		
		Highcharts.setOptions({
	        global: {
	            useUTC: false
	        }
	    });
		var graphForConsumption= new Highcharts.Chart({
	        chart: {
	        	renderTo: "consumptionGraphBody",
	            type: 'spline',
	            animation: Highcharts.svg, // don't animate in old IE
	            //marginRight: 10,
	            events: {
	                load : function (event) {
	                	// set up the updating of the chart each second
	                    var series = this.series[0];
	                 intervalTimeConsumptionGraph = setInterval(function () {
	                        var x = (new Date()).getTime(), // current time
                            y = currentConsumption+ Math.floor((Math.random() * 10) + 1);
                        series.addPoint([x, y], true, true);
                    }, 1000);                    
	                }
	            }
	        },
	        credits:{
	        	enabled : false
	        },
	        title: {
	            text: 'Consumption Graph for ' +holonType+":"+ +id,
	            align: "left"
	        },
	        xAxis: {
	            type: 'datetime',
	            tickPixelInterval: 150
	        },
	        yAxis: {
	            title: {
	                text: 'Consumption'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            formatter: function () {
	                return '<b>' + this.series.name + '</b><br/>' +
	                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
	                    Highcharts.numberFormat(this.y, 2);
	            }
	        },
	        legend: {
	            enabled: false
	        },
	        exporting: {
	            enabled: false
	        },
	        series: [{
	            name: 'Element Consumption',
	            data: (function () {
	                // generate an array of random data
	                var data1 = [],
	                    time = (new Date()).getTime(),
	                    i;

	                for (i = -19; i <= 0; i += 1) {
	                    data1.push({
	                        x: time + i * 1000,
	                        y: currentConsumption + Math.floor((Math.random() * 10) + 1)
	                    });
	                }
	                return data1; 
	            }())
	        }],
	        lang: {
	            some_key: 'Close Chart'
	        },
	        exporting: {
	            buttons: {
	                contextButton: {
	                    enabled: false
	                },
	                closeButton: {
	                    text: 'Close Chart',
	                    _titleKey: 'some_key',
	                    onclick: function () {
	                    	closeDiv('consumptionGraphBody');
	                    	clearInterval(intervalTimeConsumptionGraph);
	                    }
	                }
	            }
	        }
	    });

	}
	  
	
}