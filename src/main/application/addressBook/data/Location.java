package addressBook.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Location {
    public StringProperty address;
    private Double latitude;
    private Double longitude;

    public Location(String address, Double latitude, Double longitude) {
        this.address = new SimpleStringProperty(address);
        this.latitude = latitude;
        this.longitude = longitude;
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
