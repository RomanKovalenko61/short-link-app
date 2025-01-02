package ru.kovalenko.Storage;

import ru.kovalenko.Model.User;

import java.util.UUID;

public interface StorageUser {

    void save(User user);

    boolean checkExistKey(UUID uuid);
}
