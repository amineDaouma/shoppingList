package io.pvardanega.shoppinglist.step_definitions;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.inject.Inject;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ShoppingListStepdefs {
    
    private final WebDriverWait WAIT_ONE_SECOND;

    private WebDriver webDriver;
    private AccountStepdefs accountStepdefs;
    private String listName;

    @Inject
    public ShoppingListStepdefs(AccountStepdefs accountStepdefs) {
        this.accountStepdefs = accountStepdefs;
        this.webDriver = accountStepdefs.webDriver;
        WAIT_ONE_SECOND = new WebDriverWait(webDriver, SECONDS.toSeconds(1L));
    }

    @Given("^(\\S+), an existing user$")
    public void he_she_is_an_existing_user(String username) throws Throwable {
        accountStepdefs.user_creates_an_account(username);
        accountStepdefs.he_she_is_logged_in();
    }

    @When("^she creates a new list with name '(.*)'$")
    public void she_creates_a_new_list_with_name(String listName) throws Throwable {
        this.listName = listName;
        webDriver.findElement(id("newListName")).sendKeys(this.listName);
        webDriver.findElement(id("btnAddNewList")).click();
    }

    @When("^she adds '(.*)' in the list$")
    public void she_adds_a_product_in_the_list(String product) throws Throwable {
        WAIT_ONE_SECOND.until(presenceOfElementLocated(className("shopping-list")));
        webDriver.findElement(linkText(listName)).click();
        WAIT_ONE_SECOND.until(presenceOfElementLocated(id("newProduct")));
        assertThat(webDriver.findElement(className("products"))).isNotNull();

        webDriver.findElement(id("newProduct")).sendKeys(product);
        webDriver.findElement(id("btnAddProduct")).click();
    }

    @Then("^she sees the new list in her shopping lists$")
    public void she_sees_the_new_list_in_her_shopping_lists() throws Throwable {
        WAIT_ONE_SECOND.until(presenceOfElementLocated(className("shopping-list")));
        assertThat(webDriver.findElement(id("shopping-lists"))).isNotNull();
        assertThat(webDriver.findElement(xpath("(//h3)[1]")).getText()).isEqualTo("My shopping lists (1)");
        assertThat(webDriver.findElements(className("shopping-list"))).hasSize(1);
        WebElement createdList = webDriver.findElements(className("shopping-list")).get(0);
        assertThat(createdList.getText()).isEqualTo(listName);
    }

    @Then("^the list contains the product '(.*)'$")
    public void the_list_contains_the_product(String product) throws Throwable {
        assertThat(webDriver.findElement(className("products"))).isNotNull();
        assertThat(webDriver.findElement(xpath("(//h3)[1]")).getText()).isEqualTo(listName + " (1)");
        assertThat(webDriver.findElements(className("product"))).hasSize(1);
        WebElement existingProduct = webDriver.findElements(className("product")).get(0);
        assertThat(existingProduct.getText()).isEqualTo(product);
    }

    @After
    public void closeDriver() {
        webDriver.close();
    }
}
