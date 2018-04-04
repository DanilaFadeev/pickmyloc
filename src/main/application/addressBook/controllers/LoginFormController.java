package addressBook.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class LoginFormController {
    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    public void onSignIn(ActionEvent event) throws IOException {
        // hardcoded values
        String login = "admin";
        String pass = "admin";

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals(login) && password.equals(pass)) {
            URL url = new File("src/main/application/addressBook/views/MainForm.fxml").toURL();
            FXMLLoader Loader = new FXMLLoader(url);

            Loader.load();
            Parent mainForm = Loader.getRoot();

            Scene scene = new Scene(mainForm);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
        } else {
            System.out.println("Error in login");
        }
    }
}
