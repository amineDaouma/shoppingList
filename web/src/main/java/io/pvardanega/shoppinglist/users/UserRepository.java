package io.pvardanega.shoppinglist.users;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.inject.Singleton;

@Singleton
public class UserRepository {

    private Map<Long, User> users = new HashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public User create(User user) {
        Long userId = (long) counter.incrementAndGet();
        User createdUser = new User(userId, user.email, user.username, user.password);
        users.put(userId, createdUser);
        return createdUser;
    }

    public User get(Long userId) {
        return users.get(userId);
    }

    public void remove(Long userId) {
        counter.decrementAndGet();
        users.remove(userId);
    }
}
