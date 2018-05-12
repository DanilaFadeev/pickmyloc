package addressBook.controllers;

import addressBook.helpers.DBConnection;
import addressBook.helpers.GoogleMapManager;
import addressBook.helpers.HelperUtils;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;
import addressBook.models.Location;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;


public class ContactFormController {
    private static final String ADDRESS_ERROR = "The address is not found. Type the full one";

    @FXML
    public void initialize() {

        birthdayField.setDayCellFactory((final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isAfter(LocalDate.now()) || item.isBefore(LocalDate.now().minusYears(120))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #a7d16b;");
                }
            }
        });
    }

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField surnameField;

    @FXML
    private JFXTextField patronymicField;

    @FXML
    private JFXTextField phoneField;

    @FXML
    private JFXTextField addressField;

    @FXML
    private JFXTextField mobilePhoneField;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXTextField companyField;

    @FXML
    private JFXTextField positionField;

    @FXML
    private JFXDatePicker birthdayField;

    @FXML
    private Label imageFileLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose profile image");

        FileChooser.ExtensionFilter jpegExtension = new FileChooser.ExtensionFilter("Jpeg files (.jpeg)", "*.jpeg");
        fileChooser.getExtensionFilters().add(jpegExtension);

        FileChooser.ExtensionFilter pngExtension = new FileChooser.ExtensionFilter("Png files (.png)", "*.png");
        fileChooser.getExtensionFilters().add(pngExtension);

        imageFile = fileChooser.showOpenDialog(nameField.getScene().getWindow());
        imageFileLabel.setText( imageFile.getAbsolutePath() );
    }

    @FXML
    void onSearchAddress(ActionEvent event) {
        String address = addressField.getText();
        LatLong coords = GoogleMapManager.getCoordsByAddress(address);

        if (coords != null)
            GoogleMapManager.setMarker(googleMapView, coords);
        else {
            errorLabel.setText(ContactFormController.ADDRESS_ERROR);
            errorLabel.setVisible(true);
        }
    }

    @FXML
    void onAddressTyping(KeyEvent event) {
        if (errorLabel.isVisible()) {
            errorLabel.setVisible(false);
        }
    }

    @FXML
    private void saveContact(ActionEvent event) {
        if (!validation()) {
            errorLabel.setVisible(true);
            return;
        }

        createOrUpdateContact();

        SwitchScene<MainController> switchScene = new SwitchScene<>("../views/forms/Main.fxml");
        switchScene.loadScene(event);
    }

    @FXML
    private void backToHome(ActionEvent event) {
        SwitchScene<MainController> switchScene = new SwitchScene<>("../views/forms/Main.fxml");
        switchScene.loadScene(event);
    }

    private Contact editingContact = null;
    private GoogleMapView googleMapView;
    private File imageFile = null;
    private LatLong coordinates;

    public void setGoogleMapView(GoogleMapView googleMapView) {
        this.googleMapView = googleMapView;
    }

    public void setEditingContact(Contact contact) {
        editingContact = contact;

        if (contact.name.getValue() != null)
            nameField.setText(contact.name.getValue());

        if (contact.surname.getValue() != null)
            surnameField.setText(contact.surname.getValue());

        if (contact.patronymic.getValue() != null)
            patronymicField.setText(contact.patronymic.getValue());

        if (contact.phone.getValue() != null)
            phoneField.setText(contact.phone.getValue());

        if (contact.mobile.getValue() != null)
            mobilePhoneField.setText(contact.mobile.getValue());

        if (contact.email.getValue() != null)
            emailField.setText(contact.email.getValue());

        if (contact.company.getValue() != null)
            companyField.setText(contact.company.getValue());

        if (contact.position.getValue() != null)
            positionField.setText(contact.position.getValue());

        if (contact.address.getValue() != null)
            addressField.setText(contact.address.getValue());

        if (contact.birthday != null)
            birthdayField.setValue(contact.birthday);
    }

    public void createOrUpdateContact() {
        Location location = null;
        if (coordinates != null) {
            location = new Location(addressField.getText(), coordinates.getLatitude(), coordinates.getLongitude());
        }

        if (editingContact != null && editingContact.id != 0)
            DBConnection.getConnection().deleteContact(editingContact.id);

        String imageHash = "";

        if (imageFile != null) {
            imageHash = HelperUtils.getSaltString();
            File imageTemp = new File("images/" + imageHash);
            try {
                HelperUtils.copyFileUsingStream(imageFile, imageTemp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Contact contact = new Contact(
            nameField.getText(),
            surnameField.getText(),
            patronymicField.getText(),
            emailField.getText(),
            phoneField.getText(),
            mobilePhoneField.getText(),
            imageHash,
            companyField.getText(),
            positionField.getText(),
            birthdayField.getValue(),
            location
        );

        try {
            DBConnection.getConnection().createContact(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validation() {
        boolean validated = true;

        if (nameField.getText().isEmpty()) {
            nameField.setStyle("-fx-background-color: #FBE9E7");
            validated = false;
        }

        if (!addressField.getText().isEmpty()) {
            coordinates = GoogleMapManager.getCoordsByAddress(addressField.getText());

            if (coordinates == null) {
                addressField.setStyle("-fx-background-color: #FBE9E7");
                errorLabel.setVisible(true);
                errorLabel.setText(ContactFormController.ADDRESS_ERROR);
                validated = false;
            }
        }

        return validated;
    }
}
