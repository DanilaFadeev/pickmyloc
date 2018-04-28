package addressBook.controllers;

import addressBook.helpers.DBConnection;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.beans.*;
import java.io.*;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SettingsController {
    @FXML
    private void initialize() {
        ObservableList<String> options =
            FXCollections.observableArrayList(
                "Russian",
                "English",
                "French"
            );

        cbLanguage.itemsProperty().set(options);
        cbLanguage.setValue("Russian");

        Tooltip ttLang = new Tooltip("Help!!!");
        ttLanguage.setTooltip(ttLang);
    }

    @FXML
    private JFXComboBox<String> cbLanguage;

    @FXML
    private JFXButton ttLanguage;

    @FXML
    private JFXTextField locationField;

    @FXML
    private JFXSlider sliderZoom;

    @FXML
    private FontAwesomeIconView ttTakeFromMap;

    @FXML
    void onTakeFromMap(ActionEvent event) {

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
