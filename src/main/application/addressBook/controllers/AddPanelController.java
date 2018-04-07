package addressBook.controllers;

import addressBook.Classes.GoogleMapManager;
import addressBook.Main;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class AddPanelController {

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField surnameField;

    @FXML
    private JFXTextField phoneField;

    @FXML
    private JFXTextField addressField;

    @FXML
    private JFXTextField mobilePhoneField;

    @FXML
    private JFXTextField companyField;

    @FXML
    private JFXTextField positionField;

    @FXML
    private JFXDatePicker birthdayField;

    @FXML
    private ImageView contactImageView;

    @FXML
    private Label errorAddressLabel;

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

//        if (coords != null)
//            GoogleMapManager.setMarker(googleMapView, coords);
//        else
//            errorAddressLabel.setVisible(true);
    }

    @FXML
    void onAddressTyping(KeyEvent event) {
        if (errorAddressLabel.isVisible()) {
            errorAddressLabel.setVisible(false);
        }
    }

    @FXML
    private void saveContact(ActionEvent event) {
        createOrUpdateContact();

        SwitchScene switchObj = new SwitchToContact("../views/MainForm.fxml", true);
        switchObj.switchScene(event);
    }

    @FXML
    private void backToHome(ActionEvent event) {
        SwitchScene switchObj = new SwitchToContact("../views/MainForm.fxml", false);
        switchObj.switchScene(event);
    }

    private Contact editingContact = null;

    public void setEditingContact(Contact contact) {
        editingContact = contact;

        nameField.setText(contact.name.getValue());
        surnameField.setText(contact.surname.getValue());
        phoneField.setText(contact.phone.getValue());
        mobilePhoneField.setText(contact.mobilePhone.getValue());
        companyField.setText(contact.company.getValue());
        positionField.setText(contact.position.getValue());
        addressField.setText(contact.address.getValue());
    }

    public void createOrUpdateContact() {
        Contact contact;

        if (editingContact != null) {
            contact = editingContact;
        } else {
            contact = new Contact();
        }

        contact.setName(nameField.getText());
        contact.setSurname(surnameField.getText());
        contact.setPhone(phoneField.getText());
        contact.setCompany(companyField.getText());
        contact.setPosition(positionField.getText());
        contact.setMobilePhone(mobilePhoneField.getText());
        contact.setAddress(addressField.getText());

        if (editingContact == null) {
            Main.data.add(contact);
        }
    }

    class SwitchToContact extends SwitchScene<MainController> {
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
                //String name = nameField.getText();
                //SceneController.addContact(name);
            }
        }
    }
}
