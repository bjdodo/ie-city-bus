angular
		.module('myApp')
		.controller(
				'TripDetailsController',
				function($scope, httpwrappersvc, $interval, $routeParams) {

					httpwrappersvc.resetSession(false);

					$scope.tripId = $routeParams.tripId;
					$scope.stopsAddedOnMap = false;

					$scope.mapData = {
						view : null,
						pins : []
					};

					$scope.showMyMobile = false;

					$scope.showMyMobileChanged = function() {
						$scope.showMyMobile = !$scope.showMyMobile;
						if ($scope.showMyMobile) {
							$scope.updateMobilePosition();
						} else {
							$scope.removePointsOnMap('img/smartphone.png');
						}

					}
					$scope.updateMobilePosition = function() {

						if ($scope.showMyMobile) {
							if ("geolocation" in navigator) {
								navigator.geolocation
										.getCurrentPosition(
												function(position) {
													$scope
															.setSinglePointOnMap({
																latitude : position.coords.latitude,
																longitude : position.coords.longitude,
																description : '<div class="mappinpopup"><b>your device</b></div>',
																pngFile : 'img/smartphone.png'
															});

													$scope.$apply();
												}, function(error) {
													console.log(error.message);
												}, {
													enableHighAccuracy : true,
													timeout : 5000
												});
							} else {
								console.log("geolocation IS NOT available");
							}
						}
					}

					$scope.updateBusPosition = function() {
						if ($scope.tripData == null) {
							$scope.removePointsOnMap('img/bus.png');
						} else {
							var latlong = utils
									.splitLatlong($scope.tripData.vehicleLatLong);
							if (!!latlong) {
								$scope
										.setSinglePointOnMap({
											latitude : latlong.latitude,
											longitude : latlong.longitude,
											description : '<div class="mappinpopup"><b>'
													+ $scope.tripData.routeShortName
													+ '</b><br/>'
													+ 'Destination: '
													+ $scope.tripData.destinationStopName
													+ '</div>',
											pngFile : 'img/bus.png'
										});
							} else {
								console.log('bus latlong was null');
							}

						}
					}

					$scope.addBusStopPinsToMap = function() {

						if ($scope.tripPassages != null
								&& $scope.tripPassages.length > 0) {
							console.log('Adding bus stop pins '
									+ $scope.tripPassages.length);

							$scope.removePointsOnMap('img/bus_stop.png');

							for (idx = 0; idx < $scope.tripPassages.length; ++idx) {

								var latlong = utils
										.splitLatlong($scope.tripPassages[idx].stopLatLong);

								if (!!latlong) {
									$scope.mapData.pins
											.push({
												latitude : latlong.latitude,
												longitude : latlong.longitude,
												description : '<div class="mappinpopup"><b>'
														+ $scope.tripPassages[idx].stopName
														+ '</b><br/>'
														+ $scope.tripPassages[idx].stopNumber
														+ '</div>',
												pngFile : 'img/bus_stop.png'
											});
								} else {
									console
											.log('stop latlong was null '
													+ +$scope.tripPassages[idx].stopName);
								}
							}

							$scope.updateBusPosition();
						}
					}

					$scope.removePointsOnMap = function(pngFileName) {
						for (idx = 0; idx < $scope.mapData.pins.length; ++idx) {
							if ($scope.mapData.pins[idx].pngFile === pngFileName) {
								$scope.mapData.pins.splice(idx, 1);
							}
						}
					}

					$scope.setSinglePointOnMap = function(singlePoint) {

						if ($scope.mapData.pins == null) {
							$scope.mapData.pins = [];
						}

						for (idx = 0; idx < $scope.mapData.pins.length; ++idx) {
							if ($scope.mapData.pins[idx].pngFile === singlePoint.pngFile) {
								$scope.mapData.pins.splice(idx, 1);
								break;
							}
						}
						$scope.mapData.pins.push(singlePoint);
						console.log("Pins: pushed singlePoint "
								+ singlePoint.pngFile);
					}

					$scope.updateData = function() {

						var promiseTrip = httpwrappersvc
								.httpGet('api/activetrip/' + $scope.tripId);
						promiseTrip
								.then(function(response) {
									$scope.tripData = response.data.length == 1 ? response.data[0]
											: null;
									if ($scope.tripData != null) {
										if ($scope.mapData == null
												|| $scope.mapData.view == null) {
											var latlong = utils
													.splitLatlong($scope.tripData.vehicleLatLong);
											$scope.mapData = {
												view : {
													latitude : latlong.latitude,
													longitude : latlong.longitude,
													zoom : 12
												}
											};
										}
										$scope.updateBusPosition();
									}
									if (($scope.tripData == null || $scope.tripData.routeShortName == null)
											&& $scope.cancelUpdateData != null) {
										$interval
												.cancel($scope.cancelUpdateData);
									}
								});

						var promisePassages = httpwrappersvc
								.httpGet('api/trip/' + $scope.tripId
										+ '/passages');
						promisePassages.then(function(response) {
							if (response.data != null) {
								$scope.tripPassages = response.data;
							}

							if ($scope.tripPassages != null
									&& $scope.tripPassages.length > 0) {

								$scope.addBusStopPinsToMap();
							}
							// else {
							// console.log( 'Failed to add bus stops to map.
							// tripPassages: ' + $scope.tripPassages +
							// 'stopsAddedOnMap: ' + $scope.stopsAddedOnMap );
							// }

						});
					}

					$scope.updateMobilePosition();
					$scope.cancelUpdateMobilePosition = $interval(
							$scope.updateMobilePosition, 10 * 1000);

					$scope.updateData();
					$scope.cancelUpdateData = $interval($scope.updateData,
							30 * 1000);

					$scope.$on('$destroy', function iVeBeenDismissed() {
						$interval.cancel($scope.cancelUpdateData);
						$interval.cancel($scope.cancelUpdateMobilePosition);
					});

				});