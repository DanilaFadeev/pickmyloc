package addressBook.controllers;

import addressBook.helpers.Animations;
import addressBook.helpers.HibernateUtil;
import addressBook.helpers.SwitchScene;
import addressBook.models.User;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

        User user = HibernateUtil.getInstance().getUser(username, password);

        if (user != null) {
            MainController.currentUser = user;

            SwitchScene<MainController> switchScene = new SwitchScene<>("forms/Main.fxml", true, true);
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
                new SwitchScene<>("forms/Registration.fxml", false, false);
        switchScene.loadScene(event);
    }

    @FXML
    public void onCloseApp(ActionEvent event) {
        SwitchScene.closeStage(event);
    }
}
