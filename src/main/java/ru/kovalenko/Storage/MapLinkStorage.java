package ru.kovalenko.Storage;

import ru.kovalenko.Model.Link;

import java.util.HashMap;
import java.util.Map;

public class MapLinkStorage implements StorageLink {
    private final Map<String, Link> links = new HashMap<>();

    @Override
    public Link get(String shortLink) {
        Link link = links.get(shortLink);
        System.out.println(link);
        return link;
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
