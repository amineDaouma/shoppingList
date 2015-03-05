package fr.xebia.shoppinglist.config;

import com.google.inject.servlet.ServletModule;
import fr.xebia.shoppinglist.users.UsersResource;

public class GuiceModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(UsersResource.class);
    }
}
