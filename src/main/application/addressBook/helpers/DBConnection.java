package addressBook.helpers;

import addressBook.controllers.MainController;
import addressBook.models.Location;
import addressBook.models.Contact;
import addressBook.models.Settings;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {
    private static final String DEFAULT_DB = "src/main/resources/database.db";
    private static Connection conn = null;
    private static DBConnection instance = null;

    private DBConnection() {}

    public static DBConnection getConnection() {
        if (instance != null) {
            return instance;
        }

        instance = new DBConnection();

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + DEFAULT_DB);

            if (conn != null) {
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("Connected: " + metaData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instance;
    }

    public void createTables() {
        //String sql = "CREATE TABLE IF NOT EXISTS `users` ( id INT PRIMARY KEY, name VARCHAR NOT NULL )";
        String sql = "DROP table `contacts`";

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Contact> getAllContacts(int userId) {
        ArrayList<Contact> contacts = new ArrayList<>();

        String sql = "SELECT * FROM contacts LEFT JOIN locations l on contacts.location_id = l.id" +
                " WHERE user_id = " + userId + ";";

        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sql);

            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String patronymic = result.getString("patronymic");
                String email = result.getString("email");
                String phone = result.getString("phone");
                String mobile = result.getString("mobile");
                String imagePath = result.getString("image_path");
                String company = result.getString("company");
                String position = result.getString("position");

                LocalDate birthday = LocalDate.parse( result.getString("birthday") );

                String address = result.getString("address");
                Double latitude = result.getDouble("latitude");
                Double longitude = result.getDouble("longitude");
                Location location = new Location(address, latitude, longitude);

                Contact contact = new Contact(name, surname, patronymic, email, phone,
                        mobile, imagePath, company, position, birthday, location);
                contact.setId(id);

                contacts.add(contact);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contacts;
    }

    public void deleteContact(Integer id) {
        String sql = "DELETE FROM `contacts` WHERE id=" + id;

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String makeInsertSQL(String table, Map<String, Object> params) {
        String sql = "INSERT INTO `" + table + "` (";

        String fields = "";
        String values = " ) VALUES ( ";

        for(Map.Entry<String, Object> item : params.entrySet()) {
            fields += item.getKey() + ", ";
            if (item.getValue() instanceof Integer || item.getValue() instanceof Double) {
                values += item.getValue() + ", ";
            } else if (item.getValue() instanceof String) {
                values += "\"" + item.getValue() + "\", ";
            } else if (item.getValue() instanceof LocalDate) {
                values += "\"" + Date.valueOf( (LocalDate)item.getValue() ) + "\", ";
            }
        }

        fields = fields.substring(0, fields.length() - 2);
        values = values.substring(0, values.length() - 2);

        return sql + fields + values + ");";
    }

    public void createContact(Contact contact) throws SQLException {
        Map<String, Object> fields = new HashMap<>();

        if (contact.location != null) {
            String sqlLocation = "SELECT COUNT(*) AS total, id FROM `locations` " +
                    "WHERE address=\"" + contact.location.getAddress() + "\"";

            Statement st = conn.createStatement();
            ResultSet locationResult = st.executeQuery(sqlLocation);

            if (locationResult.getInt("total") == 0) {
                Map<String, Object> locationFields = new HashMap<>();

                locationFields.put("address", contact.location.getAddress());
                locationFields.put("latitude", contact.location.getLatitude());
                locationFields.put("longitude", contact.location.getLongitude());

                st.execute( makeInsertSQL("locations", locationFields) );

                Integer locationId = st.getGeneratedKeys().getInt(1);
                fields.put("location_id", locationId);
            } else {
                fields.put("location_id", locationResult.getInt("id"));
            }
        }

        if (!contact.name.getValue().isEmpty())
            fields.put("name", contact.name.getValue());

        if (!contact.surname.getValue().isEmpty())
            fields.put("surname", contact.surname.getValue());

        if (!contact.patronymic.getValue().isEmpty())
            fields.put("patronymic", contact.patronymic.getValue());

        if (!contact.email.getValue().isEmpty())
            fields.put("email", contact.email.getValue());

        if (!contact.phone.getValue().isEmpty())
            fields.put("phone", contact.phone.getValue());

        if (!contact.mobile.getValue().isEmpty())
            fields.put("mobile", contact.mobile.getValue());

        if (!contact.imagePath.getValue().isEmpty())
            fields.put("image_path", contact.imagePath.getValue());

        if (!contact.company.getValue().isEmpty())
            fields.put("company", contact.company.getValue());

        if (!contact.position.getValue().isEmpty())
            fields.put("position", contact.position.getValue());

        if(contact.birthday != null)
            fields.put("birthday", contact.birthday);

        fields.put("user_id", MainController.userId);

        System.out.println(makeInsertSQL("contacts", fields));

        Statement stmt = conn.createStatement();
        stmt.execute(makeInsertSQL("contacts", fields));
    }

    public int createUser(String email, String username, String password) {
        // check for the unique
        try {
            String sql = "SELECT COUNT(*) as total FROM `users` WHERE email=\"" + email + "\" OR username=\"" + username + "\";";
            Statement stmt = conn.createStatement();

            ResultSet resultSet = stmt.executeQuery(sql);
            if (resultSet.next() && resultSet.getInt("total") != 0) {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // crete user
        try {
            String sql = "INSERT INTO `users` (username, email, password) VALUES (\"" + username + "\", \""
                    + email + "\", \"" + password + "\");";

            Statement stmt = conn.createStatement();
            stmt.execute(sql);

            return stmt.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int checkUserLogin(String emailOrUsername, String password) {
        try {
            String sql = "SELECT id FROM `users` WHERE email=\"" + emailOrUsername + "\" OR username=\"" + emailOrUsername + "\" AND password=\"" + password + "\";";
            Statement stmt = conn.createStatement();

            ResultSet resultSet = stmt.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Settings getUserSettings(int userId) {
        String sql = "SELECT lang, zoom, address, latitude, longitude FROM `settings` as s" +
                " LEFT JOIN `locations` as l where s.default_location_id = l.id AND s.user_id = " + userId + ";";

        Settings settings = null;

        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sql);

            if (result.next()) {
                String lang = result.getString("lang");
                String address = result.getString("address");
                int zoom = result.getInt("zoom");
                double latitude = result.getDouble("latitude");
                double longitude = result.getDouble("longitude");

                Location location = new Location(address, latitude, longitude);
                settings = new Settings(lang, zoom, location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return settings;
    }

    public void updateUserSettings(int userId, Settings settings) {
        int defaultLocationId = 0;

        try {
            String sql = "SELECT COUNT(*) as total, id FROM `locations` WHERE latitude=" + settings.getLocation().getLatitude()
                    + " AND longitude=" + settings.getLocation().getLongitude() + ";";
            Statement stmt = conn.createStatement();

            ResultSet resultSet = stmt.executeQuery(sql);

            if (resultSet.next() && resultSet.getInt("total") != 0) {
                defaultLocationId = resultSet.getInt("id");
            } else {
                Map<String, Object> locationFields = new HashMap<>();

                locationFields.put("address", settings.getLocation().getAddress());
                locationFields.put("latitude", settings.getLocation().getLatitude());
                locationFields.put("longitude", settings.getLocation().getLongitude());

                stmt.execute( makeInsertSQL("locations", locationFields) );
                defaultLocationId = stmt.getGeneratedKeys().getInt(1);
            }

            sql = "UPDATE settings SET " +
                "lang = \"" + settings.getLang() + "\", " +
                "zoom = " + settings.getZoom() + ", " +
                "default_location_id = " + defaultLocationId + " " +
                "WHERE user_id = " + userId + ";";

            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

//    CREATE TABLE `users` (
//        id INTEGER PRIMARY KEY AUTOINCREMENT,
//        username VARCHAR(30) NOT NULL,
//        email VARCHAR(50) NOT NULL,
//        password VARCHAR(30) NOT NULL
//    );

//    CREATE TABLE `locations` (
//        id INTEGER PRIMARY KEY AUTOINCREMENT,
//        address VARCHAR(60) NOT NULL,
//        latitude DECIMAL(9,6) NOT NULL,
//        longitude DECIMAL(9,6) NOT NULL
//    );

//    CREATE TABLE contacts (
//        id INTEGER PRIMARY KEY AUTOINCREMENT,
//        name VARCHAR(50),
//        surname VARCHAR(50),
//        patronymic VARCHAR(50),
//        email VARCHAR(50),
//        phone VARCHAR(20),
//        mobile VARCHAR(20),
//        image_path VARCHAR,
//        company VARCHAR(50),
//        position VARCHAR(50),
//        birthday DATE,
//        user_id INTEGER NOT NULL,
//        location_id INTEGER,
//        FOREIGN KEY (user_id) REFERENCES `users` (id),
//        FOREIGN KEY (location_id) REFERENCES `locations` (id)
//    );

//    CREATE TABLE `settings` (
//        id INTEGER PRIMARY KEY AUTOINCREMENT,
//        lang VARCHAR(20) NOT NULL,
//        zoom INTEGER(2) NOT NULL,
//        default_location_id INTEGER NOT NULL,
//        user_id INTEGER NOT NULL,
//        FOREIGN KEY (default_location_id) REFERENCES `locations`(id),
//        FOREIGN KEY (user_id) REFERENCES `users` (id)
//        );

// INSERT INTO `locations` (address, latitude, longitude) VALUES ('Беларусь, Минск', 53.8840088, 27.5793043);
// INSERT INTO `settings` (lang, zoom, default_location_id, user_id) VALUES ('ru', 11, 12, 1);