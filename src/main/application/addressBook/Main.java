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
            new Contact("Jacob", "Smith", "Minsk, Spilivskogo", "299 23 54"),
            new Contact("Isabella", "Johnson", "Minsk, Octabryskaya", "299 45 23"),
            new Contact("Ethan", "Williams", "Minsk, Kuprevicha", "299 50 23"),
            new Contact("Emma", "Jones", "Minsk", "299 64 92"),
            new Contact("Michael", "Brown", "Moskow", "299 51 38")
    );

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/home.fxml"));
        primaryStage.setTitle("Address book");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // Geocoder
//        final Geocoder geocoder = new Geocoder();
//        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress("Беларусь, Минск ул. Янки Лучины").setLanguage("ru").getGeocoderRequest();
//        GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
//        System.out.println(geocoderResponse.getResults().get(0).getGeometry().getLocation());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
