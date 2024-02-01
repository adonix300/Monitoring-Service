package models;

import enums.Role;

import java.util.Objects;

/**
 * Класс User представляет пользователя в системе.
 * У пользователя есть логин, пароль и роль.
 */
public class User {
    private final String login;
    private String password;
    private final Role role;

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, role);
    }
}
