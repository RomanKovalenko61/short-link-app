package ru.kovalenko.Model;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

public class Link {
    public URL url;
    public String shortLink;
    public UUID owner;
    public LocalDateTime expired;
    public int transitionCount = 0;
    public int transitionLimit;

    public Link(URL url, String shortLink, UUID owner, LocalDateTime expired, int transitionLimit) {
        this.url = url;
        this.shortLink = shortLink;
        this.owner = owner;
        this.expired = expired;
        this.transitionLimit = transitionLimit;
    }

    public String getShortLink() {
        return shortLink;
    }
}
