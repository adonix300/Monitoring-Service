package interfaces;

import models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void changePassword(User user, String oldPassword, String newPassword);
    void registerUser(String login, String password);
    Optional<User> getUser(String login);
    Optional<User> getUser(String login, String password);
    List<String> getLogins();
}
