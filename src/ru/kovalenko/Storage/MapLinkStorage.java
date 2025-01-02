package ru.kovalenko.Storage;

import ru.kovalenko.Model.Link;

import java.util.HashMap;
import java.util.Map;

public class MapLinkStorage implements StorageLink {
    private Map<String, Link> storage = new HashMap<>();

    @Override
    public Link get(String shortLink) {
        return storage.get(shortLink);
    }

    @Override
    public void delete(String shortLink) {
        storage.remove(shortLink);
    }

    @Override
    public void save(Link link) {
        storage.put(link.getShortLink(), link);
    }

    @Override
    public void update(Link link) {
        storage.put(link.getShortLink(), link);
    }

    @Override
    public boolean existKey() {
        return false;
    }
}
