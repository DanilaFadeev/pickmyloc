package addressBook;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    public static double xOffset = 0;
    public static double yOffset = 0;

    public static HostServices hostServices;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/forms/Login.fxml"));
        primaryStage.setTitle("PickMyLoc");

        hostServices = getHostServices();

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles/main.css");

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        Main.launch(args);
    }
}

// TODO list

  /// PRODUCTION:
 // db default table creation

      /// DEVELOPMENT:
     // error when address deleted
    // password hash
   // import and export with images
  // email message to contact
 // prettify the details page (at all and when no field filled)
// delete image with removing a contact