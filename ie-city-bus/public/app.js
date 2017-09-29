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
			//  template : "<h1>aaa</h1>"
		  })
		});
	app.filter("pointToMapUrl", function() {
		  return function(pointdecl) {
		    	var latlong = pointdecl.substring(7,pointdecl.length-1).replace(' ', ',');
		    	return "http://www.openstreetmap.org/search?query=" + latlong + "#map=14/" + latlong + "&layers=T";
		    };
		});
	
//	app.controller('MainController', function($scope, $http, $interval) {
//	  });
	
	app.controller('TripsController', function($scope, $http, $interval) {
		$scope.activeTrips = '';
	    
	    function updateData() {
	    	// update active trips
	    	var promise = $http.get('api/activetrip');
		    promise.then(function(response) {
		      $scope.activeTrips = response.data;
		    });
	    }
	    updateData();
	    $interval(updateData, 30*1000);
	    
	    $scope.getLatLongForPoint = function(pointdecl) {
	    	alert(pointdecl.substring(7,pointdecl.length-1).replace(' ', ','));
	    	return pointdecl.substring(7,pointdecl.length-1).replace(' ', ',');
	    }

	    $scope.getMapUrlForPoint = function(pointdecl) {
	    	var latlong = getLatLongForPoint(pointdecl);
	    	return "http://www.openstreetmap.org/search?query=" + latlong + "#map=15/" + latlong + "&layers=T";
	    }
	    
	  });
	
	app.controller('TripDetailsController', function($scope, $http, $interval, $routeParams) {
		$scope.tripId = $routeParams.tripId;
		$scope.tripDetails = '';
	    
	    function updateData() {
	    	// update active trips
	    	var promise = $http.get('api/activetrip/' + $scope.tripId + '/details');
		    promise.then(function(response) {
		      $scope.tripDetails = response.data;
		    });
	    }
	    updateData();
	    $interval(updateData, 30*1000);
	  });

}());


