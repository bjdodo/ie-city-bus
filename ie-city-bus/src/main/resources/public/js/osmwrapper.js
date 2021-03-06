function OSMWrapper() {

	var map = null;
	var vectorLayer = null;
	var epsg4326 = null;
	var projectTo = null;
	var controls = null;

	var osm_create = function(div_or_id) {
		// http://dev.openlayers.org/docs/files/OpenLayers/Map-js.html
		map = new OpenLayers.Map({
			div : div_or_id
		});
		
		//map.addLayer(new OpenLayers.Layer.OSM());
		// For https we need to specify the tiles url
		// https://gis.stackexchange.com/a/90308
		map.addLayer(new OpenLayers.Layer.OSM(
		    "OpenStreetMap", 
		    // Official OSM tileset as protocol-independent URLs
		    [
		        '//a.tile.openstreetmap.org/${z}/${x}/${y}.png',
		        '//b.tile.openstreetmap.org/${z}/${x}/${y}.png',
		        '//c.tile.openstreetmap.org/${z}/${x}/${y}.png'
		    ], 
		    null));

		epsg4326 = new OpenLayers.Projection("EPSG:4326"); // WGS 1984
		// projection
		projectTo = map.getProjectionObject(); // The map projection (Spherical
		// Mercator)

		vectorLayer = new OpenLayers.Layer.Vector("Overlay");
		map.addLayer(vectorLayer);

		// Add a selector control to the vectorLayer with popup functions
		controls = {
			selector : new OpenLayers.Control.SelectFeature(vectorLayer, {
				onSelect : createPopup,
				onUnselect : destroyPopup
			})
		};

		function createPopup(feature) {
			feature.popup = new OpenLayers.Popup.FramedCloud("pop",
			// feature.popup = new OpenLayers.Popup("pop",
			feature.geometry.getBounds().getCenterLonLat(), null,
					'<div class="markerContent">'
							+ feature.attributes.description + '</div>', null,
					true, function() {
						controls['selector'].unselectAll();
					});
			feature.popup.autoSize = true;

			if (feature.attributes.onSelectCB != null) {
				feature.attributes.onSelectCB(feature.attributes.vehicleId,
						true);
			}
			// feature.popup.addCloseBox();
			// feature.popup.closeOnMove = true;
			map.addPopup(feature.popup);
		}

		function destroyPopup(feature) {
			if (feature.attributes.onSelectCB != null) {
				feature.attributes.onSelectCB(feature.attributes.vehicleId,
						false);
			}

			feature.popup.destroy();
			feature.popup = null;
		}

		map.addControl(controls['selector']);
		controls['selector'].activate();
	}

	var osm_removeAllFeatures = function() {
		controls['selector'].unselectAll();
		vectorLayer.removeAllFeatures();
	}

	var osm_addFeature = function(vehicleId, lat, lon, description, pngFile,
			onSelectCB) {
		var feature = new OpenLayers.Feature.Vector(
				new OpenLayers.Geometry.Point(lon, lat).transform(epsg4326,
						projectTo), {
					description : description,
					vehicleId : vehicleId,
					onSelectCB : onSelectCB
				}, {
					externalGraphic : pngFile,
					graphicHeight : 25,
					graphicWidth : 21,
					graphicXOffset : -12,
					graphicYOffset : -25
				});

		vectorLayer.addFeatures(feature);
	}

	var osm_selectFeature = function(vehicleId) {

		for (idx = 0; idx < vectorLayer.features.length; idx++) {
			var feature = vectorLayer.features[idx];
			if (feature.attributes.vehicleId === vehicleId) {
				var currentlySelected = 
					vectorLayer.selectedFeatures.length > 0 &&
					vectorLayer.selectedFeatures[0].attributes.vehicleId === vehicleId;
				controls.selector.unselectAll();
				if (!currentlySelected) {
					controls.selector.select(feature);
					osm_setCenter(feature.attributes.latitude,
							feature.attributes.longitude, null);
				}
				break;
			}
		}
	}

	var osm_setCenter = function(lat, lon, zoom) {

		var lonLat = new OpenLayers.LonLat(lon, lat).transform(epsg4326,
				projectTo);

		if (zoom != null) {
			map.setCenter(lonLat, zoom);
		} else {
			map.setCenter(lonLat);
		}
	}

	return {
		'create' : osm_create,
		'addPin' : osm_addFeature,
		'selectPin' : osm_selectFeature,
		'removeAllPins' : osm_removeAllFeatures,
		'setCenter' : osm_setCenter
	};
}
