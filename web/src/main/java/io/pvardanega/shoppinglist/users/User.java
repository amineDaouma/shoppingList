package io.pvardanega.shoppinglist.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY // mandatory for serialization
)
public class User {
    public final Long userId;
    protected final String email;
    protected final String username;
    protected final String password;

    @JsonCreator
    public User(@JsonProperty("userId") Long userId,
                @JsonProperty("email") String email,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
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

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }

    public UserEntity toEntity() {
        return new UserEntity(userId, email, username, password);
    }
}
