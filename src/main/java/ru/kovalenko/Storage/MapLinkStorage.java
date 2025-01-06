package ru.kovalenko.Storage;

import ru.kovalenko.Model.Link;

import java.util.*;

public class MapLinkStorage implements StorageLink {
    private final Map<String, Link> links = new HashMap<>();

    @Override
    public Link get(String shortLink) {
        return links.get(shortLink);
    }

    @Override
    public List<Link> getAll(UUID uuid) {
        List<Link> filtered = new ArrayList<>();
        for (Map.Entry<String, Link> entry : links.entrySet()) {
            if (entry.getValue().getOwner().equals(uuid)) {
                filtered.add(entry.getValue());
            }
        }
        return filtered;
    }

    @Override
    public void delete(String shortLink) {
        links.remove(shortLink);
    }

    @Override
    public void save(Link link) {
        links.put(link.getShortLink(), link);
    }

    @Override
    public void update(Link link) {
        links.put(link.getShortLink(), link);
    }

    @Override
    public boolean existsKey(String shortLink) {
        return links.containsKey(shortLink);
    }

    @Override
    public String toString() {
        return "MapLinkStorage{" +
                "links=" + links +
                '}';
    }
}
