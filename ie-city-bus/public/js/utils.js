var utils = {
		
	splitLatlong : function(latlong) {
		if (!latlong.startsWith("POINT (") || !latlong.endsWith(")")) {
			throw "Wrong latlong " + latlong;
		}

		latlong = latlong.substring("POINT (".length, latlong.length- 2);

		var points = latlong.split(" ");
		if (points.length != 2) {
			throw "Wrong latlong (split) " + latlong;
		}
		return {
			  latitude : parseFloat(points[0]),
			  longitude : parseFloat(points[1])
		  };
	}
	
}
