package io.pvardanega.shoppinglist.users;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.google.common.collect.Lists;
import io.pvardanega.shoppinglist.users.shoppinglist.ShoppingList;

@RunWith(MockitoJUnitRunner.class)
public class UsersResourceTest {

    @InjectMocks
    private UsersResource resource;

    @Mock
    private UsersRepository usersRepository;

    @Test
    public void should_persist_user_and_return_201() {
        User user = new User("test@test.fr", "test", "password");
        UserEntity expectedUserEntity = new UserEntity(12345L, "test@test.fr", "test", "password");
        given(usersRepository.create(user)).willReturn(expectedUserEntity);

        Response response = resource.createUser(user);

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getEntity()).isEqualTo(expectedUserEntity.toUser());
    }

    @Test
    public void should_find_user_and_add_him_a_new_list() {
        long userId = 12345L;

        UserEntity expectedUserEntity = new UserEntity(userId, "test@test.fr", "test", "password");
        given(usersRepository.get(userId)).willReturn(expectedUserEntity);

        Response response = resource.addNewList(userId, "Apéro tonight");

        assertThat(response.getStatus()).isEqualTo(201);
        ShoppingList list = (ShoppingList) response.getEntity();
        assertThat(list.id).isEqualTo(1L);
        assertThat(list.name).isEqualTo("Apéro tonight");
    }

    @Test
    public void should_retrieve_all_user_lists() {
        Long userId = 12345L;

        UserEntity expectedUserEntity = new UserEntity(userId, "test@test.fr", "test", "password");
        ShoppingList list1 = new ShoppingList(345L, "list1");
        expectedUserEntity.lists.add(list1);
        ShoppingList list2 = new ShoppingList(346L, "list2");
        expectedUserEntity.lists.add(list2);
        given(usersRepository.get(userId)).willReturn(expectedUserEntity);

        Response response = resource.retrieveAllLists(userId);

        List<ShoppingList> lists = (List<ShoppingList>) response.getEntity();
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(lists).hasSize(2).containsOnly(list1, list2);
    }

    @Test
    public void should_remove_a_user() {
        Response response = resource.removeUser(12345L);

        verify(usersRepository).remove(12345L);
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }

    @Test
    public void should_add_one_product_to_a_list() {
        // Given
        Long userId = 54321L;
        Long listId = 132L;

        UserEntity expectedUserEntity = new UserEntity(userId, "test@test.fr", "test", "password", Lists.newArrayList(new ShoppingList(listId, "Romantic dinner")));
        given(usersRepository.get(userId)).willReturn(expectedUserEntity);

        // When
        Response response = resource.addProductToList(userId, listId, "Salad");

        // Then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo("Salad");
    }

    @Test
    public void should_retrieve_a_list() {
        // Given
        Long userId = 54321L;
        Long listId = 132L;

        ShoppingList expectedList = new ShoppingList(listId, "Romantic dinner");
        UserEntity expectedUserEntity = new UserEntity(userId, "test@test.fr", "test", "password", Lists.newArrayList(expectedList));
        given(usersRepository.get(userId)).willReturn(expectedUserEntity);

        // When
        Response response = resource.retrieveList(userId, listId);

        // Then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(expectedList);
    }
}
