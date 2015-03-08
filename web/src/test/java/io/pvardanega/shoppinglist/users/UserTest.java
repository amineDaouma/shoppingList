package io.pvardanega.shoppinglist.users;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class UserTest {

    @Test public void
    should_instanciate_a_user() {
        User expectedUser = new User(12345L, "email@test.fr", "username", "password", newArrayList());

        User user = new User(12345L, "email@test.fr", "username", "password", newArrayList());

        assertThat(user).isEqualToComparingFieldByField(expectedUser);
    }

    @Test public void
    should_instanciate_a_user_with_a_null_list_of_lists() {
        User expectedUser = new User(12345L, "email@test.fr", "username", "password", newArrayList());

        User user = new User(12345L, "email@test.fr", "username", "password", null);

        assertThat(user).isEqualToComparingFieldByField(expectedUser);
    }

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
