package addressBook.controllers;

import addressBook.helpers.Animations;
import addressBook.helpers.DBConnection;
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
        String username = usernameField.getText();
        String password = passwordField.getText();

        int userId = DBConnection.getConnection().checkUserLogin(username, password);

        if (userId != 0) {
            MainController.userId = userId;

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
    public void onSignUp(ActionEvent event) {
        SwitchScene<RegistrationController> switchScene =
                new SwitchScene<>("../views/forms/Registration.fxml", false, false);
        switchScene.loadScene(event);
    }
}
