package io.pvardanega.shoppinglist.users;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.pvardanega.shoppinglist.users.UsersRepository.USERS_COLLECTION_NAME;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import java.net.UnknownHostException;
import org.junit.Rule;
import org.junit.Test;
import io.pvardanega.shoppinglist.config.MongoDataInjector;

public class NewAccountIT {

    @Rule
    public MongoDataInjector injector;

    public NewAccountIT() throws UnknownHostException {
        injector = new MongoDataInjector(USERS_COLLECTION_NAME);
    }

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
                body("userId", notNullValue()).
                body("email", equalTo("test@test.fr")).
                body("username", equalTo("norman")).
                body("lists", emptyIterable())
        ;
    }
}
