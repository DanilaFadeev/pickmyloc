package addressBook;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import addressBook.models.Contact;
import addressBook.data.ContactsData;

public class Main extends Application {

    // hardcoded contacts data for testing
    public static ObservableList<Contact> data = ContactsData.getContactsList();

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
// birthday field
// upload and save image
// validation: check address correct, phone and mobile(not letters)

// ******** Settings ************
// export / import in xml
// choose lang for search
// choose start coords for map: by click on map or type


// ******** Details ************
// make details form responsive with anchor panel


// ******* DB SQLite ***********
// connection to DB (SQLite)
// CRUD operations


// resizing troubles
