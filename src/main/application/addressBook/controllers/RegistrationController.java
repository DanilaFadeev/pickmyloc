package addressBook.controllers;

import addressBook.helpers.*;
import addressBook.models.Location;
import addressBook.models.Settings;
import addressBook.models.User;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class RegistrationController {
    private final String EMAIL_VALIDATION = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.," +
            ";:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([" +
            "a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    @FXML
    public void initialize() {
        emailField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                emailField.setUnFocusColor(Color.BLACK);
                errorMsg.setVisible(false);
            }
        });

        usernameField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                usernameField.setUnFocusColor(Color.BLACK);
                errorMsg.setVisible(false);
            }
        });

        password1Field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                password1Field.setUnFocusColor(Color.BLACK);
                errorMsg.setVisible(false);
            }
        });

        password2Field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                password2Field.setUnFocusColor(Color.BLACK);
                errorMsg.setVisible(false);
            }
        });
    }

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField password1Field;

    @FXML
    private JFXPasswordField password2Field;

    @FXML
    private Text errorMsg;

    @FXML
    void onRegister(ActionEvent event) {
        boolean validated = true;

        String pass1 = password1Field.getText();
        String pass2 = password2Field.getText();

        if (!emailField.getText().matches(EMAIL_VALIDATION)) {
            emailField.setUnFocusColor(Color.RED);
            Animations.shakeAnimation(emailField);

            validated = false;
        }

        if (usernameField.getText().length() < 4) {
            usernameField.setUnFocusColor(Color.RED);
            Animations.shakeAnimation(usernameField);

            validated = false;
        }

        if ( !pass1.equals(pass2) || pass1.isEmpty() ) {
            password1Field.setUnFocusColor(Color.RED);
            password2Field.setUnFocusColor(Color.RED);

            Animations.shakeAnimation(password1Field);
            Animations.shakeAnimation(password2Field);

            validated = false;
        }

        if (validated) {
            Location defaultLocation = new Location("", 50.0641917, 27.421875);
            Settings defaultSettings = new Settings("en", 4, defaultLocation);
            User newUser = new User(emailField.getText(), usernameField.getText(), pass1, defaultSettings);

            HibernateUtil.getInstance().save(newUser);

            if (newUser.getId() != 0) {
                MainController.currentUser = newUser;

                SwitchScene<MainController> switchScene = new SwitchScene<>("forms/Main.fxml", true, false);
                switchScene.loadScene(event);
            }
        } else {
            errorMsg.setVisible(true);
        }
    }

    @FXML
    void onBack(ActionEvent event) {
        SwitchScene<LoginController> switchScene = new SwitchScene<>("forms/Login.fxml", false, false);
        switchScene.loadScene(event);
    }

    @FXML
    void onCloseApp(ActionEvent event) {
        SwitchScene.closeStage(event);
    }
}
