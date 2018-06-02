package addressBook.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;

@Entity
@Table(name="locations")
@Access(AccessType.PROPERTY)
public class Location {

    private int id;
    private StringProperty address = new SimpleStringProperty("");
    private Double latitude;
    private Double longitude;

    public Location(String address, Double latitude, Double longitude) {
        this.address = new SimpleStringProperty(address);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {}

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public String getAddress() {
        return address.getValue();
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
