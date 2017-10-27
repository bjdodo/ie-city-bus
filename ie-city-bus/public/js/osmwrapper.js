function OSMWrapper() {
   
    var map = null;
    var vectorLayer = null;
    var epsg4326 = null;
    var projectTo = null;
   
    var osm_create = function(div_or_id) {
      //http://dev.openlayers.org/docs/files/OpenLayers/Map-js.html
        map = new OpenLayers.Map({div: div_or_id});
        map.addLayer(new OpenLayers.Layer.OSM());
       
        epsg4326 =  new OpenLayers.Projection("EPSG:4326"); //WGS 1984 projection
        projectTo = map.getProjectionObject(); //The map projection (Spherical Mercator)
      
        vectorLayer = new OpenLayers.Layer.Vector("Overlay");
        map.addLayer(vectorLayer);
       
        //Add a selector control to the vectorLayer with popup functions
        var controls = {
          selector: new OpenLayers.Control.SelectFeature(vectorLayer, { onSelect: createPopup, onUnselect: destroyPopup })
        };

        function createPopup(feature) {
          feature.popup = new OpenLayers.Popup.FramedCloud("pop",
              feature.geometry.getBounds().getCenterLonLat(),
              null,
              '<div class="markerContent">'+feature.attributes.description+'</div>',
              null,
              true,
              function() { controls['selector'].unselectAll(); }
          );
          //feature.popup.closeOnMove = true;
          map.addPopup(feature.popup);
        }

        function destroyPopup(feature) {
          feature.popup.destroy();
          feature.popup = null;
        }
       
        map.addControl(controls['selector']);
        controls['selector'].activate();
    }
   
    var osm_removeAllFeatures = function() {
        vectorLayer.removeAllFeatures();
    }
   
    var osm_addFeature = function(lat, lon, description, pngFile) {
        var feature = new OpenLayers.Feature.Vector(
            new OpenLayers.Geometry.Point( lon, lat ).transform(epsg4326, projectTo),
            {description:description} ,
            {externalGraphic: pngFile, graphicHeight: 25, graphicWidth: 21, graphicXOffset:-12, graphicYOffset:-25  }
        );   
       
        vectorLayer.addFeatures(feature);
    }
   
    var osm_setCenter = function (lat, lon, zoom) {
      var lonLat = new OpenLayers.LonLat(lon, lat).transform(epsg4326, projectTo);
             
        map.setCenter (lonLat, zoom);
    }
   
    return { 'create' : osm_create,
             'removeAllPins' : osm_removeAllFeatures,
             'addPin' : osm_addFeature,
             'setCenter' : osm_setCenter
        };
}