package addressBook;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/home.fxml"));
        primaryStage.setTitle("Address book");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
