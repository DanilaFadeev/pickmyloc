package addressBook;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import addressBook.models.Contact;

public class Main extends Application {

    public static ObservableList<Contact> data = FXCollections.observableArrayList(
            new Contact("Jacob", "Smith", "г. Минск ул. Янки Лучины", "299 23 54"),
            new Contact("Isabella", "Johnson", "Беларусь, Минск ул. Петруся Бровки", "299 45 23"),
            new Contact("Ethan", "Williams", "Беларусь, Минск ул. Голубева", "299 50 23"),
            new Contact("Emma", "Jones", "Беларусь, Минск ул. Янки Купалы", "299 64 92"),
            new Contact("Michael", "Brown", "Беларусь, Минск Уручье", "299 51 38")
    );

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/Login.fxml"));
        primaryStage.setTitle("Address book");

        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles/main.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

// TODO list
// expand fields of contact class according add form
// make method for dynamic location buttons for the left panel
// choose and implement new font for whole app
// make all form responsive with anchor panel
// method for edit contact (open addForm with contact data)
// connection to DB (SQLite)
// CRUD operations
// Add some validation for addForm
// Login form connected to app
// Create a register form
