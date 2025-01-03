package ru.kovalenko.Storage;

import ru.kovalenko.Model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MapUserStorage implements StorageUser {
    private Map<UUID, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUuid(), user);
    }

    public boolean existsKey(UUID uuid) {
        return users.containsKey(uuid);
    }
}
