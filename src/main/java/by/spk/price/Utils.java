package by.spk.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Utils {
    private static final String PROPERTIES_FILE_PATH = "price.properties";
    private static final String VERSION_FILE_PATH = "version.properties";
    private static Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static String getPropertiesValue(final String key) {
        try {
            java.util.Properties properties = new java.util.Properties();
            properties.load(Utils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH));
            return properties.getProperty(key);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    public static String getVersion() {
        try {
            java.util.Properties properties = new java.util.Properties();
            properties.load(Utils.class.getClassLoader().getResourceAsStream(VERSION_FILE_PATH));
            return properties.getProperty("version") + " (" + properties.getProperty("date") + ")";
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalStateException(e);
        }
    }
}
