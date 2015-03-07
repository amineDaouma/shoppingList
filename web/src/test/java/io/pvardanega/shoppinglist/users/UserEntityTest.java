package io.pvardanega.shoppinglist.users;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.Test;
import io.pvardanega.shoppinglist.users.shoppinglist.ShoppingList;

public class UserEntityTest {

    @Test public void
    should_transform_a_user_entity_into_user() {
        ShoppingList list1 = new ShoppingList("List1");
        ShoppingList list2 = new ShoppingList("List2");
        List<ShoppingList> lists = asList(list1, list2);

        User user = new UserEntity(1234L, "email", "username", "password", lists).toUser();

        assertThat(user.userId).isEqualTo(1234L);
        assertThat(user.email).isEqualTo("email");
        assertThat(user.username).isEqualTo("username");
        assertThat(user.password).isNull();
        assertThat(user.lists).containsOnly(list1, list2);
    }
}
