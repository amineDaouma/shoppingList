package fr.xebia.shoppinglist.it.shoppinglists;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Rule;
import org.junit.Test;
import fr.xebia.shoppinglist.it.rules.ListRule;

public class RetrieveAllListsIT {

    @Rule
    public ListRule listRule = new ListRule();

    @Test
    public void should_retieve_all_lists() {
        when().
                get("/api/users/1/lists").
        then().
                statusCode(200).
                body(equalTo("[{\"title\":\"Romantic dinner\",\"id\":1,\"products\":[]}]"))
        ;
    }
}
