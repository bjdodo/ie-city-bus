angular
		.module('myApp')
		.controller(
				'TripsController',
				function($scope, httpwrappersvc, $interval, $filter,
						guiconfigurationsvc) {

					httpwrappersvc.resetSession(false);

					$scope.activeTrips = '';

					$scope.mapData = {
						view : null,
						pins : []
					};

					$scope
							.$on(
									'vehicle.mapPinSelected',
									function(event, data) {
										for (idx = 0; idx < $scope.selectedActiveTrips.length; ++idx) {
											if ($scope.selectedActiveTrips[idx].vehicleId === data.vehicleId) {
												tableRow = document
														.getElementById('vehicles_row_'
																+ data.vehicleId);
												if (tableRow != null) {
													if (data.selected) {
														tableRow.className = 'pinselected';
													} else {
														tableRow.className = '';
													}
												}
												break;
											}
										}
									});
					$scope.tablePinSelected = function(vehicleId) {
						$scope.$broadcast('vehicle.tablePinSelected', {
							vehicleId : vehicleId
						});
					}
					$scope.updateData = function() {

						guiconfigurationsvc
								.get(function(config) {

									// update active trips
									var promise = httpwrappersvc
											.httpGet('api/activetrip');
									promise
											.then(function(response) {
												$scope.activeTrips = response.data;
												$scope.routes = config.monitoredRoutes;
												$scope.selectedPin = null;
												if ($scope.mapData == null
														|| $scope.mapData.view == null) {
													$scope.mapData = {
														view : {
															latitude : config.guiOverviewMapLatitude,
															longitude : config.guiOverviewMapLongitude,
															zoom : config.guiOverviewMapZoom
														}
													};
												}

												$scope
														.calculateSelectedActiveTrips();
												$scope.refreshMap();
											});

								});

					};

					$scope.calculateSelectedActiveTrips = function() {
						$scope.selectedRoutes = [];
						for (idx = 0; idx < $scope.routes.length; ++idx) {
							if ($scope.routes[idx].onmap) {
								$scope.selectedRoutes
										.push($scope.routes[idx].shortName);
							}
						}
						$scope.selectedActiveTrips = [];

						for (idx = 0; idx < $scope.activeTrips.length; ++idx) {

							if ($scope.selectedRoutes != null
									&& $scope.selectedRoutes.length !== 0
									&& $scope.selectedRoutes
											.indexOf($scope.activeTrips[idx].routeShortName) < 0) {
								continue;
							}
							$scope.selectedActiveTrips
									.push($scope.activeTrips[idx]);
						}

					}

					$scope.onRouteSelectionChanged = function() {
						$scope.calculateSelectedActiveTrips();
						$scope.refreshMap();
					}

					$scope.refreshMap = function() {

						var pinsArr = [];
						for (idx = 0; idx < $scope.selectedActiveTrips.length; ++idx) {

							var latlong = utils
									.splitLatlong($scope.selectedActiveTrips[idx].vehicleLatLong);
							pinsArr
									.push({
										latitude : latlong.latitude,
										longitude : latlong.longitude,
										description : '<div class="mappinpopup"><b>'
												+ $scope.selectedActiveTrips[idx].routeShortName
												+ '</b><br/>'
												+ 'trip start at '
												+ $filter('date')
														(
																new Date(
																		$scope.selectedActiveTrips[idx].scheduledStart * 1000),
																'HH:mm')
												+ '<br/>'
												+ 'from '
												+ $scope.selectedActiveTrips[idx].originStopName
												+ '<br/>'
												+ 'to '
												+ $scope.selectedActiveTrips[idx].destinationStopName
												+ "</div>",
										vehicleId : $scope.selectedActiveTrips[idx].vehicleId,
										pngFile : $scope.selectedActiveTrips[idx].tripDirection === 1 ? 'img/bus.png' : 'img/bus2.png' 
									});
						}

						$scope.mapData.pins = pinsArr;
					}

					// This is where the polling is set up
					$scope.updateData();
					$scope.cancelUpdateData = $interval($scope.updateData,
							30 * 1000);

					// When the user navigates away we'll cancel the polling
					$scope.$on('$destroy', function iVeBeenDismissed() {
						$interval.cancel($scope.cancelUpdateData);
					});

				});