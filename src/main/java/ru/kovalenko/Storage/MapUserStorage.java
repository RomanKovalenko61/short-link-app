package ru.kovalenko.Storage;

import ru.kovalenko.Model.User;

import java.util.*;

public class MapUserStorage implements StorageUser {
    private Map<UUID, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUuid(), user);
    }

    public boolean existsKey(UUID uuid) {
        return users.containsKey(uuid);
    }

    @Override
    public List<String> getUsers() {
        List<java.lang.String> allUsers = new ArrayList<>();
        for (Map.Entry<UUID, User> entry : users.entrySet()) {
            allUsers.add(entry.getKey().toString());
        }
        return allUsers;
    }
}
