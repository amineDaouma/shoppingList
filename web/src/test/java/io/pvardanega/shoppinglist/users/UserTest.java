package io.pvardanega.shoppinglist.users;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class UserTest {

    @Test public void
    should_convert_a_user_into_an_entity() {
        UserEntity userEntity = new User(1234L, "email", "username", "password").toEntity();

        assertThat(userEntity.userId).isEqualTo(1234L);
        assertThat(userEntity.email).isEqualTo("email");
        assertThat(userEntity.username).isEqualTo("username");
        assertThat(userEntity.password).isEqualTo("password");
        assertThat(userEntity.lists).isEmpty();
    }
}
