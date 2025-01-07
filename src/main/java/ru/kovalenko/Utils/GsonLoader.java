package ru.kovalenko.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.kovalenko.Model.Link;
import ru.kovalenko.Model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GsonLoader {

    private static final File storageUsers = new File("config\\users.map");

    private static final File storageLinks = new File("config\\links.map");

    private static final Gson gson = new Gson();

    public static void saveUsers(Map<UUID, User> users) throws IOException {
        Type type = new TypeToken<Map<UUID, User>>() {
        }.getType();
        String json = gson.toJson(users, type);

        writer(json, storageUsers);
    }

    public static void saveLinks(Map<String, Link> links) throws IOException {
        Type type = new TypeToken<Map<String, Link>>() {
        }.getType();
        String json = gson.toJson(links, type);

        writer(json, storageLinks);
    }

    public static Map<UUID, User> loadUsers() throws Exception {
        Type type = new TypeToken<Map<UUID, User>>() {
        }.getType();

        return reader(storageUsers, type);
    }

    public static Map<String, Link> loadLinks() throws Exception {
        Type type = new TypeToken<Map<String, Link>>() {
        }.getType();

        return reader(storageLinks, type);
    }

    private static void writer(String json, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
            System.out.println("Успешно записано " + file.getAbsolutePath());
        }
    }

    private static <K, V> Map<K, V> reader(File file, Type type) throws IOException {
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return gson.fromJson(sb.toString(), type);
        }
    }
}
