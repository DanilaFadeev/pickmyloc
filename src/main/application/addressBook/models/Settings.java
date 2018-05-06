package addressBook.models;

public class Settings {
    private String lang;
    private int zoom;
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
}
