package io.pvardanega.shoppinglist.users.shoppinglist;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.pvardanega.shoppinglist.users.UsersRepository.USERS_COLLECTION_NAME;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import java.net.UnknownHostException;
import org.junit.Rule;
import org.junit.Test;
import io.pvardanega.shoppinglist.config.MongoDataInjector;
import io.pvardanega.shoppinglist.users.User;

public class NewListsIT {

    @Rule
    public MongoDataInjector injector;

    private final User user = new User("test@test.fr", "username", "password");

    public NewListsIT() throws UnknownHostException {
        injector = new MongoDataInjector(USERS_COLLECTION_NAME, user);
    }

    @Test
    public void should_add_one_new_list_to_an_existing_user() {
        given().
                body("Apero tonight").
                contentType(JSON).
        when().
                post("/api/users/" + user.userId + "/lists").
        then().
                statusCode(201).
                body(matchesJsonSchemaInClasspath("schemas/list.json")).
                body("id", equalTo(1)).
                body("name", equalTo("Apero tonight")).
                body("products", emptyIterable())
        ;
    }
}
