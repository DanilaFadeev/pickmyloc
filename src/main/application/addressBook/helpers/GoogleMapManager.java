package addressBook.helpers;

import addressBook.controllers.ContactsController;
import addressBook.controllers.MainController;
import addressBook.models.Contact;
import addressBook.models.Location;
import addressBook.models.Settings;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.jfoenix.controls.JFXSpinner;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.event.MapStateEventType;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapManager {
    private static final int locationZoom = 14;

    private static GoogleMap map;
    private static GoogleMapView mapView;

    private static ArrayList<Marker> mapMarkers;

    public static void configureMap(GoogleMapView googleMapView, JFXSpinner spinner) {
        MapOptions mapOptions = new MapOptions();

        double initLatitude = MainController.appSettings.getLocation().getLatitude();
        double initLongitude = MainController.appSettings.getLocation().getLongitude();

        mapOptions.center(new LatLong(initLatitude, initLongitude))
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(MainController.appSettings.getZoom());

        mapView = googleMapView;
        map = googleMapView.createMap(mapOptions, false);

        map.addStateEventHandler(MapStateEventType.tilesloaded, () -> spinner.setVisible(false));

        setAllMarkers(MainController.contacts);
    }

    public static void setDefaultMapOptions() {
        double initLatitude = MainController.appSettings.getLocation().getLatitude();
        double initLongitude = MainController.appSettings.getLocation().getLongitude();

        map.setCenter(new LatLong(initLatitude, initLongitude));
        map.setZoom(MainController.appSettings.getZoom());
    }

    public static void setMapOptions(LatLong center, int zoom) {
        map.setCenter(center);
        map.setZoom(zoom);
    }

    public static Settings getMapOptions() {
        Location location = new Location("", map.getCenter().getLatitude(), map.getCenter().getLongitude());
        return new Settings("", map.getZoom(), location);
    }

    public static LatLong getCoordsByAddress(String address) {
        final Geocoder geocoder = new Geocoder();

        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder()
                .setAddress(address)
                .setLanguage(MainController.appSettings.getLang())
                .getGeocoderRequest();

        GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
        List<GeocoderResult> geoResults = geocoderResponse.getResults();

        // the second try to load coordinates, because the first one
        // is not always success
        if (geoResults.size() == 0) {
            geocoderResponse = geocoder.geocode(geocoderRequest);
            geoResults = geocoderResponse.getResults();
        }

        if (geoResults.size() == 0) {
            return null;
        }

        GeocoderGeometry geometryResult = geoResults.get(0).getGeometry();

        double latitude = geometryResult.getLocation().getLat().doubleValue();
        double longitude = geometryResult.getLocation().getLng().doubleValue();

        return new LatLong(latitude, longitude);
    }

    public static void setAllMarkers(ObservableList<Contact> contacts) {
        for (Contact c: contacts) {
            if (c.location == null)
                continue;

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position( new LatLong(c.location.getLatitude(), c.location.getLongitude()) );

            map.addMarker( new Marker(markerOptions) );
        }

        setDefaultMapOptions();
    }

    public static void setAllMarkers(ObservableList<Contact> contacts, ContactsController contactsController) {
        for (Contact c: contacts) {
            if (c.location == null)
                continue;

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position( new LatLong(c.location.getLatitude(), c.location.getLongitude()) );

            Marker marker = new Marker(markerOptions);
            map.addMarker( marker );

            map.addUIEventHandler(marker, UIEventType.click, param -> {
                contactsController.onSelectContact();
                setMarker(mapView, new LatLong(c.location.getLatitude(), c.location.getLongitude()));
            });
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
