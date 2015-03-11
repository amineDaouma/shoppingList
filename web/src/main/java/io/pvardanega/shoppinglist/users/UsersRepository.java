package io.pvardanega.shoppinglist.users;

import static java.util.Optional.ofNullable;
import java.util.Optional;
import javax.inject.Inject;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import com.google.inject.Singleton;
import com.mongodb.DuplicateKeyException;
import io.pvardanega.shoppinglist.config.MongoClient;
import io.pvardanega.shoppinglist.users.UserEntity.UserIdSeq;
import io.pvardanega.shoppinglist.users.shoppinglist.ShoppingList;

@Singleton
public class UsersRepository {

    public static final String USERS_COLLECTION_NAME = "users";
    protected static final String COUNTERS_COLLECTION_NAME = "counters";
    private MongoClient mongoClient;

    @Inject
    public UsersRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void addNewListTo(Long userId, ShoppingList newList) {
        getUsersCollection()
                .update("{userId: #}", userId)
                .with("{$push: {lists: #}}", newList);
    }

    public void addProductToList(Long userId, String listName, String product) {
        getUsersCollection()
                .update("{userId: #, lists.name: #}", userId, listName)
                .with("{$push: {lists.$.products: #}}", product);
    }

    public UserEntity create(User user) throws DuplicateKeyException {
        UserEntity userToCreate = user.toEntity();
        getUsersCollection().insert(userToCreate);
        getUsersCollection()
                .update(userToCreate._id)
                .with("{$set: {userId: #}}", getNextUserId());
        return get(userToCreate._id).get();
    }

    public Optional<UserEntity> get(Long userId) {
        return ofNullable(getUsersCollection().findOne("{userId: #}", userId).as(UserEntity.class));
    }

    public Optional<UserEntity> get(ObjectId id) {
        return ofNullable(getUsersCollection().findOne(id).as(UserEntity.class));
    }

    public void remove(Long userId) {
        getUsersCollection().remove("{userId: #}", userId);
    }

    private MongoCollection getUsersCollection() {
        return mongoClient.getCollection(USERS_COLLECTION_NAME);
    }

    private MongoCollection getCountersCollection() {
        return mongoClient.getCollection(COUNTERS_COLLECTION_NAME);
    }

    private Long getNextUserId() {
        return getCountersCollection().
                findAndModify("{_id: #}", "userId").
                with("{$inc: {nextVal: 1}}").
                returnNew().
                upsert().
                as(UserIdSeq.class).
                nextVal
        ;
    }
}
