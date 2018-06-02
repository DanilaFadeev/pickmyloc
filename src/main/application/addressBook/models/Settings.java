package addressBook.models;

import javax.persistence.*;

@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @GeneratedValue
    private int id;

    private String lang;
    private int zoom;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "default_location_id")
    private Location location;

    public Settings() {

    }

    public Settings(String lang, int zoom, Location location) {
        this.lang = lang;
        this.zoom = zoom;
        this.location = location;
    }

    public String getLang() {
        return lang;
    }

    public int getZoom() {
        return zoom;
    }

    public Location getLocation() {
        return location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
