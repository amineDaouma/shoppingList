package fr.xebia.shoppinglist.it.shoppinglists;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Rule;
import org.junit.Test;
import fr.xebia.shoppinglist.it.rules.ListRule;

public class AddProductIT {

    @Rule
    public ListRule listRule = new ListRule();

    @Test
    public void should_add_one_product_to_a_list() {
        given().
                body("Salad").
                contentType(JSON).
        when().
                post("/api/users/1/lists/1/products").
        then().
                statusCode(200).
                body(equalTo("Salad"))
        ;
    }
}
