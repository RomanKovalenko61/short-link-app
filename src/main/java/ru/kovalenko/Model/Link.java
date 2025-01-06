package ru.kovalenko.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Link implements Serializable {
    private final String url;
    private final String shortLink;
    private final UUID owner;
    private final LocalDateTime expired;
    private int transitionCount = 0;
    private int transitionLimit;

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

    public void setTransitionLimit(int transitionLimit) {
        this.transitionLimit = transitionLimit;
    }

    public LocalDateTime getExpired() {
        return expired;
    }

    public int getTransitionCount() {
        return transitionCount;
    }

    public void increaseTransitionCount() {
        transitionCount++;
    }

    public int getTransitionLimit() {
        return transitionLimit;
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
        return "Адрес : " + url +
                ", короткая ссылка " + shortLink +
                ", владелец : " + owner +
                ", Время жизни до : " + expired +
                ", Количество переходов : " + transitionCount +
                ", Лимит переходов : " + transitionLimit +
                '}';
    }
}
