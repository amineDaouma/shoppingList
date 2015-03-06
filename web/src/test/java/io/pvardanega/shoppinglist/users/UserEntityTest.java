package io.pvardanega.shoppinglist.users;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class UserEntityTest {

    @Test
    public void
    should_transform_a_user_entity_into_user() {
        User user = new UserEntity(1234L, "email", "username", "password").toUser();

        assertThat(user.userId).isEqualTo(1234L);
        assertThat(user.email).isEqualTo("email");
        assertThat(user.username).isEqualTo("username");
        assertThat(user.password).isNull();
    }
}
