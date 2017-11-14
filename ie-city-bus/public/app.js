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
				containerDivId : '@'
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
						scope.osm.addPin(scope.model.pins[idx].latitude,
								scope.model.pins[idx].longitude,
								scope.model.pins[idx].description,
								scope.model.pins[idx].pngFile);
					}
				}, true);
			}
		}
	});

	app.service('busroutes', function($http) {

		// This is a singleton that loads once when initialized and never
		// refreshes

		var routes = null;
		var getAll = function() {
			if (routes == null) {
				var promise = $http.get('api/route');
				promise.then(function(response) {
					routes = response.data;
					routes.sort(function(a, b) {
						return a.shortName > b.shortName;
					});
				});
				return [];
			}

			return routes;
		};

		// This is when the data gets initialized
		getAll();
		return {
			get : getAll
		};
	});

	app
			.controller(
					'TripsController',
					function($scope, $http, $interval, busroutes) {
						$scope.activeTrips = '';

						$scope.mapData = {
							view : null,
							pins : []
						};

						$scope.updateData = function() {
							// update active trips
							var promise = $http.get('api/activetrip');
							promise.then(function(response) {
								$scope.activeTrips = response.data;							
								$scope.routes = busroutes.get();
								$scope.calculateSelectedActiveTrips();
								$scope.refreshMap();
							});
						}

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
							if ($scope.mapData == null
									|| $scope.mapData.view == null) {
								$scope.mapData = {
									view : {
										latitude : 53.27452,
										longitude : -9.04784,
										zoom : 12
									}
								};
							}

							var pinsArr = [];
							for (idx = 0; idx < $scope.selectedActiveTrips.length; ++idx) {

								var latlong = utils
										.splitLatlong($scope.selectedActiveTrips[idx].vehicleLatLong);
								pinsArr
										.push({
											latitude : latlong.latitude,
											longitude : latlong.longitude,
											description : '<b>'
													+ $scope.selectedActiveTrips[idx].routeShortName
													+ '</b><br/>'
													+ 'Destination: '
													+ $scope.selectedActiveTrips[idx].destinationStopName,
											pngFile : 'img/bus.png'
										});
							}

							$scope.mapData.pins = pinsArr;
						}

						$scope.getLatLongForPoint = function(pointdecl) {
							alert(pointdecl.substring(7, pointdecl.length - 1)
									.replace(' ', ','));
							return pointdecl.substring(7, pointdecl.length - 1)
									.replace(' ', ',');
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

	app.controller('TripDetailsController', function($scope, $http, $interval,
			$routeParams) {
		$scope.tripId = $routeParams.tripId;
		$scope.tripPassage = '';

		$scope.mapData = {
			view : null,
			pins : []
		};

		$scope.updateData = function() {
			var promiseTrip = $http.get('api/activetrip/' + $scope.tripId);
			promiseTrip.then(function(response) {
				$scope.tripData = response.data.length == 1 ? response.data[0]
						: null;
				if ($scope.tripData != null) {
					var latlong = utils
							.splitLatlong($scope.tripData.vehicleLatLong);
					if ($scope.mapData == null || $scope.mapData.view == null) {
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
						description : '<b>' + $scope.tripData.routeShortName
								+ '</b><br/>' + 'Destination: '
								+ $scope.tripData.destinationStopName,
						pngFile : 'img/bus.png'
					} ];

				}
			});

			var promisePassages = $http.get('api/activetrip/' + $scope.tripId
					+ '/passages');
			promisePassages.then(function(response) {
				$scope.tripPassages = response.data;
			});
		}

		$scope.updateData();
		$scope.cancelUpdateData = $interval($scope.updateData, 30 * 1000);

		$scope.$on('$destroy', function iVeBeenDismissed() {
			$interval.cancel($scope.cancelUpdateData);
		});

	});

}());
