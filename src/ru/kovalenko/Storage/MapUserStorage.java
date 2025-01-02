package ru.kovalenko.Storage;

import ru.kovalenko.Model.User;

import java.util.HashMap;
import java.util.UUID;

public class MapUserStorage implements StorageUser {
    private static final HashMap<UUID, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUuid(), user);
    }

    public boolean checkExistKey(UUID uuid) {
        return users.containsKey(uuid);
    }
}
