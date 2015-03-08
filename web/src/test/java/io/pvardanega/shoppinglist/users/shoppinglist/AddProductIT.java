package io.pvardanega.shoppinglist.users.shoppinglist;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.http.ContentType.TEXT;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import org.junit.Rule;
import org.junit.Test;
import io.pvardanega.shoppinglist.config.ListRule;

public class AddProductIT {

    @Rule
    public ListRule listRule = new ListRule();

    @Test
    public void should_add_one_product_to_a_list() {
        given().
                body("Salad").
                contentType(JSON).
        when().
                put("/api/users/" + listRule.userId() + "/lists/" + listRule.listName + "/products").
        then().
                statusCode(200).
                contentType(TEXT).
                body(equalTo("Salad"))
        ;

        given().
                contentType(JSON).
        when().
                get("/api/users/" + listRule.userId() + "/lists/" + listRule.listName).
        then().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("schemas/list.json")).
                body("name", equalTo(listRule.listName)).
                body("products", hasItems("Salad"))
        ;
    }
}
