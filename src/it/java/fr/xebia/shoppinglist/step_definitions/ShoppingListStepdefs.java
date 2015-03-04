package fr.xebia.shoppinglist.step_definitions;

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

    private WebDriver webDriver;
    private String listTitle;

    @Inject
    public ShoppingListStepdefs(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Given("^(\\S+), an existing user$")
    public void Claire_is_logged_in(String username) throws Throwable {
        webDriver.navigate().to("http://localhost:8080/");
        new WebDriverWait(webDriver, 1).until(presenceOfElementLocated(id("btnSignIn")));
        webDriver.findElement(id("btnSignIn")).click();
        new WebDriverWait(webDriver, 1).until(presenceOfElementLocated(id("formNewAccount")));
        webDriver.findElement(id("username")).sendKeys(username);
        webDriver.findElement(id("email")).sendKeys(username + "@yopmail.com");
        webDriver.findElement(id("password")).sendKeys("password");
        webDriver.findElement(id("btnSubmit")).click();
        new WebDriverWait(webDriver, 1).until(presenceOfElementLocated(id("shopping-lists")));
        assertThat(webDriver.getCurrentUrl()).endsWith("/me");
    }

    @When("^she creates a new list with title '(.*)'$")
    public void she_creates_a_new_list_with_title(String listTitle) throws Throwable {
        this.listTitle = listTitle;
        webDriver.findElement(id("newListTitle")).sendKeys(this.listTitle);
        webDriver.findElement(id("btnAddNewList")).click();
    }

    @When("^she adds '(.*)' in the list$")
    public void she_adds_a_product_in_the_list(String product) throws Throwable {
        new WebDriverWait(webDriver, SECONDS.toSeconds(1L)).until(presenceOfElementLocated(className("shopping-list")));
        webDriver.findElement(linkText(listTitle)).click();
        assertThat(webDriver.findElement(id("products"))).isNotNull();

        webDriver.findElement(id("newProduct")).sendKeys(product);
        webDriver.findElement(id("btnAddProduct")).click();
    }

    @Then("^she sees the new list in her shopping lists$")
    public void she_sees_the_new_list_in_her_shopping_lists() throws Throwable {
        new WebDriverWait(webDriver, 1).until(presenceOfElementLocated(className("shopping-list")));
        assertThat(webDriver.findElement(id("shopping-lists"))).isNotNull();
        assertThat(webDriver.findElement(xpath("(//h3)[1]")).getText()).isEqualTo("My shopping lists (1)");
        assertThat(webDriver.findElements(className("shopping-list"))).hasSize(1);
        WebElement createdList = webDriver.findElements(className("shopping-list")).get(0);
        assertThat(createdList.getText()).isEqualTo(listTitle);
    }

    @Then("^the list contains the product '(.*)'$")
    public void the_list_contains_the_product(String product) throws Throwable {
        assertThat(webDriver.findElement(id("products"))).isNotNull();
        assertThat(webDriver.findElement(xpath("(//h3)[1]")).getText()).isEqualTo(listTitle + " (1)");
        assertThat(webDriver.findElements(className("product"))).hasSize(1);
        WebElement existingProduct = webDriver.findElements(className("product")).get(0);
        assertThat(existingProduct.getText()).isEqualTo(product);
    }

    @After
    public void closeDriver() {
        webDriver.close();
    }
}
