package ru.kovalenko.Storage;

import ru.kovalenko.Model.Link;

import java.util.List;
import java.util.UUID;

public interface StorageLink {
    Link get(String shortLink);

    List<Link> getAll(UUID uuid);

    void delete(String shortLink);

    void save(Link link);

    void update(Link link);

    boolean existsKey(String shortLink);
}
