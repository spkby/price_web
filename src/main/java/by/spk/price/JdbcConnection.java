package by.spk.price;

import java.sql.*;

public class JdbcConnection {

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
            instance = DriverManager.getConnection(
                    Utils.getPropertiesValue("db.url"),
                    Utils.getPropertiesValue("db.user"),
                    Utils.getPropertiesValue("db.pass")
            );
        } catch (ClassNotFoundException | SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void destroyConnection() {
        if (instance != null) {
            try {
                instance.close();
            } catch (SQLException e) {
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
            throw new IllegalStateException(e);
        }
    }
}
