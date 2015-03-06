package io.pvardanega.shoppinglist.users;

import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import com.google.inject.Singleton;
import io.pvardanega.shoppinglist.config.MongoClient;

@Singleton
public class UsersRepository {

    public static final String USERS_COLLECTION_NAME = "users";
    private AtomicInteger counter = new AtomicInteger(0);
    private MongoClient mongoClient;

    @Inject
    public UsersRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public UserEntity create(User user) {
        UserEntity userToCreate = user.toEntity();
        getUsersCollection().insert(userToCreate);
        Long userId = (long) counter.incrementAndGet();
        getUsersCollection()
                .update(userToCreate._id)
                .with("{$set: {userId: #}}", userId);
        return get(userToCreate._id);
    }

    public UserEntity get(Long userId) {
        return getUsersCollection().findOne("{userId: #}", userId).as(UserEntity.class);
    }

    public UserEntity get(ObjectId id) {
        return getUsersCollection().findOne(id).as(UserEntity.class);
    }

    public void remove(Long userId) {
        getUsersCollection().remove("{userId: #}", userId);
    }

    private MongoCollection getUsersCollection() {
        return mongoClient.getCollection(USERS_COLLECTION_NAME);
    }
}
