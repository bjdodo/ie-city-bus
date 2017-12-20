angular
		.module('myApp')
		.controller(
				'TripTrafficController',
				function($scope, httpwrappersvc, $interval, $routeParams) {

					httpwrappersvc.resetSession(false);

					$scope.tripId = $routeParams.tripId;
					$scope.tripTraffic = null;
					$scope.tripTrafficFormatted = null;

					$scope.updateData = function() {
						var promiseTrip = httpwrappersvc.httpGet('api/trip/'
								+ $scope.tripId + "/traffic");
						promiseTrip
								.then(function(response) {
									$scope.tripTraffic = response.data;
									if ($scope.tripTraffic == null)
										return;

									$scope.tripTrafficFormatted = [];

									for (stopsIdx = 0; stopsIdx < $scope.tripTraffic.length; stopsIdx++) {
										var passageInfo = $scope.tripTraffic[stopsIdx];
										

										if (stopsIdx === 0) {
											$scope.tripTrafficFormatted
													.push({
														stopName : passageInfo.fromStopName,
														stopNumber : passageInfo.fromStopNumber,
														stopLatLong : passageInfo.fromStopLatLong
													});
										}

										// passages are ordered by time and we
										// are looking for the most recent ones
										
										var currentEntry = {
											stopName : passageInfo.toStopName,
											stopNumber : passageInfo.toStopNumber,
											stopLatLong : passageInfo.toStopLatLong
										};

										var cnt = 5;
										for (passageIdx = passageInfo.tripSectionPassages.length - 1; passageIdx >= 0; passageIdx--) {

											if (cnt < 1)
												break;

											currentEntry["tripId" + cnt] = passageInfo.tripSectionPassages[passageIdx].tripId;
											currentEntry["duration" + cnt] = passageInfo.tripSectionPassages[passageIdx].stop2Time
													- passageInfo.tripSectionPassages[passageIdx].stop1Time;
											currentEntry["starttime" + cnt] = passageInfo.tripSectionPassages[passageIdx].stop1Time;
											currentEntry["finishtime" + cnt] = passageInfo.tripSectionPassages[passageIdx].stop2Time;
											currentEntry["routeShortName" + cnt] = passageInfo.tripSectionPassages[passageIdx].routeShortName;

											cnt--;
										}

										$scope.tripTrafficFormatted
												.push(currentEntry);
									}

								});
					}

					$scope.updateData();
					$scope.cancelUpdateData = $interval($scope.updateData,
							30 * 1000);

					$scope.$on('$destroy', function iVeBeenDismissed() {
						$interval.cancel($scope.cancelUpdateData);
					});
				});