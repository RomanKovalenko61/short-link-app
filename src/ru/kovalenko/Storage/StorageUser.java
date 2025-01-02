package ru.kovalenko.Storage;

import ru.kovalenko.Model.User;

public interface StorageUser {
    User get(String login, String password);

    void save(User user);

    void update(User user);

    void delete(User user);
}
