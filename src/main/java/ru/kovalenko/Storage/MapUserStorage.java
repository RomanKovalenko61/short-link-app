package ru.kovalenko.Storage;

import ru.kovalenko.Model.User;
import ru.kovalenko.Utils.GsonLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MapUserStorage implements StorageUser {
    private Map<UUID, User> users;

    {
        try {
            users = GsonLoader.loadUsers();
        } catch (Exception e) {
            System.err.println("Не удалось загрузить список UUID");
        }
    }

    @Override
    public void save(User user) {
        users.put(user.getUuid(), user);
    }

    public boolean existsKey(UUID uuid) {
        return users.containsKey(uuid);
    }

    @Override
    public List<String> getUsers() {
        List<String> allUsers = new ArrayList<>();
        for (Map.Entry<UUID, User> entry : users.entrySet()) {
            allUsers.add(entry.getKey().toString());
        }
        return allUsers;
    }

    public Map<UUID, User> getMapUsers() {
        return users;
    }
}
