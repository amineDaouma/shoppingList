package io.pvardanega.shoppinglist.users.shoppinglist;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ShoppingListTest {

    @Test
    public void should_add_a_product_to_the_list() {
        ShoppingList list = new ShoppingList(1234L, "MyList");

        list.addProduct("Salad");

        assertThat(list.products).hasSize(1).containsExactly("Salad");
    }
}
