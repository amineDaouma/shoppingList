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
public class User {

    protected final ObjectId _id;
    public final Long userId;
    public final String email;
    public final String username;
    protected final String password;
    protected final List<ShoppingList> lists = new ArrayList<>();

    @JsonCreator
    public User(@JsonProperty("_id") ObjectId _id,
                @JsonProperty("userId") Long userId,
                @JsonProperty("email") String email,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password) {
        this(_id, userId, email, username, password, newArrayList());
    }

    public User(String email, String username, String password) {
        this(null, email, username, password);
    }

    public User(Long id, String email, String username, String password) {
        this(id, email, username, password, newArrayList());
    }

    public User(Long userId, String email, String username, String password, ArrayList<ShoppingList> shoppingLists) {
        this(null, userId, email, username, password, shoppingLists);
    }

    private User(ObjectId _id, Long userId, String email, String username, String password, ArrayList<ShoppingList> shoppingLists) {
        this._id = _id;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.lists.addAll(shoppingLists);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!userId.equals(user.userId)) return false;
        if (!email.equals(user.email)) return false;
        return username.equals(user.username);
    }
}
