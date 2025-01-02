package ru.kovalenko.Model;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

public class Link {
    public URL url;
    public String shortLink;
    public UUID owner;
    public LocalDateTime expired;
    public int transitionCount;
    public int transitionLimit;
}
