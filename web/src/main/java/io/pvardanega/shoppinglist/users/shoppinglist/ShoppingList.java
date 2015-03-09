package io.pvardanega.shoppinglist.users.shoppinglist;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY // mandatory for serialization
)
public class ShoppingList {

    public final String name;
    public final List<String> products = new ArrayList<>();

    @JsonCreator
    public ShoppingList(@JsonProperty(value = "name") String name) {
        this.name = name;
    }
}
