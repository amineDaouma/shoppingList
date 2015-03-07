package io.pvardanega.shoppinglist.users;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Test;

public class NewAccountIT {

    private User createdUser;

    @Test
    public void should_create_one_new_account() {
        createdUser =
                given().
                    body(new User("test@test.fr", "norman", "password")).
                    contentType(JSON).
                when().
                        post("/api/users").
                        as(User.class)
        ;

        assertThat(createdUser.userId).isNotNull();
        assertThat(createdUser.email).isEqualTo("test@test.fr");
        assertThat(createdUser.username).isEqualTo("norman");
        assertThat(createdUser.lists).isEmpty();
    }

    @After
    public void removeUser() {
        when().
                delete("/api/users/" + createdUser.userId).
        then().
                statusCode(200)
        ;
    }
}
