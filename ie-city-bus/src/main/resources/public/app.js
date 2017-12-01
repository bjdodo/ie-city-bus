(function() {

	var app = angular.module("myApp", [ "ngRoute" ]);

	app.config(function($routeProvider, $locationProvider) {
		$locationProvider.hashPrefix('');
		$routeProvider.when("/", {
			controller : "TripsController",
			templateUrl : "templates/trips.html"
		}).when("/tripdetails/:tripId", {
			controller : "TripDetailsController",
			templateUrl : "templates/tripdetails.html"
		})
	});
	app.filter("pointToMapUrl", function() {
		return function(pointdecl) {
			if (pointdecl == null) {
				return "";
			}

			var latlong = pointdecl.substring(7, pointdecl.length - 1).replace(
					' ', ',');
			// return "http://www.openstreetmap.org/search?query=" + latlong +
			// "#map=14/" + latlong + "&layers=T";
			return "https://www.google.com/maps/?q=" + latlong;
		};
	});

	app.directive('openStreetMap', function() {
		return {
			restrict : "E",
			scope : {
				model : '=',
				containerDivId : '@',
				emitOnPinSelected : '@',
				listenOnPinSelected : '@' 
			},
			link : function(scope, element, attrs, controller) {

				// the parent node for OSM has to be a div
				var osm_div = document.createElement("div");
				osm_div.id = scope.containerDivId;
				element.append(osm_div);

				scope.osm = new OSMWrapper();
				scope.osm.create(osm_div);

				scope.$watch("model.view", function() {
					console.log("watch triggered for view");
					scope.osm.setCenter(scope.model.view.latitude,
							scope.model.view.longitude, scope.model.view.zoom);
				}, true);

				scope.$watch("model.pins", function() {
					console.log("watch triggered for pins");
					scope.osm.removeAllPins();
					for (var idx = 0; idx < scope.model.pins.length; ++idx) {
						scope.osm.addPin(scope.model.pins[idx].vehicleId,
								scope.model.pins[idx].latitude,
								scope.model.pins[idx].longitude,
								scope.model.pins[idx].description,
								scope.model.pins[idx].pngFile, function(
										vehicleId, selected) {
									if (scope.emitOnPinSelected != null) {
										scope.$emit(scope.emitOnPinSelected, {
											vehicleId : vehicleId,
											selected : selected
										});
									}
								});
					}
				}, true);
				
				scope.$on(scope.listenOnPinSelected,
						function(event, data) {
					scope.osm.selectPin(data.vehicleId);
				});

			}
		}
	});

	app.service('guiconfiguration', function($http) {

		// This is a singleton that loads once when initialized and never
		// refreshes

		var config = null;
		var getAll = function(callback) {
			if (config == null) {
				var promise = $http.get('api/guiconfiguration');
				promise.then(function(response) {
					config = response.data;
					callback(config);
				});
			} else {
				callback(config);
			}
		};

		return {
			get : getAll
		};
	});

	app
			.controller(
					'TripsController',
					function($scope, $http, $interval, $filter,
							guiconfiguration) {
						$scope.activeTrips = '';

						$scope.mapData = {
							view : null,
							pins : []
						};

						// $scope.mapPinSelected = function(vehicleId, selected)
						// {
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
							$scope.$broadcast('vehicle.tablePinSelected', { vehicleId: vehicleId });
						}
						$scope.updateData = function() {

							guiconfiguration
									.get(function(config) {

										// update active trips
										var promise = $http
												.get('api/activetrip');
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
											pngFile : 'img/bus.png'
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

	app
			.controller(
					'TripDetailsController',
					function($scope, $http, $interval, $routeParams) {
						$scope.tripId = $routeParams.tripId;
						$scope.tripPassage = '';

						$scope.mapData = {
							view : null,
							pins : []
						};

						$scope.updateData = function() {
							var promiseTrip = $http.get('api/activetrip/'
									+ $scope.tripId);
							promiseTrip
									.then(function(response) {
										$scope.tripData = response.data.length == 1 ? response.data[0]
												: null;
										if ($scope.tripData != null) {
											var latlong = utils
													.splitLatlong($scope.tripData.vehicleLatLong);
											if ($scope.mapData == null
													|| $scope.mapData.view == null) {
												$scope.mapData = {
													view : {
														latitude : latlong.latitude,
														longitude : latlong.longitude,
														zoom : 12
													}
												};
											}
											$scope.mapData.pins = [ {
												latitude : latlong.latitude,
												longitude : latlong.longitude,
												description : '<div class="mappinpopup"><b>'
														+ $scope.tripData.routeShortName
														+ '</b><br/>'
														+ 'Destination: '
														+ $scope.tripData.destinationStopName
														+ '</div>',
												pngFile : 'img/bus.png'
											} ];

										}
										if (($scope.tripData == null || $scope.tripData.routeShortName == null)
												&& $scope.cancelUpdateData != null) {
											$interval
													.cancel($scope.cancelUpdateData);
										}
									});

							var promisePassages = $http.get('api/activetrip/'
									+ $scope.tripId + '/passages');
							promisePassages.then(function(response) {
								$scope.tripPassages = response.data;
							});
						}

						$scope.updateData();
						$scope.cancelUpdateData = $interval($scope.updateData,
								30 * 1000);

						$scope.$on('$destroy', function iVeBeenDismissed() {
							$interval.cancel($scope.cancelUpdateData);
						});

					});

}());
