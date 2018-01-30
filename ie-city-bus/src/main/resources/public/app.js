(function() {

	var app = angular.module("myApp", [ "ngRoute" ]);

	app.config(function($routeProvider, $locationProvider) {
		$locationProvider.hashPrefix('');
		$routeProvider.when("/", {
			controller : "WelcomeController",
			templateUrl : "templates/welcome.html"
		}).when("/trips/", {
			controller : "TripsController",
			templateUrl : "templates/trips.html"
		}).when("/tripdetails/:tripId", {
			controller : "TripDetailsController",
			templateUrl : "templates/tripdetails.html"
		}).when("/triptraffic/:tripId", {
			controller : "TripTrafficController",
			templateUrl : "templates/triptraffic.html"
		}).when("/expired/", {
			controller : "ExpiredController",
			templateUrl : "templates/expired.html"
		}).otherwise({
			redirectTo : '/'
		});

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
					if (scope.model.view != null) {
						scope.osm.setCenter(scope.model.view.latitude,
								scope.model.view.longitude,
								scope.model.view.zoom);
					}
				}, true);

				// scope.$watch("model.pins", function() {
				scope.$watchCollection("model.pins", function() {
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
						console.log("pin added for " + scope.model.pins[idx].pngFile);
					}
				});

				scope.$on(scope.listenOnPinSelected, function(event, data) {
					scope.osm.selectPin(data.vehicleId);
				});

			}
		}
	});

	app.directive('copyRightTexts', function() {
		return {
			restrict : "E",
			templateUrl : 'templates/copyRightTexts.html'
		}
	});

	app
			.service(
					'httpwrappersvc',
					function($http, $window, guiconfigurationsvc) {

						var createTime = Date.now();
						var lastHref = '';
						var configuredSessionExpiryMins = null;

						var sessionExpired = function() {

							guiconfigurationsvc
									.get(function(config) {
										configuredSessionExpiryMins = config.guiSessionExpiryMins;
									});

							if (configuredSessionExpiryMins == null) {
								return Date.now() - createTime > 1000 * 60 * 30;
							} else {
								return Date.now() - createTime > 1000 * 60 * configuredSessionExpiryMins;
							}
						}

						var get = function(url) {
							if (sessionExpired()) {
								if ($window.location.href
										.indexOf('/#/expired/') === -1) {
									lastHref = $window.location.href;
									console
											.log('httpwrappersvc: lastHref saved: '
													+ lastHref);
								}
								$window.location.href = '/#/expired/';
							}
							return $http.get(url);
						}

						var resetSession = function(restoreHref) {
							createTime = Date.now();
							if (restoreHref) {
								if (lastHref.length > 0) {
									$window.location.href = lastHref;
									console
											.log('httpwrappersvc: lastHref restored: '
													+ lastHref);
								} else {
									$window.location.href = '/#';
								}
							}
						}

						var getLatestHref = function() {
							return lastHref;
						}

						return {
							httpGet : get,
							resetSession : resetSession,
							getLatestHref : getLatestHref
						};
					});

	app.service('guiconfigurationsvc', function($http) {

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

		var getForced = function(callback) {
			var promise = $http.get('api/guiconfiguration');
			promise.then(function(response) {
				callback(config, response.data);
				config = response.data;
			});
		}

		return {
			get : getAll,
			getForced : getForced
		};
	});

	app.controller('IndexController', function($scope, guiconfigurationsvc) {
		guiconfigurationsvc.get(function(config) {
			$scope.appVersion = config.buildVersion;
		});
	});
}());
