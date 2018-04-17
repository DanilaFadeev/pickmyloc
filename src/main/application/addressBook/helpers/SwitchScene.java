package addressBook.helpers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitchScene<Controller> {
    public SwitchScene(String fxml) {
        Loader = new FXMLLoader();
        this.fxml = fxml;
    }

    public SwitchScene(String fxml, boolean isCentered, boolean isResizable) {
        Loader = new FXMLLoader();

        this.fxml = fxml;
        this.isCentered = isCentered;
        this.isResizible = isResizable;
    }

    private String fxml;
    private boolean isCentered = false;
    private boolean isResizible = true;
    private FXMLLoader Loader;

    public Controller loadToPane(Pane rootPane) {
        Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource(fxml));

        try {
            Pane pane = Loader.load();
            rootPane.getChildren().setAll(pane.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Loader.getController();
    }

    public Controller loadScene(Event event) {
        Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource(fxml));

        Parent mainForm = null;

        try {
            mainForm = Loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(mainForm);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);

        if (isCentered) {
            stage.centerOnScreen();
        }

        stage.setResizable(isResizible);

        return Loader.getController();
    }
}


