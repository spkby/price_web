package by.spk.price;

import java.io.IOException;

public class Utils {
    private static final String PROPERTIES_FILE_PATH = "price.properties";
    private static final String VERSION_FILE_PATH = "version.properties";

    public static String getPropertiesValue(final String key) {
        try {
            java.util.Properties properties = new java.util.Properties();
            properties.load(Utils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH));
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getVersion() {
        try {
            java.util.Properties properties = new java.util.Properties();
            properties.load(Utils.class.getClassLoader().getResourceAsStream(VERSION_FILE_PATH));
            return properties.getProperty("version") + " (" + properties.getProperty("date") + ")";
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
