angular
		.module('myApp')
		.controller(
				'TripDetailsController',
				function($scope, httpwrappersvc, $interval, $routeParams) {

					httpwrappersvc.resetSession(false);

					$scope.tripId = $routeParams.tripId;
					$scope.tripPassage = '';

					$scope.mapData = {
						view : null,
						pins : []
					};

					$scope.updateData = function() {
						var promiseTrip = httpwrappersvc
								.httpGet('api/activetrip/' + $scope.tripId);
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

						var promisePassages = httpwrappersvc
								.httpGet('api/activetrip/' + $scope.tripId
										+ '/passages');
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