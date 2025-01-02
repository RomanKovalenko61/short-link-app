package ru.kovalenko.Model;

import java.util.List;
import java.util.UUID;

public class User {
    public UUID uuid;
    public String login;
    public String password;
    public List<Link> myLinks;
}
