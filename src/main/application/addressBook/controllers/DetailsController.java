package addressBook.controllers;

import addressBook.Main;
import addressBook.helpers.SwitchScene;
import addressBook.models.Contact;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DetailsController {

    @FXML
    private Label nameField;

    @FXML
    private Label surnameField;

    @FXML
    private Label patronymicField;

    @FXML
    private Label phoneField;

    @FXML
    private Label emailField;

    @FXML
    private Label addressField;

    @FXML
    private Label mobilePhoneField;

    @FXML
    private Label companyField;

    @FXML
    private Label positionField;

    @FXML
    private ImageView contactImageView;

    @FXML
    private Label birthdayField;

    @FXML
    private Label errorAddressLabel;

    @FXML
    private JFXButton sendBtn;

    @FXML
    private void backToHome(ActionEvent event) {
        SwitchScene<MainController> switchScene = new SwitchScene<>("../views/forms/Main.fxml");
        switchScene.loadScene(event);
    }

    @FXML
    private void onSendMail(ActionEvent event) {
        URI uri = null;
        try {
            uri = new URI("mailto", "john@example.com", null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Main.hostServices.showDocument("mailto:someone@example.com?Subject=Hello%20again");
    }

    void setContactInfo(Contact contact) {
        nameField.setText(contact.name.getValue());
        surnameField.setText(contact.surname.getValue());
        patronymicField.setText(contact.patronymic.getValue());
        phoneField.setText(contact.phone.getValue());
        mobilePhoneField.setText(contact.mobile.getValue());
        emailField.setText(contact.email.getValue());
        companyField.setText(contact.company.getValue());
        positionField.setText(contact.position.getValue());
        addressField.setText(contact.address.getValue());

        if (contact.birthday != null) {
            birthdayField.setText(contact.birthday.toString());
        }

        File image = new File("images/" + contact.getImagePath());
        Image img = new Image(image.toURI().toString());
        contactImageView.setImage(img);

        if (!contact.getEmail().isEmpty()) {
            sendBtn.setDisable(false);
        }
    }

}
