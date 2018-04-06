package by.spk.price;

import by.spk.price.Web.WebDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Utils {
    private static final String PROPERTIES_FILE_PATH = "price.properties";
    private static final String VERSION_FILE_PATH = "version.properties";

    private static Connection openConnection() throws IOException, SQLException {

        String url = Utils.getPropertiesValue("db_url");
        String username = Utils.getPropertiesValue("db_username");
        String password = Utils.getPropertiesValue("db_password");

        return DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            Connection connection = openConnection();

            connection.setAutoCommit(true);

            return connection;

        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static WebDAO getDAO() {
        return new WebDAO(getConnection());
    }

    public static String getPropertiesValue(String key) throws IOException {
        java.util.Properties properties = new java.util.Properties();
        properties.load(Utils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH));
        return properties.getProperty(key);
    }

    public static String getVersion() throws IOException {
        java.util.Properties properties = new java.util.Properties();
        properties.load(Utils.class.getClassLoader().getResourceAsStream(VERSION_FILE_PATH));
        return properties.getProperty("version") + " (" + properties.getProperty("date") + ")";
    }
}
