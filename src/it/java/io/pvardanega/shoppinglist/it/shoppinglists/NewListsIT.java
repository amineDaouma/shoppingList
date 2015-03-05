package io.pvardanega.shoppinglist.it.shoppinglists;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Rule;
import org.junit.Test;
import io.pvardanega.shoppinglist.it.rules.UserRule;

public class NewListsIT {

    @Rule
    public UserRule userRule = new UserRule();

    @Test
    public void should_add_one_new_list_to_an_existing_user() {
        given().
                body("Apero tonight").
                contentType(JSON).
        when().
                post("/api/users/1/lists").
        then().
                statusCode(201).
                body(matchesJsonSchemaInClasspath("schemas/list.json")).
                body("id", equalTo(1)).
                body("name", equalTo("Apero tonight")).
                body("products", emptyIterable())
        ;
    }
}
