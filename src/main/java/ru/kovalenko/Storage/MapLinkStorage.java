package ru.kovalenko.Storage;

import ru.kovalenko.Model.Link;
import ru.kovalenko.Utils.GsonLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MapLinkStorage implements StorageLink {
    private Map<String, Link> links;

    {
        try {
            links = GsonLoader.loadLinks();
        } catch (Exception e) {
            System.err.println("Не удалось загрузить список ссылок");
        }
    }

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

    public Map<String, Link> getMapLinks() {
        return links;
    }

    @Override
    public String toString() {
        return "MapLinkStorage{" +
                "links=" + links +
                '}';
    }
}
