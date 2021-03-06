package io.pvardanega.shoppinglist.users.shoppinglist;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.emptyIterable;
import org.junit.Rule;
import org.junit.Test;
import io.pvardanega.shoppinglist.config.ListRule;

public class RetrieveOneListIT {

    @Rule
    public ListRule listRule = new ListRule("Romantic dinner");

    @Test
    public void should_retrieve_one_list() {
        given().
                contentType(JSON).
        when().
                get("/api/users/" + listRule.userId() + "/lists/" + listRule.listName).
        then().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("schemas/list.json")).
                body("name", equalTo("Romantic dinner")).
                body("products", emptyIterable())
        ;
    }
}
