package io.pvardanega.shoppinglist.users;

import static io.pvardanega.shoppinglist.users.UsersRepository.COUNTERS_COLLECTION_NAME;
import static io.pvardanega.shoppinglist.users.UsersRepository.USERS_COLLECTION_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DuplicateKeyException;
import io.pvardanega.shoppinglist.config.MongoClient;
import io.pvardanega.shoppinglist.users.shoppinglist.ShoppingList;

@RunWith(MockitoJUnitRunner.class)
public class UsersRepositoryTest {

    @InjectMocks private UsersRepository repository;
    @Mock private MongoClient mongoClient;
    private Jongo jongo;

    @Before
    public void initFongo() {
        jongo = new Jongo(new Fongo("Test").getDB("shoppinglist"));
        given(mongoClient.getCollection(USERS_COLLECTION_NAME)).willReturn(jongo.getCollection(USERS_COLLECTION_NAME));
        given(mongoClient.getCollection(COUNTERS_COLLECTION_NAME)).willReturn(jongo.getCollection(COUNTERS_COLLECTION_NAME));
    }

    @Test public void
    should_compute_ids_and_insert_user_in_DB() {
        User user = new User("test@test.fr", "username", "password");

        UserEntity createdUserEntity = repository.create(user);

        assertThat(createdUserEntity.userId).isEqualTo(1L);
        assertThat(createdUserEntity._id).isNotNull();
        assertThat(createdUserEntity.email).isEqualTo(user.email);
        assertThat(createdUserEntity.username).isEqualTo(user.username);
        assertThat(createdUserEntity.lists).isEmpty();
    }

    @Test public void
    should_increment_userid_sequentially() {
        User user1 = new User("test1@test.fr", "username", "password");
        User user2 = new User("test2@test.fr", "username", "password");
        User user3 = new User("test3@test.fr", "username", "password");

        UserEntity createdUserEntity1 = repository.create(user1);
        UserEntity createdUserEntity2 = repository.create(user2);
        UserEntity createdUserEntity3 = repository.create(user3);

        assertThat(createdUserEntity1.userId).isEqualTo(1L);
        assertThat(createdUserEntity2.userId).isEqualTo(2L);
        assertThat(createdUserEntity3.userId).isEqualTo(3L);
    }

    @Test(expected = DuplicateKeyException.class) public void
    should_throw_exception_when_creating_a_user_with_already_existing_email() {
        jongo.getCollection(USERS_COLLECTION_NAME).getDBCollection().createIndex(new BasicDBObject("email", 1), new BasicDBObject("unique", true));
        User user1 = new User("test@test.fr", "username", "password");
        User user2 = new User("test@test.fr", "username", "password");

        repository.create(user1);
        repository.create(user2);
    }

    @Test public void
    should_retrieve_a_user_by_its_id() {
        UserEntity createdUserEntity = repository.create(new User("test@test.fr", "test", "password"));

        Optional<UserEntity> userEntity = repository.get(createdUserEntity.userId);

        assertThat(userEntity.isPresent()).isTrue();
        assertThat(userEntity.get()).isEqualTo(createdUserEntity);
    }

    @Test public void
    should_not_retrieve_a_user_when_searching_with_an_unknwon_id() {
        repository.create(new User(1234L, "test@test.fr", "test", "password"));

        Optional<UserEntity> userEntity = repository.get(98765L);

        assertThat(userEntity.isPresent()).isFalse();
    }

    @Test public void
    should_retrieve_a_user_by_its_object_id() {
        UserEntity createdUserEntity = repository.create(new User("test@test.fr", "test", "password"));

        Optional<UserEntity> userEntity = repository.get(createdUserEntity._id);

        assertThat(userEntity.isPresent()).isTrue();
        assertThat(userEntity.get()).isEqualTo(createdUserEntity);
    }

    @Test public void
    should_not_retrieve_a_user_when_searching_with_an_unknwon_object_id() {
        repository.create(new User("test@test.fr", "test", "password"));

        Optional<UserEntity> userEntity = repository.get(new ObjectId());

        assertThat(userEntity.isPresent()).isFalse();
    }

    @Test public void
    should_add_one_new_list_into_user_s_lists() {
        UserEntity userEntity = repository.create(new User("email", "username", "password"));
        assertThat(userEntity.lists).isEmpty();

        repository.addNewListTo(userEntity.userId, new ShoppingList("list1"));

        Optional<UserEntity> updatedUser = repository.get(userEntity.userId);
        assertThat(updatedUser.get().lists).hasSize(1).extracting("name").containsExactly("list1");
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

        ShoppingList list1 = repository.get(userId).get().lists.stream().filter(l -> l.name.equals(listName)).findFirst().get();
        ShoppingList list2 = repository.get(userId).get().lists.stream().filter(l -> l.name.equals(listName2)).findFirst().get();
        assertThat(list1.products).containsOnly(champagne, chips);
        assertThat(list2.products).containsOnly(chips);
    }

    @Test public void
    should_remove_a_user() {
        UserEntity createdUserEntity = repository.create(new User("test@test.fr", "test", "password"));

        repository.remove(createdUserEntity.userId);

        Optional<UserEntity> userEntity = repository.get(createdUserEntity._id);
        assertThat(userEntity.isPresent()).isFalse();
    }
}
