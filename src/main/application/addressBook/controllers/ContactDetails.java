package addressBook.controllers;

import addressBook.models.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ContactDetails {

    @FXML
    private Label nameField;

    @FXML
    private Label surnameField;

    @FXML
    private Label phoneField;

    @FXML
    private Label addressField;

    @FXML
    private Label errorAddressLabel;

    @FXML
    private Label mobilePhoneField;

    @FXML
    private ImageView contactImageView;

    @FXML
    private Label birthdayField;

    @FXML
    void backToHome(ActionEvent event) {

    }

    public void setContactInfo(Contact contact) {
        nameField.setText( contact.name.getValue() );
        surnameField.setText( contact.surname.getValue() );

        addressField.setText( contact.address.getValue() );
    }

}
