package ru.kovalenko.Storage;

import ru.kovalenko.Model.User;

import java.util.List;
import java.util.UUID;

public interface StorageUser {

    void save(User user);

    boolean existsKey(UUID uuid);

    List<String> getUsers();
}
