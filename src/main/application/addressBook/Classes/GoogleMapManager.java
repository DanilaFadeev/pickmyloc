package addressBook.Classes;

import addressBook.Main;
import addressBook.models.Contact;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.jfoenix.controls.JFXSpinner;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.event.MapStateEventType;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.collections.ObservableList;

import java.util.List;

public class GoogleMapManager {
    private static final double initLatitude = 53.902174;
    private static final double initLongitude = 27.5614256;
    private static final int initZoom = 11;
    private static final int locationZoom = 14;

    private static GoogleMap map;

    public static void configureMap(GoogleMapView googleMapView, JFXSpinner spinner) {
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(initLatitude, initLongitude))
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(initZoom);

        map = googleMapView.createMap(mapOptions, false);

        map.addStateEventHandler(MapStateEventType.tilesloaded, () -> spinner.setVisible(false));

        setAllMarkers(Main.data);
    }

    public static void setDefaultMapOptions() {
        map.setCenter(new LatLong(initLatitude, initLongitude));
        map.setZoom(initZoom);
    }

    public static LatLong getCoordsByAddress(String address) {
        final Geocoder geocoder = new Geocoder();

        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder()
                .setAddress(address)
                .setLanguage("ru")
                .getGeocoderRequest();

        GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
        List<GeocoderResult> geoResults = geocoderResponse.getResults();

        if (geoResults.size() == 0) {
            return null;
        }

        GeocoderGeometry geometryResult = geoResults.get(0).getGeometry();

        double latitude = geometryResult.getLocation().getLat().doubleValue();
        double longitude = geometryResult.getLocation().getLng().doubleValue();

        return new LatLong(latitude, longitude);
    }

    public static void setAllMarkers(ObservableList<Contact> contacts) { // need to be optimized
        for (Contact c: contacts) {
            LatLong contactCoords = getCoordsByAddress(c.address.getValue());
            c.setLocation(contactCoords);

            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.position( contactCoords );

            map.addMarker( new Marker(markerOptions1) );
        }

        setDefaultMapOptions();
    }

    public static void setMarker(GoogleMapView googleMapView, LatLong coords) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coords);

        map.clearMarkers();

        googleMapView.setCenter(coords.getLatitude(), coords.getLongitude());
        googleMapView.setZoom(locationZoom);

        map.addMarker( new Marker(markerOptions) );
    }
}
