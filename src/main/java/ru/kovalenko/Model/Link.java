package ru.kovalenko.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Link implements Serializable {
    public String url;
    public String shortLink;
    public UUID owner;
    public LocalDateTime expired;
    public int transitionCount = 0;
    public int transitionLimit;

    public Link(String url, String shortLink, UUID owner, LocalDateTime expired, int transitionLimit) {
        this.url = url;
        this.shortLink = shortLink;
        this.owner = owner;
        this.expired = expired;
        this.transitionLimit = transitionLimit;
    }

    public String getShortLink() {
        return shortLink;
    }

    public String getUrl() {
        return url;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return shortLink.equals(link.shortLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortLink);
    }

    @Override
    public String toString() {
        return "Link{" +
                "url='" + url + '\'' +
                ", shortLink='" + shortLink + '\'' +
                ", owner=" + owner +
                ", expired=" + expired +
                ", transitionCount=" + transitionCount +
                ", transitionLimit=" + transitionLimit +
                '}';
    }
}
