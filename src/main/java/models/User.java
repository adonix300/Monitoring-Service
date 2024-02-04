package models;

import enums.Role;

import java.util.Objects;

/**
 * Класс User представляет пользователя в системе.
 * У пользователя есть логин, пароль и роль.
 */
public record User(String login, Role role) {


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) && role == user.role;
    }

}
