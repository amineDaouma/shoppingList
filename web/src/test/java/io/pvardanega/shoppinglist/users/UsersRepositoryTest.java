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

    @Test
    public void should_insert_user_in_DB_and_generate_ids() {
        User user = new User("test@test.fr", "username", "password");
        assertThat(user.userId).isNull();
        assertThat(user._id).isNull();

        User createdUser = repository.create(user);

        assertThat(createdUser.userId).isGreaterThan(0L);
        assertThat(createdUser._id).isNotNull();
        assertThat(createdUser).isEqualToComparingOnlyGivenFields(user, "email", "username");
    }

    @Test
    public void should_retrieve_a_user_by_its_id() {
        User createdUser = repository.create(new User(1234L, "test@test.fr", "test", "password"));

        User user = repository.get(createdUser.userId);

        assertThat(user).isEqualTo(createdUser);
    }

    @Test
    public void should_retrieve_a_user_by_its_object_id() {
        User createdUser = repository.create(new User("test@test.fr", "test", "password"));

        User user = repository.get(createdUser._id);

        assertThat(user).isEqualTo(createdUser);
    }

    @Test
    public void should_remove_a_user() {
        User createdUser = repository.create(new User("test@test.fr", "test", "password"));

        repository.remove(createdUser.userId);

        User user = repository.get(createdUser._id);
        assertThat(user).isNull();
    }
}
