package io.pvardanega.shoppinglist.users;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.After;
import org.junit.Test;
import io.pvardanega.shoppinglist.users.User;

public class NewAccountIT {

    @Test
    public void should_create_one_new_account() {
        given().
                body(new User("test@test.fr", "norman", "password")).
                contentType(JSON).
        when().
                post("/api/users").
        then().
                statusCode(201).
                body(matchesJsonSchemaInClasspath("schemas/user.json")).
                body("id", notNullValue()).
                body("email", equalTo("test@test.fr")).
                body("username", equalTo("norman")).
                body("password", equalTo("password")).
                body("lists", emptyIterable())
        ;
    }

    @After
    public void removeUser() {
        when().
                delete("/api/users/1").
        then().
                statusCode(200)
        ;
    }
}
