package addressBook.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import addressBook.helpers.SwitchScene;


public class FormController {
    @FXML
    private JFXTextField nameField;

    @FXML
    private void saveContact(ActionEvent event) {
        SwitchScene switchObj = new SwitchToContact("../views/home.fxml");
        switchObj.switchScene(event);
    }

    class SwitchToContact extends SwitchScene<Controller> {
        SwitchToContact(String FXMLPath) {
            super(FXMLPath);
        }

        @Override
        public void action() {
            String name = nameField.getText();

            SceneController.addContact(name);
        }
    }
}
