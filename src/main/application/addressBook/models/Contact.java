package addressBook.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class Contact extends RecursiveTreeObject<Contact>{
    public StringProperty name;
    public StringProperty surname;
    public StringProperty phone;
    public StringProperty mobilePhone;
    public StringProperty address;
    public StringProperty company;
    public StringProperty position;
    public Date birthday;
    public LatLong location;

    public Contact() {}

    // deprecated
    public Contact(String name, String surname, String address, String phone) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public void setSurname(String surname) {
        this.surname = new SimpleStringProperty(surname);
    }

    public void setPhone(String phone) {
        this.phone = new SimpleStringProperty(phone);
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = new SimpleStringProperty(mobilePhone);
    }

    public void setAddress(String address) {
        this.address = new SimpleStringProperty(address);
    }

    public void setCompany(String company) {
        this.company = new SimpleStringProperty(company);
    }

    public void setPosition(String position) {
        this.position = new SimpleStringProperty(position);
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setLocation(LatLong location) {
        this.location = location;
    }
}
