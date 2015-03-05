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

    protected final Long id;
    protected final String email;
    protected final String username;
    protected final String password;
    protected final List<ShoppingList> lists = new ArrayList<>();

    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password) {
        this(null, email, username, password);
    }

    public User(Long id, String email, String username, String password) {
        this(id, email, username, password, newArrayList());
    }

    public User(Long userId, String email, String username, String password, ArrayList<ShoppingList> shoppingLists) {
        this.id = userId;
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

        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
