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

        sce.getServletContext().setAttribute("ver", Utils.getVersion());
        sce.getServletContext().setAttribute("urlDomain", Utils.getPropertiesValue("web.url.domain"));
        sce.getServletContext().setAttribute("urlSearch", Utils.getPropertiesValue("web.url.search"));

    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {

        // down connect
        JdbcConnection.destroyConnection();

    }
}
