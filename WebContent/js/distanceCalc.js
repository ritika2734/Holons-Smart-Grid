/**
 * This JS is used to calculate distance between two points
 */

/**
 * This JS file contains code related to drawing a power line on the map.
 * 
 * 
 * 
 */
$(document)
		.ready(
				function() {
					// Global Variables for distance calculation
					var dPointA;// =new google.maps.LatLng(49.863915, 8.555046);
					var dPointB;// =new google.maps.LatLng(49.861304, 8.554177);
					var dist;
					var distMarkers = [];
					var infowinDist;
					var distListener;
					var ro;
					// Global Variables for distance calculation

					$("#calcDistance")
							.on(
									'click',
									function() {

										// alert($(this).css("background-color"));
										if (calculateDistanceMode == false) {
											calculateDistanceMode = true;
											$(this).css("background-color",
													"rgb(153,153,0)");
											swal({
												title : "Select Points",
												text : "Please select two points on the Map and click on the button again to calculate the distance.",
												type : "info",
												confirmButtonText : "Sure!"
											});

											distListener = google.maps.event
													.addListener(
															map,
															'click',
															function(event) {

																if (dPointA == undefined
																		&& dPointB == undefined) {
																	// alert("AbhinavMark
																	// A
																	// "+event.latLng);
																	dPointA = event.latLng;
																	placeMarker(event.latLng);
																} else if (dPointA != undefined
																		&& dPointB == undefined) {
																	// alert("AbhinavMark
																	// B"+event.latLng);
																	dPointB = event.latLng;
																	placeMarker(event.latLng);

																} else {
																	swal({
																		title : "Points Already Selected!",
																		text : "Please click on the button to calculate the distance.",
																		type : "info",
																		confirmButtonText : "Sure!"
																	});

																}
															});

										} else {

											if (dPointA == undefined
													&& dPointB == undefined) {

												// alert("Please select two
												// points
												// on the Map to calculate the
												// distance.")
												swal({
													title : "Which are the Points?",
													text : "Please select two points on the Map before calculating the distance.",
													type : "error",
													confirmButtonText : "Sure!"
												});
											} else if ((dPointA != undefined && dPointB == undefined)
													|| (dPointA == undefined && dPointB != undefined)) {
												swal({
													title : "Which is the second point?",
													text : "Please select second point on the Map before calculating the distance.",
													type : "error",
													confirmButtonText : "Sure!"
												});
											} else {
												// alert("abhinav1");
												dist = google.maps.geometry.spherical
														.computeDistanceBetween(
																dPointA,
																dPointB);
												infowinDist = new google.maps.InfoWindow(
														{
															content : "The distance between selected points is "
																	+ dist
																	+ " meters."
														});
												var route = drawRoute(dPointA,
														dPointB, infowinDist,
														map);

												swal({
													title : "Distance",
													text : "The distance between selected points is "
															+ dist + " meters.",
													type : "success",
													confirmButtonText : "Nice!"
												});
												dPointA = undefined;
												dPointB = undefined;
												deleteMarkers();
												google.maps.event
														.removeListener(distListener);
												$(this).css("background-color",
														"rgb(26,26,26)");
												calculateDistanceMode = false;
											}

										}

									});

				})

/**
 *method to clear markers from the map 
 */
function deleteMarkers() {
	setAllMap(null);
	distMarkers = [];
}


/**
 * put all the markers from distMarkers array on the map 
 * @param map is the map on which markers has to be placed
 */
function setAllMap(map) {
	for (var i = 0; i < distMarkers.length; i++) {
		distMarkers[i].setMap(map);
	}
}

/**
 * create markers for the clicked position 
 * @param location is the location on the map where markers have to be placed
 */
function placeMarker(location) {
	// alert("AbhinavMark Place"+ location);
	var clickedLocation = new google.maps.LatLng(location);
	var marker = new google.maps.Marker({
		position : location,
		map : map
	});
	distMarkers.push(marker);
}


/**
 * draws the line on the map between selected locations
 * 
 * @param start start point of the line 
 * @param end end point of the line 
 * @param infowindow the infowindow to be showed for the line 
 * @param map map on which the line has to be drawn 
 */
function drawRoute(start, end, infowindow, map) {
	// alert("infowin "+infowindow+" "+"map "+map+" this
	// "+this)

	var request = {
		origin : start,
		destination : end,
		travelMode : google.maps.TravelMode.DRIVING,

	};
	ro = directionsService.route(request, function(response, status) {
		if (status == google.maps.DirectionsStatus.OK) {
			directionsDisplay.setDirections(response);
		}
		infowindow.setOptions({
			position : start
		});
		infowindow.open(map, map);

	});

}