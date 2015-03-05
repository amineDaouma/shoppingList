package io.pvardanega.shoppinglist;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.google.inject.Binder;
import com.google.inject.Module;

public class GuiceATConfig implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(WebDriver.class).to(FirefoxDriver.class);
    }
}
