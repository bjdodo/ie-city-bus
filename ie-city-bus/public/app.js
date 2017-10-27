(function() {
	
	var app = angular.module("myApp", ["ngRoute"]);	

	app.config(function($routeProvider, $locationProvider) {
		  $locationProvider.hashPrefix('');
		  $routeProvider
		  .when("/", {
			 controller : "TripsController",
		     templateUrl : "templates/trips.html"
		  })
		  .when("/tripdetails/:tripId", {
			controller : "TripDetailsController",
		    templateUrl : "templates/tripdetails.html"
		  })
		});
	app.filter("pointToMapUrl", function() {
		  return function(pointdecl) {
			  if (pointdecl == null) {
				  return "";
			  }
			  
			  var latlong = pointdecl.substring(7,pointdecl.length-1).replace(' ', ',');
			  //return "http://www.openstreetmap.org/search?query=" + latlong + "#map=14/" + latlong + "&layers=T";
			  return "https://www.google.com/maps/?q=" + latlong;
		    };
		});
	
	app.directive('openStreetMap', function() {
	  return {
		    restrict: "E",
		    scope: {
		      model : '=',
		      containerDivId: '@'
		    },
		    link: function(scope, element, attrs, controller) {

		      // the parent node for OSM has to be a div
		      var osm_div = document.createElement("div");
		      osm_div.id = scope.containerDivId;
		      element.append(osm_div);

		      scope.osm = new OSMWrapper();
		      scope.osm.create(osm_div);
		      
		      scope.$watch("model.view", function () {
		          console.log("watch triggered for view");
		          scope.osm.setCenter(scope.model.view.latitude, scope.model.view.longitude, scope.model.view.zoom);
		        }, true);
		        
		        scope.$watch("model.pins", function () {
		          console.log("watch triggered for pins");
		          scope.osm.removeAllPins();
		          for (var idx=0;idx< scope.model.pins.length;++idx) {
		            scope.osm.addPin(scope.model.pins[idx].latitude, scope.model.pins[idx].longitude, scope.model.pins[idx].description, scope.model.pins[idx].pngFile);
		          }
		        }, true);
	      }
	    }
	  });
	
	app.controller('TripsController', function($scope, $http, $interval) {
		$scope.activeTrips = '';
	    
		$scope.updateData = function () {
	    	// update active trips
	    	var promise = $http.get('api/activetrip');
		    promise.then(function(response) {
		      $scope.activeTrips = response.data;
		    });
	    }
	    
	    $scope.getLatLongForPoint = function(pointdecl) {
	    	alert(pointdecl.substring(7,pointdecl.length-1).replace(' ', ','));
	    	return pointdecl.substring(7,pointdecl.length-1).replace(' ', ',');
	    }

	    $scope.getMapUrlForPoint = function(pointdecl) {
	    	var latlong = getLatLongForPoint(pointdecl);
	    	return "http://www.openstreetmap.org/search?query=" + latlong + "#map=15/" + latlong + "&layers=T";
	    }
	    
	    $scope.updateData();
	    $scope.cancelUpdateData = $interval($scope.updateData, 30*1000);
	    
	    $scope.$on('$destroy', function iVeBeenDismissed() {
	    	$interval.cancel($scope.cancelUpdateData);
	    });
	    
	  });
	
	app.controller('TripDetailsController', function($scope, $http, $interval, $routeParams) {
		$scope.tripId = $routeParams.tripId;
		$scope.tripPassage = '';
		
		$scope.mapData = {view : null, pins : []};
	    
		$scope.updateData = function() {
	    	var promiseTrip = $http.get('api/activetrip/' + $scope.tripId);
	    	promiseTrip.then(function(response) {
		      $scope.tripData = response.data.length == 1 ? response.data[0] : null;
		      if ($scope.tripData != null) {
		    	  var latlong = utils.splitLatlong($scope.tripData.vehicleLatLong);
		    	  $scope.mapData = { 
		    			  			view : {
		    		  					latitude: latlong.latitude,
		    		  					longitude: latlong.longitude,
		    		  					zoom: 12
		    		  				},
		    		  				pins : [{
		    		  					latitude: latlong.latitude,
		    		  					longitude: latlong.longitude,
		    		  					description: '<b>' + $scope.tripData.routeShortName + '</b><br/>' +
		    		  								'Destination: ' + $scope.tripData.destinationStopName,
		    		  					pngFile: 'img/bus.png'
		    		  				}]
		    	  };
		    	}
		    });
		    
	    	var promisePassages = $http.get('api/activetrip/' + $scope.tripId + '/passages');
	    	promisePassages.then(function(response) {
		      $scope.tripPassages = response.data;
		    });
	    }
	    
		$scope.updateData();
		$scope.cancelUpdateData = $interval($scope.updateData, 30*1000);
	    
	    $scope.$on('$destroy', function iVeBeenDismissed() {
	    	$interval.cancel($scope.cancelUpdateData);
	    });
	    
	    
	    
	  });

}());


