package io.pvardanega.shoppinglist.step_definitions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.inject.Inject;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AccountStepdefs {

    private WebDriver webDriver;

    @Inject
    public AccountStepdefs(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @When("^(\\S+) creates an account$")
    public void user_creates_an_account(String username) throws Throwable {
        webDriver.navigate().to("http://localhost:8080/");
        new WebDriverWait(webDriver, 1).until(presenceOfElementLocated(id("btnSignIn")));
        webDriver.findElement(id("btnSignIn")).click();
        new WebDriverWait(webDriver, 1).until(presenceOfElementLocated(id("formNewAccount")));
        webDriver.findElement(id("username")).sendKeys(username);
        webDriver.findElement(id("email")).sendKeys(username + "@yopmail.com");
        webDriver.findElement(id("password")).sendKeys("password");
        webDriver.findElement(id("btnSubmit")).click();
    }

    @Then("^he is logged in$")
    public void he_she_is_logged_in() throws Throwable {
        new WebDriverWait(webDriver, 1).until(presenceOfElementLocated(id("shopping-lists")));
        assertThat(webDriver.getCurrentUrl()).endsWith("/me");
    }

    @And("^he sees an empty shopping lists$")
    public void he_sees_an_shopping_lists() throws Throwable {
        assertThat(webDriver.findElement(id("shopping-lists"))).isNotNull();
        assertThat(webDriver.findElement(xpath("(//h3)[1]")).getText()).isEqualTo("My shopping lists (0)");
        assertThat(webDriver.findElements(By.className("shopping-list"))).isEmpty();
    }

    @After
    public void closeDriver() {
        webDriver.close();
    }
}
