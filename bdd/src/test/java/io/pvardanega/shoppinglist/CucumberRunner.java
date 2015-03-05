package io.pvardanega.shoppinglist;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = "pretty", features = "src/test/resources/features", glue = { "io.pvardanega.shoppinglist.step_definitions" })
public class CucumberRunner {

}
