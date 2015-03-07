package io.pvardanega.shoppinglist.users;

import static io.pvardanega.shoppinglist.users.UsersRepository.USERS_COLLECTION_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.github.fakemongo.Fongo;
import io.pvardanega.shoppinglist.config.MongoClient;
import io.pvardanega.shoppinglist.users.shoppinglist.ShoppingList;

@RunWith(MockitoJUnitRunner.class)
public class UsersRepositoryTest {

    @InjectMocks private UsersRepository repository;
    @Mock private MongoClient mongoClient;

    private final Fongo fongo = new Fongo("Test");
    private MongoCollection usersCollection;

    @Before
    public void initFongo() {
        usersCollection = new Jongo(fongo.getDB("shoppinglist")).getCollection(USERS_COLLECTION_NAME);
        given(mongoClient.getCollection(USERS_COLLECTION_NAME)).willReturn(usersCollection);
    }

    @After
    public void clearSlotsCollection() {
        usersCollection.drop();
    }

    @Test public void
    should_insert_user_in_DB_and_generate_ids() {
        User user = new User("test@test.fr", "username", "password");

        UserEntity createdUserEntity = repository.create(user);

        assertThat(createdUserEntity.userId).isGreaterThan(0L);
        assertThat(createdUserEntity._id).isNotNull();
        assertThat(createdUserEntity.email).isEqualTo(user.email);
        assertThat(createdUserEntity.username).isEqualTo(user.username);
        assertThat(createdUserEntity.lists).isEmpty();
    }

    @Test public void
    should_retrieve_a_user_by_its_id() {
        UserEntity createdUserEntity = repository.create(new User(1234L, "test@test.fr", "test", "password"));

        UserEntity userEntity = repository.get(createdUserEntity.userId);

        assertThat(userEntity).isEqualTo(createdUserEntity);
    }

    @Test public void
    should_retrieve_a_user_by_its_object_id() {
        UserEntity createdUserEntity = repository.create(new User("test@test.fr", "test", "password"));

        UserEntity userEntity = repository.get(createdUserEntity._id);

        assertThat(userEntity).isEqualTo(createdUserEntity);
    }

    @Test public void
    should_add_one_new_list_into_user_s_lists() {
        UserEntity userEntity = repository.create(new User("email", "username", "password"));
        assertThat(userEntity.lists).isEmpty();

        repository.addNewListTo(userEntity.userId, new ShoppingList("list1"));

        userEntity = repository.get(userEntity.userId);
        assertThat(userEntity.lists).hasSize(1).extracting("name").containsExactly("list1");
    }

    @Test public void
    should_add_product_to_list() {
        String listName = "list1";
        String listName2 = "list2";
        String champagne = "Champagne";
        String chips = "chips";
        UserEntity userEntity = repository.create(new User("email", "username", "password"));
        Long userId = userEntity.userId;
        assertThat(userEntity.lists).isEmpty();
        repository.addNewListTo(userEntity.userId, new ShoppingList(listName));
        repository.addNewListTo(userEntity.userId, new ShoppingList(listName2));

        repository.addProductToList(userId, listName, champagne);
        repository.addProductToList(userId, listName2, chips);
        repository.addProductToList(userId, listName, chips);

        ShoppingList list1 = repository.get(userId).lists.stream().filter(l -> l.name.equals(listName)).findFirst().get();
        ShoppingList list2 = repository.get(userId).lists.stream().filter(l -> l.name.equals(listName2)).findFirst().get();
        assertThat(list1.products).containsOnly(champagne, chips);
        assertThat(list2.products).containsOnly(chips);
    }

    @Test public void
    should_remove_a_user() {
        UserEntity createdUserEntity = repository.create(new User("test@test.fr", "test", "password"));

        repository.remove(createdUserEntity.userId);

        UserEntity userEntity = repository.get(createdUserEntity._id);
        assertThat(userEntity).isNull();
    }
}
