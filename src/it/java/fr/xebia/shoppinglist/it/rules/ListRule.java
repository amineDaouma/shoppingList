package fr.xebia.shoppinglist.it.rules;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;

public class ListRule extends UserRule {

    @Override
    protected void before() throws Throwable {
        super.before();
        given().
                body("Romantic dinner").
                contentType(JSON).
        when().
                post("/api/users/1/lists").
        then().
                statusCode(201)
        ;
    }
}
