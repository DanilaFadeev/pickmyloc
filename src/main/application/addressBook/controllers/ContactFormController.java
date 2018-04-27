package addressBook.controllers;

import addressBook.data.Location;
import addressBook.helpers.DBConnection;
import addressBook.helpers.GoogleMapManager;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;


public class ContactFormController {
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
    private Label errorLabel;

    @FXML
    private void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose profile image");

        FileChooser.ExtensionFilter jpegExtension = new FileChooser.ExtensionFilter("Jpeg files (.jpeg)", "*.jpeg");
        fileChooser.getExtensionFilters().add(jpegExtension);

        FileChooser.ExtensionFilter pngExtension = new FileChooser.ExtensionFilter("Png files (.png)", "*.png");
        fileChooser.getExtensionFilters().add(pngExtension);

        File imageFile = fileChooser.showOpenDialog(nameField.getScene().getWindow());
    }

    @FXML
    void onSearchAddress(ActionEvent event) {
        String address = addressField.getText();
        LatLong coords = GoogleMapManager.getCoordsByAddress(address);

        if (coords != null)
            GoogleMapManager.setMarker(googleMapView, coords);
        else {
            errorLabel.setText("The address is not found. Type the full one");
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

    public void setGoogleMapView(GoogleMapView googleMapView) {
        this.googleMapView = googleMapView;
    }

    public void setEditingContact(Contact contact) {
        editingContact = contact;

        nameField.setText(contact.name.getValue());
        surnameField.setText(contact.surname.getValue());
        patronymicField.setText(contact.patronymic.getValue());
        phoneField.setText(contact.phone.getValue());
        mobilePhoneField.setText(contact.mobile.getValue());
        emailField.setText(contact.email.getValue());
        companyField.setText(contact.company.getValue());
        positionField.setText(contact.position.getValue());
        addressField.setText(contact.location.getAddress());
        birthdayField.setValue(contact.birthday);
    }

    public void createOrUpdateContact() {
        LatLong coords = GoogleMapManager.getCoordsByAddress(addressField.getText());
        Location location = new Location(addressField.getText(), coords.getLatitude(), coords.getLongitude());

        if (editingContact != null && editingContact.id != 0)
            DBConnection.getConnection().deleteContact(editingContact.id);

        Contact contact = new Contact(
            nameField.getText(),
            surnameField.getText(),
            patronymicField.getText(),
            emailField.getText(),
            phoneField.getText(),
            mobilePhoneField.getText(),
            "",
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

            Tooltip tt = new Tooltip();
            tt.setText("Error!");
            tt.setStyle("-fx-padding: 10px");
            nameField.setTooltip(tt);

            Point2D p = nameField.localToScene(0.0, 0.0);
            tt.show(nameField,
                    p.getX() + nameField.getScene().getX() + nameField.getScene().getWindow().getX() + nameField.getWidth() + 10,
                    p.getY() + nameField.getScene().getY() + nameField.getScene().getWindow().getY());


            validated = false;
        }

        if (surnameField.getText().isEmpty()) {
            surnameField.setStyle("-fx-background-color: #FBE9E7");
            validated = false;
        }

        if (addressField.getText().isEmpty()) {
            addressField.setStyle("-fx-background-color: #FBE9E7");
            validated = false;
        }


        return validated;
    }
}
