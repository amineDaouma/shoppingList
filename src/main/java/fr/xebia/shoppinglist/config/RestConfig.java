package fr.xebia.shoppinglist.config;

import javax.inject.Inject;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Guice;
import fr.xebia.shoppinglist.Main;

//@ApplicationPath("api")
public class RestConfig extends ResourceConfig {

    @Inject
    public RestConfig(ServiceLocator serviceLocator) {
//        packages("fr.xebia.shoppinglist");
        packages(Main.class.getPackage().getName());
        register(JacksonJsonProvider.class);

        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(Guice.createInjector(new GuiceModule()));
//        Injector injector = Guice.createInjector(new ServletModule() {
//            @Override
//            protected void configureServlets() {
//                bind(UsersResource.class);
//            }
//        });
    }
}
