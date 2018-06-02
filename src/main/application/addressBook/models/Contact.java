package addressBook.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "contacts")
@Access(AccessType.PROPERTY)
public class Contact extends RecursiveTreeObject<Contact> {

    public int id;

    private Date birthday;
    private Location location;

    public StringProperty name = new SimpleStringProperty();
    public StringProperty surname = new SimpleStringProperty();
    public StringProperty patronymic = new SimpleStringProperty();
    public StringProperty email = new SimpleStringProperty();
    public StringProperty phone = new SimpleStringProperty();
    public StringProperty mobile = new SimpleStringProperty();
    public StringProperty imagePath = new SimpleStringProperty();
    public StringProperty company = new SimpleStringProperty();
    public StringProperty position = new SimpleStringProperty();
    public StringProperty address = new SimpleStringProperty();

    public Contact() {

    }

    private User user;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Column(name = "image_path")
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

    @Temporal(TemporalType.DATE)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        if (location != null) {
            this.address = new SimpleStringProperty(location.getAddress());
        }
    }
}
