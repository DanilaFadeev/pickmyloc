package addressBook.controllers;

import addressBook.helpers.Animations;
import addressBook.helpers.SwitchScene;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class LoginController {
    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private Label errorMsgLabel;

    @FXML
    public void onSignIn(ActionEvent event) {
        // hardcoded values
        String login = "admin";
        String pass = "admin";

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals(login) && password.equals(pass)) {
            SwitchScene<MainController> switchScene = new SwitchScene<>("../views/forms/Main.fxml", true, true);
            switchScene.loadScene(event);
        } else {
            errorMsgLabel.setVisible(true);

            passwordField.setText("");

            Animations.shakeAnimation(usernameField);
            Animations.shakeAnimation(passwordField);
            Animations.shakeAnimation(errorMsgLabel);
        }
    }

    @FXML
    public void onRegisterClick(MouseEvent event) {
        SwitchScene<RegistrationController> switchScene = new SwitchScene<>("../views/forms/Registration.fxml");
        switchScene.loadScene(event);
    }
}
