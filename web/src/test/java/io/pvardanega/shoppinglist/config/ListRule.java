package io.pvardanega.shoppinglist.config;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import org.apache.commons.lang3.StringUtils;

public class ListRule extends UserRule {

    private static final String DEFAULT_LIST_NAME = "Default list";
    public final String listName;

    public ListRule() {
        this(null);
    }

    public ListRule(String listName) {
        this.listName = StringUtils.isBlank(listName) ? DEFAULT_LIST_NAME : listName;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        given().
                body(listName).
                contentType(JSON).
        when().
                post("/api/users/" + userId() + "/lists").
        then().
                statusCode(201)
        ;
    }
}
