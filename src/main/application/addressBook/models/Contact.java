package addressBook.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Contact extends RecursiveTreeObject<Contact>{
    public StringProperty name;
    public StringProperty surname;
    public StringProperty address;
    public StringProperty phone;
    public StringProperty location;


    public Contact(String name, String surname, String address, String phone) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
    }

}
