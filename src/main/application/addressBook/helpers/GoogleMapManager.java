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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleMapManager {
    private static final int locationZoom = 14;

    private static GoogleMap map;
    private static GoogleMapView mapView;

    private static Map<Integer, Marker> mapMarkers = new HashMap<>();

    public static void configureMap(GoogleMapView googleMapView, JFXSpinner spinner, ContactsController cc) {
        MapOptions mapOptions = new MapOptions();

        double initLatitude = MainController.appSettings.getLocation().getLatitude();
        double initLongitude = MainController.appSettings.getLocation().getLongitude();

        mapOptions.center(new LatLong(initLatitude, initLongitude))
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(MainController.appSettings.getZoom());

        mapView = googleMapView;
        map = googleMapView.createMap(mapOptions, false);

        map.addStateEventHandler(MapStateEventType.tilesloaded, () -> spinner.setVisible(false));

        initContactsMarkers(MainController.contacts, cc);
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

    public static void initContactsMarkers(ObservableList<Contact> contacts, ContactsController contactsController) {
        for (Contact contact: contacts) {
            if (contact.location == null)
                continue;

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position( new LatLong(
                contact.location.getLatitude(),
                contact.location.getLongitude())
            );

            Marker marker = new Marker(markerOptions);
            marker.setTitle(contact.getName() + " " + (contact.getSurname() != null ? contact.getSurname() : ""));
            map.addMarker( marker );

            map.addUIEventHandler(marker, UIEventType.click, param -> {
                contactsController.onSelectContact();
                setMarker(mapView, contact);
            });

            mapMarkers.put(contact.hashCode(), marker);
        }

        setDefaultMapOptions();
    }

    public static void displayAllMarkers(boolean isDisplayed) {
        for (Map.Entry<Integer, Marker> m : mapMarkers.entrySet()) {
            m.getValue().setVisible(isDisplayed);
        }

        if (isDisplayed) {
            setDefaultMapOptions();
        }
    }

    public static void setMarker(GoogleMapView googleMapView, Contact contact) {
        displayAllMarkers(false);

        for (Map.Entry<Integer, Marker> m : mapMarkers.entrySet()) {
            if (m.getKey() == contact.hashCode()) {
                m.getValue().setVisible(true);
                break;
            }
        }

        googleMapView.setCenter(contact.location.getLatitude(), contact.location.getLongitude());
        googleMapView.setZoom(locationZoom);
    }
}
