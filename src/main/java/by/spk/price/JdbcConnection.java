package by.spk.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class JdbcConnection {

    private static Logger LOGGER = LoggerFactory.getLogger(JdbcConnection.class);
    private static Connection instance;

    private JdbcConnection() {
    }

    public static Connection getInstance() {
        buildConnection();
        return instance;
    }

    private static void buildConnection() {

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
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                throw new IllegalStateException("Error on destroy connection");
            }
        }
    }

    public static void close(final Statement statement, final ResultSet resultSet) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalStateException("Error close statement and resultSet");
        }
    }
}
