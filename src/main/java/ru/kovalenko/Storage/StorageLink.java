package ru.kovalenko.Storage;

import ru.kovalenko.Model.Link;

public interface StorageLink {
    Link get(String shortLink);

    void delete(String shortLink);

    void save(Link link);

    void update(Link link);

    boolean existsKey(String shortLink);
}
