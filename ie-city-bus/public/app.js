(function() {
	
	var app = angular.module("myApp", ["ngRoute"]);	

	app.config(function($routeProvider, $locationProvider) {
		  $locationProvider.hashPrefix('');
		  $routeProvider
		  .when("/", {
			 controller : "TripController",
		     templateUrl : "templates/trips.html"
		  })
		  .when("/tripdetails", {
			controller : "TripDetailsController",
		    templateUrl : "templates/tripdetails.html"
			//  template : "<h1>aaa</h1>"
		  })
		});
	
//	app.controller('MainController', function($scope, $http, $interval) {
//	  });
	
	app.controller('TripController', function($scope, $http, $interval) {
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
	  });

}());


