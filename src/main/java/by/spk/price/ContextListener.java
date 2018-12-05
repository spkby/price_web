package by.spk.price;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {

        // up connect
        JdbcConnection.getInstance();

    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {

        // down connect
        JdbcConnection.destroyConnection();

    }
}
