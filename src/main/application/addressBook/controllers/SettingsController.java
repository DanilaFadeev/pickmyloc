package addressBook.controllers;

import addressBook.helpers.*;
import addressBook.models.Contact;
import addressBook.models.Location;
import addressBook.models.Settings;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.beans.*;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class SettingsController {
    private final String ADDRESS_NOT_FOUND = "Not found";
    private int location_id = 0;

    @FXML
    private void initialize() {
        ObservableList<String> options =
            FXCollections.observableArrayList(
                "Russian",
                "English"
            );

        cbLanguage.itemsProperty().set(options);

        Settings settings = MainController.currentUser.getSettings();
        setParams(settings);

        Tooltip ttLang = new Tooltip("Language for address search");
        ttAddressArea.setTooltip(ttLang);

        Tooltip ttAddress = new Tooltip("Address shown as default");
        ttLocationArea.setTooltip(ttAddress);

        Tooltip ttTake = new Tooltip("Take current map coordinates");
        ttTakeBtnArea.setTooltip(ttTake);

        locationField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                LatLong centerCoords = GoogleMapManager.getCoordsByAddress( locationField.getText() );

                if (centerCoords != null) {
                    GoogleMapManager.setMapOptions(centerCoords, (int) sliderZoom.getValue());
                } else {
                    locationField.setText( ADDRESS_NOT_FOUND );
                }
            }
        });
    }

    @FXML
    private JFXComboBox<String> cbLanguage;

    @FXML
    private JFXTextField locationField;

    @FXML
    private JFXSlider sliderZoom;

    @FXML
    private Label ttAddressArea;

    @FXML
    private Label ttLocationArea;

    @FXML
    private Label ttTakeBtnArea;

    @FXML
    private void onTakeFromMap(ActionEvent event) {
        Settings settings = GoogleMapManager.getMapOptions();
        setParams(settings);
    }

    @FXML
    private void onApply(ActionEvent event) {
        Settings userSettings = MainController.currentUser.getSettings();
        updateSettings(userSettings);

        LatLong center = new LatLong(userSettings.getLocation().getLatitude(), userSettings.getLocation().getLongitude());
        GoogleMapManager.setMapOptions(center, userSettings.getZoom());

        HibernateUtil.getInstance().save(userSettings);
    }

    @FXML
    private void backToHome(ActionEvent event) {
        SwitchScene<MainController> switchScene = new SwitchScene<>("forms/Main.fxml");
        switchScene.loadScene(event);
    }

    @FXML
    private void onExportContacts(ActionEvent event) {
        final String EXPORT_FILE_NAME = "contacts.xml";

        Alert exportAlert = new Alert( Alert.AlertType.CONFIRMATION );
        exportAlert.setGraphic(null);
        exportAlert.setHeaderText(null);
        exportAlert.setHeaderText("Export");
        exportAlert.setContentText("Do you want to export " + MainController.contacts.size() +
                " to " + EXPORT_FILE_NAME + " file?");

        Optional<ButtonType> result = exportAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            HelperUtils.xmlDecode(EXPORT_FILE_NAME, MainController.contacts);

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setContentText("All contacts were exported!");
            success.show();
        }
    }

    @FXML
    public void onImportContacts(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose source file with contacts data");

        FileChooser.ExtensionFilter jpegExtension = new FileChooser.ExtensionFilter("XML files (.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(jpegExtension);

        File importFile = fileChooser.showOpenDialog(cbLanguage.getScene().getWindow());
        ArrayList<Contact> contacts = HelperUtils.xmlEncode(importFile.getAbsolutePath());

        Alert importAlert = new Alert(Alert.AlertType.CONFIRMATION);

        importAlert.setGraphic(null);
        importAlert.setHeaderText(null);
        importAlert.setHeaderText("Import");
        importAlert.setContentText("Do you want to import " + contacts.size() +
                " from " + importFile.getName() + " file?");

        Optional<ButtonType> result = importAlert.showAndWait();

        if (result.get() == ButtonType.OK) {
            for (Contact contact : contacts) {
                contact.setId(0);
                contact.setUser(MainController.currentUser);
                HibernateUtil.getInstance().save(contact);
            }
        }
    }

    private void setParams(Settings settings) {
        if (!settings.getLang().isEmpty()) {
            switch (settings.getLang()) {
                case "ru": {
                    cbLanguage.setValue("Russian");
                    break;
                }
                default: {
                    cbLanguage.setValue("English");
                }
            }
        }

        sliderZoom.setValue(settings.getZoom());

        if (settings.getLocation().getAddress().isEmpty()) {
            locationField.setText(settings.getLocation().getLatitude() + "," + settings.getLocation().getLongitude());
        } else {
            locationField.setText(settings.getLocation().getAddress());
        }
    }

    private void updateSettings(Settings settings) {
        switch (cbLanguage.getValue()) {
            case "Russian": {
                settings.setLang("ru");
                break;
            }
            default: {
                settings.setLang("en");
            }
        }

        Location location;

        if (locationField.getText().matches("^\\d+\\.\\d+,\\d+\\.\\d+$")) {
            String[] strCoords = locationField.getText().split(",");

            Double latitude = Double.parseDouble(strCoords[0]);
            Double longitude = Double.parseDouble(strCoords[1]);

            location = new Location("", latitude, longitude);
        } else {
            LatLong coords = GoogleMapManager.getCoordsByAddress( locationField.getText() );
            location = new Location(locationField.getText(), coords.getLatitude(), coords.getLongitude());
        }

        settings.setLocation(location);

        int zoom = (int) sliderZoom.getValue();
        settings.setZoom(zoom);
    }
}
