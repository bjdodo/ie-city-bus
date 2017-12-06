angular.module('myApp').controller(
		'ExpiredController',
		function($scope, httpwrappersvc, guiconfigurationsvc) {

			guiconfigurationsvc.getForced(function(oldconfig, newconfig) {
				if (oldconfig != null && oldconfig.buildVersion != null
						&& oldconfig.buildVersion !== newconfig.buildVersion) {
					location.reload(true);
				}
			});

			$scope.isLatestHrefKnown = function() {
				httpwrappersvc.getLatestHref().length > 0;
			}
			$scope.resetSession = function() {
				httpwrappersvc.resetSession(true);
			}

		});
