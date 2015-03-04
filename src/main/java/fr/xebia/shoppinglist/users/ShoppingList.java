package fr.xebia.shoppinglist.users;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY // mandatory for serialization
)
public class ShoppingList {

    public final Long id;
    public final String title;
    public final List<String> products = new ArrayList<>();

    @JsonCreator
    public ShoppingList(@JsonProperty(value = "title") String title) {
        this(null, title);
    }

    public ShoppingList(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public void addProduct(String product) {
        this.products.add(product);
    }
}
