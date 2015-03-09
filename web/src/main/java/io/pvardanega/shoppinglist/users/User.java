package io.pvardanega.shoppinglist.users;

import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.pvardanega.shoppinglist.users.shoppinglist.ShoppingList;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY // mandatory for serialization
)
public class User {
    public final Long userId;
    protected final String email;
    protected final String username;
    protected final String password;
    protected final List<ShoppingList> lists = new ArrayList<>();

    @JsonCreator
    public User(@JsonProperty("userId") Long userId,
                @JsonProperty("email") String email,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("lists") List<ShoppingList> lists) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        if (lists != null) {
            this.lists.addAll(lists);
        }
    }

    public User(Long id, String email, String username, String password) {
        this(id, email, username, password, newArrayList());
    }

    public User(String email, String username, String password) {
        this(null, email, username, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return email.equals(user.email) && userId.equals(user.userId) && username.equals(user.username);
    }

    public UserEntity toEntity() {
        return new UserEntity(userId, email, username, password);
    }
}
