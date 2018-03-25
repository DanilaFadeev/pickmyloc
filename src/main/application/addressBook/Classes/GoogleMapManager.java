package addressBook.Classes;

import addressBook.Main;
import addressBook.models.Contact;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.*;

public class GoogleMapManager {
    private static final double initLatitude = 53.902174;
    private static final double initLongitude = 27.5614256;
    private static final int initZoom = 11;

    public static GoogleMap map;

    public static void configureMap(GoogleMapView googleMapView) {
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(initLatitude, initLongitude))
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(initZoom);

        map = googleMapView.createMap(mapOptions, false);

        for (Contact c: Main.data ) {
            LatLong contactCoords = getCoordsByAddress(c.address.getValue());
            c.setLocation(contactCoords);

            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.position( contactCoords );

            map.addMarker( new Marker(markerOptions1) );
        }
    }

    public static LatLong getCoordsByAddress(String address) {
        final Geocoder geocoder = new Geocoder();

        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder()
                .setAddress(address)
                .setLanguage("ru")
                .getGeocoderRequest();

        GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
        GeocoderGeometry geometryResult = geocoderResponse.getResults().get(0).getGeometry();

        double latitude = geometryResult.getLocation().getLat().doubleValue();
        double longitude = geometryResult.getLocation().getLng().doubleValue();

        return new LatLong(latitude, longitude);
    }

    public static void setMarker(LatLong coords) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coords);

        map.clearMarkers();
        map.addMarker( new Marker(markerOptions) );
    }
}
