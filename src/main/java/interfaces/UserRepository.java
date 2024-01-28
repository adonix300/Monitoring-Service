package interfaces;

import models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void addUser(User user);
    Optional<User> getUser(String login);
    List<String> getAllLogins();
}
