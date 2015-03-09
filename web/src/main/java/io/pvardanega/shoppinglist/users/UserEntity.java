package io.pvardanega.shoppinglist.users;

import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.pvardanega.shoppinglist.users.shoppinglist.ShoppingList;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY // mandatory for serialization
)
public class UserEntity {

    protected final ObjectId _id;
    public final Long userId;
    public final String email;
    public final String username;
    protected final String password;
    protected final List<ShoppingList> lists = new ArrayList<>();

    @JsonCreator
    public UserEntity(@JsonProperty("_id") ObjectId _id,
                      @JsonProperty("userId") Long userId,
                      @JsonProperty("email") String email,
                      @JsonProperty("username") String username,
                      @JsonProperty("password") String password,
                      @JsonProperty("lists") List<ShoppingList> lists) {
        this._id = _id;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.lists.addAll(lists);
    }

    public UserEntity(Long id, String email, String username, String password) {
        this(id, email, username, password, newArrayList());
    }

    public UserEntity(Long userId, String email, String username, String password, List<ShoppingList> shoppingLists) {
        this(null, userId, email, username, password, shoppingLists);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity userEntity = (UserEntity) o;

        if (!userId.equals(userEntity.userId)) return false;
        if (!email.equals(userEntity.email)) return false;
        return username.equals(userEntity.username);
    }

    public User toUser() {
        return new User(userId, email, username, null, lists);
    }
}
