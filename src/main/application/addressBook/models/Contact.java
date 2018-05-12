package addressBook.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Contact extends RecursiveTreeObject<Contact>{
    public int id = 0;
    public StringProperty name;
    public StringProperty surname;
    public StringProperty patronymic;
    public StringProperty email;
    public StringProperty phone;
    public StringProperty mobile;
    public StringProperty imagePath;
    public StringProperty company;
    public StringProperty position;
    public StringProperty address;

    public Contact() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getPatronymic() {
        return patronymic.get();
    }

    public void setPatronymic(String patronymic) {
        this.patronymic.set(patronymic);
    }

    public String getEmail() {
        return email.get();
    }


    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getMobile() {
        return mobile.get();
    }


    public void setMobile(String mobile) {
        this.mobile.set(mobile);
    }

    public String getImagePath() {
        return imagePath.get();
    }


    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    public String getCompany() {
        return company.get();
    }


    public void setCompany(String company) {
        this.company.set(company);
    }

    public String getPosition() {
        return position.get();
    }


    public void setPosition(String position) {
        this.position.set(position);
    }

    public String getBirthday() {
        return birthday.toString();
    }

    public void setBirthday(String birthday) {
        this.birthday = LocalDate.parse(birthday);
    }

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

        if (location != null) {
            this.address = location.address;
        } else {
            this.address = new SimpleStringProperty(null);
        }
    }

    public void setId(int id) {
        this.id = id;
    }
}
