package by.spk.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class Utils {
    private static final String PROPERTIES_FILE_PATH = "price.properties";
    private static final String VERSION_FILE_PATH = "version.properties";
    private static Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static String getPropertiesValue(final String key) {
        try {
            java.util.Properties properties = new java.util.Properties();
            properties.load(Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH)));
            final String value = properties.getProperty(key);
            if (value == null) {
                throw new IllegalArgumentException("not found property with name: " + key);
            }
            return value;
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    public static String getVersion() {
        try {
            java.util.Properties properties = new java.util.Properties();
            properties.load(Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(VERSION_FILE_PATH)));

            final String version = properties.getProperty("version");
            if (version == null) {
                throw new IllegalArgumentException("not found 'version' in " + VERSION_FILE_PATH);
            }
            final String date = properties.getProperty("date");
            if (date == null) {
                throw new IllegalArgumentException("not found 'date' in " + VERSION_FILE_PATH);
            }
            return version + " (" + date + ")";

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalStateException(e);
        }
    }
}
