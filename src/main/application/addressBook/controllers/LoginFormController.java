package addressBook.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LoginFormController {
    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private Label errorMsgLabel;

    @FXML
    public void onSignIn(ActionEvent event) throws IOException {
        // hardcoded values
        String login = "admin";
        String pass = "admin";

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals(login) && password.equals(pass)) {
            FXMLLoader Loader = new FXMLLoader();
            Loader.setLocation(getClass().getResource("../views/MainForm.fxml"));

            Loader.load();
            Parent mainForm = Loader.getRoot();

            Scene scene = new Scene(mainForm);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);

            stage.centerOnScreen();
            stage.setResizable(true);
        } else {
            errorMsgLabel.setVisible(true);

            passwordField.setText("");

            shakeAnimation(usernameField);
            shakeAnimation(passwordField);
            shakeAnimation(errorMsgLabel);
        }
    }

    public void shakeAnimation(Node node) {
        TranslateTransition ft = new TranslateTransition(Duration.millis(70), node);
        ft.setFromX(0f);
        ft.setByX(10f);
        ft.setCycleCount(3);
        ft.setAutoReverse(true);
        ft.playFromStart();
    }
}
