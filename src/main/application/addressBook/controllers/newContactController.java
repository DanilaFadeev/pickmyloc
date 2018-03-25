package addressBook.controllers;

import addressBook.Classes.GoogleMapManager;
import com.jfoenix.controls.JFXTextField;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import addressBook.helpers.SwitchScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;


public class newContactController {
    @FXML
    public void initialize() {
        googleMapView.addMapInializedListener(() -> GoogleMapManager.configureMap(googleMapView));
    }

    @FXML
    private GoogleMapView googleMapView;

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField addressLabel;

    @FXML
    private Label errorAddressLabel;

    @FXML
    void onSearchAddress(ActionEvent event) {
        String address = addressLabel.getText();
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
