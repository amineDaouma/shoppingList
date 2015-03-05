package fr.xebia.shoppinglist.it.shoppinglists;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import org.junit.Rule;
import org.junit.Test;
import fr.xebia.shoppinglist.it.rules.ListRule;

public class AddProductIT {

    @Rule
    public ListRule listRule = new ListRule();

    @Test
    public void should_add_one_product_to_a_list() {
        given().
                body("Salad").
                contentType(JSON).
        when().
                post("/api/users/1/lists/1/products").
        then().
                statusCode(200).
                body(equalTo("Salad"))
        ;

        given().
                contentType(JSON).
        when().
                get("/api/users/1/lists/1").
        then().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("schemas/list.json")).
                body("id", equalTo(1)).
                body("title", equalTo("Romantic dinner")).
                body("products", hasItems("Salad"))
        ;
    }
}
