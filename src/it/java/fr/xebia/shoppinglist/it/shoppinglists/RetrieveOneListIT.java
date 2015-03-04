package fr.xebia.shoppinglist.it.shoppinglists;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.emptyIterable;
import org.junit.Test;
import fr.xebia.shoppinglist.users.User;

public class RetrieveOneListIT {

    @Test
    public void
    should_add_one_product_to_a_list() {
        given().
                body(new User("test@test.fr", "norman", "password")).
                contentType(JSON).
        when().
                post("/api/users").
        then().
                statusCode(201)
        ;

        given().
                body("Romantic dinner").
                contentType(JSON).
        when().
                post("/api/users/1/lists").
        then().
                statusCode(201)
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
                body("products", emptyIterable())
        ;

        given().
                contentType(JSON).
        when().
                delete("/api/users/1").
        then().
                statusCode(200)
        ;
    }
}
