package addressBook.helpers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitchScene<ControllerType> {
    private FXMLLoader Loader;
    protected ControllerType SceneController;

    public SwitchScene(String FXMLPath) {
        Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource(FXMLPath));

        try {
            Loader.load();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }

        SceneController = Loader.getController();
    }

    public void action() {

    }

    public void switchScene(ActionEvent event) {
        action();
        Parent contactForm = Loader.getRoot();

        Scene scene = new Scene(contactForm);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
}
