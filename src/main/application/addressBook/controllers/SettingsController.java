package addressBook.controllers;

import addressBook.helpers.DBConnection;
import addressBook.helpers.GoogleMapManager;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;
import addressBook.models.Location;
import addressBook.models.Settings;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.lynden.gmapsfx.javascript.object.LatLong;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.beans.*;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;

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

        Settings settings = DBConnection.getConnection().getUserSettings(1);
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
        Settings settings = getParams();

        LatLong center = new LatLong(settings.getLocation().getLatitude(), settings.getLocation().getLongitude());
        GoogleMapManager.setMapOptions(center, settings.getZoom());

        DBConnection.getConnection().updateUserSettings(1, settings);
    }

    @FXML
    private void backToHome(ActionEvent event) {
        SwitchScene<MainController> switchScene = new SwitchScene<>("../views/forms/Main.fxml");
        switchScene.loadScene(event);
    }

    @FXML
    private void onExportContacts(ActionEvent event) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("contacts.xml");

            XMLEncoder encoder = new XMLEncoder(fos);

            encoder.setExceptionListener(new ExceptionListener() {
                public void exceptionThrown(Exception e) {
                    System.out.println("Exception! :"+e.toString());
                }
            });

            encoder.setPersistenceDelegate(LocalDate.class,
                    new PersistenceDelegate() {
                        @Override
                        protected Expression instantiate(Object localDate, Encoder encdr) {
                            return new Expression(localDate,
                                    LocalDate.class,
                                    "parse",
                                    new Object[]{localDate.toString()});
                        }
                    });

            for (Contact contact: MainController.contacts) {
                encoder.writeObject(contact);
            }

            encoder.close();

            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
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

    private Settings getParams() {
        String lang;

        switch (cbLanguage.getValue()) {
            case "Russian": {
                lang = "ru";
                break;
            }
            default: {
                lang = "en";
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

        int zoom = (int) sliderZoom.getValue();

        return new Settings(lang, zoom, location);
    }

    public void onImportContacts(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose source file with contacts data");

        FileChooser.ExtensionFilter jpegExtension = new FileChooser.ExtensionFilter("XML files (.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(jpegExtension);

        File imageFile = fileChooser.showOpenDialog(cbLanguage.getScene().getWindow());

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imageFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        XMLDecoder decoder = new XMLDecoder(fis);

        try {
            for (;;) {
                Contact contact = (Contact) decoder.readObject();
                DBConnection.getConnection().createContact(contact);
            }
        } catch (ArrayIndexOutOfBoundsException exc) {
            // do nothing
        } catch (SQLException e) {
            e.printStackTrace();
        }


        decoder.close();

        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
