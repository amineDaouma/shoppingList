package io.pvardanega.shoppinglist.config;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import org.junit.rules.ExternalResource;
import io.pvardanega.shoppinglist.users.User;

public class UserRule extends ExternalResource {

    private final User user;
    private User createdUser;

    public UserRule() {
        this.user = new User("test@free.fr", "username", "password");
    }

    @Override
    protected void before() throws Throwable {
        createdUser =
            given().
                body(user).
                contentType(JSON).
            when().
                post("/api/users").
                as(User.class)
        ;
    }

    @Override
    protected void after() {
        when().
                delete("/api/users/" + createdUser.userId).
        then().
                statusCode(200)
        ;
    }

    public Long userId() {
        return createdUser.userId;
    }
}
