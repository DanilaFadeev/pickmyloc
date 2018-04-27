package addressBook.models;

import addressBook.data.Location;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.Date;

public class Contact extends RecursiveTreeObject<Contact>{
    public StringProperty name;
    public StringProperty surname;
    public StringProperty patronymic;
    public StringProperty email;
    public StringProperty phone;
    public StringProperty mobile;
    public StringProperty imagePath;
    public StringProperty company;
    public StringProperty position;
    public LocalDate birthday;
    public Location location;

    public Contact(String name, String surname, String patronymic, String email, String phone, String mobile,
               String imagePath, String company, String position, LocalDate birthday, Location location) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.patronymic = new SimpleStringProperty(patronymic);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.mobile = new SimpleStringProperty(mobile);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.company = new SimpleStringProperty(company);
        this.position = new SimpleStringProperty(position);
        this.birthday = birthday;
        this.location = location;
    }
}
