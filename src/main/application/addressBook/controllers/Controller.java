package addressBook.controllers;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;

import addressBook.Main;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class Controller {

    @FXML
    public void initialize() {

        ObservableList<Contact> filteredContacts = Main.data;

        JFXTreeTableColumn<Contact, String> nameColumn = new JFXTreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().name);

        JFXTreeTableColumn<Contact, String> surnameColumn = new JFXTreeTableColumn<>("Surname");
        surnameColumn.setCellValueFactory(param -> param.getValue().getValue().surname);

        JFXTreeTableColumn<Contact, String> phoneColumn = new JFXTreeTableColumn<>("Phone");
        phoneColumn.setCellValueFactory(param -> param.getValue().getValue().phone);

        JFXTreeTableColumn<Contact, String> addressColumn = new JFXTreeTableColumn<>("Address");
        addressColumn.setCellValueFactory(param -> param.getValue().getValue().address);

        // Style preferences
        JFXTreeTableColumn[] columns = { nameColumn, surnameColumn, phoneColumn, addressColumn };
        for (JFXTreeTableColumn column: columns ) {
            column.setStyle("-fx-font-size: 11px");
        }

        TreeItem<Contact> root = new RecursiveTreeItem<>(filteredContacts, RecursiveTreeObject::getChildren);
        contactsTable.getColumns().setAll(nameColumn, surnameColumn, phoneColumn, addressColumn);
        contactsTable.setRoot(root);
        contactsTable.setShowRoot(false);

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                contactsTable.setPredicate((Predicate<TreeItem<Contact>>) treeItem ->
                        treeItem.getValue().name.get().toLowerCase().contains(newValue.toLowerCase())
                        || treeItem.getValue().surname.get().toLowerCase().contains(newValue.toLowerCase())
                        || treeItem.getValue().phone.get().contains(newValue)
                ));

        googleMapView.addMapInializedListener(() -> configureMap());
    }

    protected void configureMap() {
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(53.902174, 27.5614256))
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(11);
        map = googleMapView.createMap(mapOptions, false);

//        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
//            LatLong latLong = event.getLatLong();
//            latitudeLabel.setText(formatter.format(latLong.getLatitude()));
//            longitudeLabel.setText(formatter.format(latLong.getLongitude()));
//        });

        for (int i = 0; i < 10; i++) {
            Random rand = new Random();

            float x = rand.nextFloat() / 10 - 0.02f;
            float y = rand.nextFloat() / 10 - 0.02f;

            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.position(new LatLong(53.9 + x, 27.5 + y));

            map.addMarker(new Marker(markerOptions1));
        }
    }

    private GoogleMap map;

    @FXML
    private GoogleMapView googleMapView;

    @FXML
    private JFXTreeTableView contactsTable;

    @FXML
    private JFXTextField searchField;

    @FXML
    private void onAddContact(ActionEvent event) {
        SwitchScene<FormController> switchScene = new SwitchScene<>("../views/contactForm.fxml");
        switchScene.switchScene(event);
    }

    void addContact(String name) {
       Main.data.add(new Contact(name, name, name, name));
    }

}
