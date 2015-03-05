package fr.xebia.shoppinglist;

import static org.glassfish.jersey.servlet.ServletProperties.JAXRS_APPLICATION_CLASS;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import fr.xebia.shoppinglist.config.RestConfig;

public class Main {

    public static void main(String[] args) throws Exception{
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        Server server = new Server(Integer.valueOf(webPort));
        ServletContextHandler sch = new ServletContextHandler(server, "/");
        sch.addServlet(DefaultServlet.class,"/").setInitParameter("resourceBase", "src/main/webapp");

        ServletHolder jerseyServletHolder = new ServletHolder(new ServletContainer());
        jerseyServletHolder.setInitParameter(JAXRS_APPLICATION_CLASS, RestConfig.class.getCanonicalName());
        sch.addServlet(jerseyServletHolder, "/api/*");

        server.start();
        server.join();
    }
}
