package addressBook.controllers;

import addressBook.Classes.GoogleMapManager;
import com.jfoenix.controls.JFXTextField;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import addressBook.helpers.SwitchScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class newContactController {
    @FXML
    public void initialize() {
        googleMapView.addMapInializedListener(() -> GoogleMapManager.configureMap(googleMapView, null));
    }

    @FXML
    private GoogleMapView googleMapView;

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField addressField;

    @FXML
    private Label errorAddressLabel;

    @FXML
    private ImageView contactImageView;

    @FXML
    private void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose profile image");

        FileChooser.ExtensionFilter jpegExtension = new FileChooser.ExtensionFilter("Jpeg files (.jpeg)", "*.jpeg");
        fileChooser.getExtensionFilters().add(jpegExtension);

        FileChooser.ExtensionFilter pngExtension = new FileChooser.ExtensionFilter("Png files (.png)", "*.png");
        fileChooser.getExtensionFilters().add(pngExtension);

        File imageFile = fileChooser.showOpenDialog(nameField.getScene().getWindow());

        try {
            contactImageView.setImage(new Image(new FileInputStream(imageFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSearchAddress(ActionEvent event) {
        String address = addressField.getText();
        LatLong coords = GoogleMapManager.getCoordsByAddress(address);

        if (coords != null)
            GoogleMapManager.setMarker(googleMapView, coords);
        else
            errorAddressLabel.setVisible(true);
    }

    @FXML
    void onAddressTyping(KeyEvent event) {
        if (errorAddressLabel.isVisible()) {
            errorAddressLabel.setVisible(false);
        }
    }

    @FXML
    private void saveContact(ActionEvent event) {
        SwitchScene switchObj = new SwitchToContact("../views/home.fxml", true);
        switchObj.switchScene(event);
    }

    @FXML
    private void backToHome(ActionEvent event) {
        SwitchScene switchObj = new SwitchToContact("../views/home.fxml", false);
        switchObj.switchScene(event);
    }

    class SwitchToContact extends SwitchScene<Controller> {
        SwitchToContact(String FXMLPath) {
            super(FXMLPath);
        }

        SwitchToContact(String FXMLPath, boolean isSave) {
            super(FXMLPath);
            this.isSave = isSave;
        }

        boolean isSave = false;

        @Override
        public void action() {
            if (isSave) {
                String name = nameField.getText();
                SceneController.addContact(name);
            }
        }
    }
}
