package io.pvardanega.shoppinglist.users;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import io.pvardanega.shoppinglist.users.shoppinglist.ShoppingList;

@RunWith(MockitoJUnitRunner.class)
public class UsersResourceTest {

    @InjectMocks
    private UsersResource resource;

    @Mock
    private UsersRepository usersRepository;

    @Captor
    private ArgumentCaptor<ShoppingList> newShoppingListCaptor;

    @Captor
    private ArgumentCaptor<Long> userIdCaptor;

    @Test public void
    should_persist_user_and_return_201() {
        User user = new User("test@test.fr", "test", "password");
        UserEntity expectedUserEntity = new UserEntity(12345L, "test@test.fr", "test", "password");
        given(usersRepository.findByEmail(user.email)).willReturn(empty());
        given(usersRepository.create(user)).willReturn(expectedUserEntity);

        Response response = resource.createUser(user);

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getEntity()).isEqualTo(expectedUserEntity.toUser());
    }

    @Test public void
    should_not_create_a_user_when_email_is_already_used() {
        User user = new User("test@test.fr", "test", "password");
        UserEntity expectedUserEntity = new UserEntity(12345L, "test@test.fr", "test", "password");
        given(usersRepository.findByEmail(user.email)).willReturn(of(expectedUserEntity));
        given(usersRepository.create(user)).willReturn(expectedUserEntity);

        Response response = resource.createUser(user);

        verify(usersRepository, never()).addNewListTo(anyLong(), any(ShoppingList.class));
        assertThat(response.getStatus()).isEqualTo(409);
        assertThat(response.getMediaType()).isEqualTo(TEXT_PLAIN_TYPE);
        String errorMessage = (String) response.getEntity();
        assertThat(errorMessage).isEqualTo("Email 'test@test.fr' already used!");
    }

    @Test public void
    should_find_user_and_add_him_a_new_list() {
        Long userId = 12345L;
        UserEntity expectedUserEntity = new UserEntity(userId, "test@test.fr", "test", "password");
        given(usersRepository.get(userId)).willReturn(of(expectedUserEntity));

        Response response = resource.addNewList(userId, "Apéro tonight");

        verify(usersRepository).addNewListTo(userIdCaptor.capture(), newShoppingListCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        ShoppingList createdList = newShoppingListCaptor.getValue();
        assertThat(createdList.name).isEqualTo("Apéro tonight");
        assertThat(createdList.products).isEmpty();
        assertThat(response.getStatus()).isEqualTo(201);
        ShoppingList list = (ShoppingList) response.getEntity();
        assertThat(list.name).isEqualTo("Apéro tonight");
        assertThat(list.products).isEmpty();
    }

    @Test public void
    should_not_add_a_list_if_one_already_exists_with_this_name() {
        Long userId = 12345L;
        UserEntity expectedUserEntity = new UserEntity(userId, "test@test.fr", "test", "password", asList(new ShoppingList("Apéro tonight")));
        given(usersRepository.get(userId)).willReturn(of(expectedUserEntity));

        Response response = resource.addNewList(userId, "Apéro tonight");

        verify(usersRepository, never()).addNewListTo(anyLong(), any(ShoppingList.class));
        assertThat(response.getStatus()).isEqualTo(409);
        assertThat(response.getMediaType()).isEqualTo(TEXT_PLAIN_TYPE);
        String errorMessage = (String) response.getEntity();
        assertThat(errorMessage).isEqualTo("A list with name 'Apéro tonight' already exists!");
    }

    @Test public void
    should_not_add_a_list_if_user_not_found() {
        Long userId = 12345L;
        given(usersRepository.get(userId)).willReturn(empty());

        Response response = resource.addNewList(userId, "Apéro tonight");

        verify(usersRepository, never()).addNewListTo(anyLong(), any(ShoppingList.class));
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMediaType()).isEqualTo(TEXT_PLAIN_TYPE);
        assertThat((String) response.getEntity()).isEqualTo("User with id '12345' not found!");
    }

    @Test public void
    should_add_one_product_to_a_list() {
        // Given
        Long userId = 54321L;
        String listName = "Romantic dinner";

        UserEntity expectedUserEntity = new UserEntity(userId, "test@test.fr", "test", "password", newArrayList(new ShoppingList(listName)));
        given(usersRepository.get(userId)).willReturn(of(expectedUserEntity));

        // When
        Response response = resource.addProductToList(userId, listName, "Salad");

        // Then
        verify(usersRepository).addProductToList(userId, listName, "Salad");
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo("Salad");
        assertThat(response.getMediaType()).isEqualTo(TEXT_PLAIN_TYPE);
    }

    @Test public void
    should_not_add_one_product_to_an_unknown_list() {
        // Given
        Long userId = 54321L;
        String listName = "Romantic dinner";

        UserEntity expectedUserEntity = new UserEntity(userId, "test@test.fr", "test", "password", newArrayList(new ShoppingList(listName)));
        given(usersRepository.get(userId)).willReturn(of(expectedUserEntity));

        // When
        Response response = resource.addProductToList(userId, "unknown", "Salad");

        // Then
        verify(usersRepository, never()).addProductToList(userId, listName, "Salad");
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMediaType()).isEqualTo(TEXT_PLAIN_TYPE);
        assertThat(response.getEntity()).isEqualTo("Cannot add produt 'Salad' to list 'unknown': list not found for user with id '" + userId + "'.");
    }

    @Test public void
    should_not_add_one_product_when_user_is_not_found() {
        // Given
        Long userId = 54321L;
        given(usersRepository.get(userId)).willReturn(empty());

        // When
        Response response = resource.addProductToList(userId, "unknown", "Salad");

        // Then
        verify(usersRepository, never()).addProductToList(userId, "unknown", "Salad");
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMediaType()).isEqualTo(TEXT_PLAIN_TYPE);
        assertThat(response.getEntity()).isEqualTo("User with id '" + userId + "' not found!");
    }

    @Test public void
    should_retrieve_a_list() {
        // Given
        Long userId = 54321L;
        String listName = "Romantic dinner";

        ShoppingList expectedList = new ShoppingList(listName);
        UserEntity expectedUserEntity = new UserEntity(userId, "test@test.fr", "test", "password", newArrayList(expectedList));
        given(usersRepository.get(userId)).willReturn(of(expectedUserEntity));

        // When
        Response response = resource.retrieveList(userId, listName);

        // Then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(expectedList);
    }

    @Test public void
    should_not_retrieve_a_list_if_user_is_not_found() {
        // Given
        Long userId = 54321L;
        given(usersRepository.get(userId)).willReturn(empty());

        // When
        Response response = resource.retrieveList(userId, "any list");

        // Then
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMediaType()).isEqualTo(TEXT_PLAIN_TYPE);
        assertThat((String) response.getEntity()).isEqualTo("User with id '54321' not found!");
    }

    @Test public void
    should_remove_a_user() {
        Response response = resource.removeUser(12345L);

        verify(usersRepository).remove(12345L);
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }
}
