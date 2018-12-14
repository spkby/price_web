package by.spk.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class JdbcConnection {

    private static Logger LOGGER = LoggerFactory.getLogger(JdbcConnection.class);
    private static Connection instance;
    private static long timeOut = Long.parseLong(Utils.getPropertiesValue("db.timeout")) * 1000;
    private static long lastSession = System.currentTimeMillis();

    private JdbcConnection() {
    }

    public static Connection getInstance() {
        buildConnection();
        return instance;
    }

    private static void buildConnection() {
        if ((System.currentTimeMillis() - lastSession) > timeOut) {
            destroyConnection();
        }
        lastSession = System.currentTimeMillis();
        if (instance != null) {
            return;
        }

        try {
            Class.forName(Utils.getPropertiesValue("db.drv"));
            LOGGER.info("create connection");
            instance = DriverManager.getConnection(
                    Utils.getPropertiesValue("db.url"),
                    Utils.getPropertiesValue("db.user"),
                    Utils.getPropertiesValue("db.pass")
            );
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalStateException("Error creating connection");
        }
    }

    public static void destroyConnection() {
        if (instance != null) {
            try {
                instance.close();
                instance = null;
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                throw new IllegalStateException("Error on destroy connection");
            }
        }
    }
}
