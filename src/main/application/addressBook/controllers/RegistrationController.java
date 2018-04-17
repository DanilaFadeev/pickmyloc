package addressBook.controllers;

import addressBook.helpers.Animations;
import addressBook.helpers.SwitchScene;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class RegistrationController {

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField password1Field;

    @FXML
    private JFXPasswordField password2Field;

    @FXML
    void onRegister(ActionEvent event) {
        String pass1 = password1Field.getText();
        String pass2 = password2Field.getText();

        if ( !pass1.equals(pass2) ) {
            Animations.shakeAnimation(password1Field);
            Animations.shakeAnimation(password2Field);

            return;
        }

        SwitchScene<MainController> switchScene = new SwitchScene<>("../views/forms/Main.fxml", true, true);
        switchScene.loadScene(event);
    }

    @FXML
    void onBackToLogin(MouseEvent event) {
        SwitchScene<LoginController> switchScene = new SwitchScene<>("../views/forms/Login.fxml");
        switchScene.loadScene(event);
    }
}
