package io.pvardanega.shoppinglist.users;

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
    public final String name;
    public final List<String> products = new ArrayList<>();

    @JsonCreator
    public ShoppingList(@JsonProperty(value = "name") String name) {
        this(null, name);
    }

    public ShoppingList(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addProduct(String product) {
        this.products.add(product);
    }
}
