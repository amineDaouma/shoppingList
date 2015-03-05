package io.pvardanega.shoppinglist.it.shoppinglists;

import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import org.junit.Rule;
import org.junit.Test;
import io.pvardanega.shoppinglist.it.rules.ListRule;

public class RetrieveAllListsIT {

    @Rule
    public ListRule listRule = new ListRule();

    @Test
    public void should_retieve_all_lists() {
        when().
                get("/api/users/1/lists").
        then().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("schemas/lists.json"))
        ;
    }
}
