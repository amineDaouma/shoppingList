package io.pvardanega.shoppinglist.it.rules;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import org.junit.rules.ExternalResource;
import io.pvardanega.shoppinglist.users.User;

public class UserRule extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        given().
                body(new User("test@test.fr", "norman", "password")).
                contentType(JSON).
        when().
                post("/api/users").
        then().
                statusCode(201)
        ;
    }

    @Override
    protected void after() {
        when().
                delete("/api/users/1").
        then().
                statusCode(200)
        ;
    }
}
