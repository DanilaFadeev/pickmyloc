package addressBook.controllers;

import addressBook.helpers.SwitchScene;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class LoginFormController {
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
            SwitchScene<MainController> switchScene = new SwitchScene<>("../views/MainForm.fxml", true, true);
            switchScene.loadScene(event);
        } else {
            errorMsgLabel.setVisible(true);

            passwordField.setText("");

            shakeAnimation(usernameField);
            shakeAnimation(passwordField);
            shakeAnimation(errorMsgLabel);
        }
    }

    private void shakeAnimation(Node node) {
        TranslateTransition ft = new TranslateTransition(Duration.millis(70), node);
        ft.setFromX(0f);
        ft.setByX(10f);
        ft.setCycleCount(3);
        ft.setAutoReverse(true);
        ft.playFromStart();
    }
}
