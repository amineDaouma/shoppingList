package io.pvardanega.shoppinglist.config;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import io.pvardanega.shoppinglist.users.UsersResource;

@ApplicationPath("api")
public class RestConfig extends ResourceConfig {

    @Inject
    public RestConfig(ServiceLocator serviceLocator) {
        packages("io.pvardanega.shoppinglist");
        register(JacksonJsonProvider.class);

        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);

        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);

        Injector injector = Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(UsersResource.class);
            }
        });

        guiceBridge.bridgeGuiceInjector(injector);
    }
}
