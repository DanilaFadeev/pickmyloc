package addressBook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/forms/Login.fxml"));
        primaryStage.setTitle("PickMyLoc");

        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles/main.css");

        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

// TODO list
// ********* Add contact ***********
// validation: check address correct, phone and mobile(not letters)

// db default table creation
// resizing trouble
