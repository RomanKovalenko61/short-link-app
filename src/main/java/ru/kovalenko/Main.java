package ru.kovalenko;

import ru.kovalenko.Model.Link;
import ru.kovalenko.Model.User;
import ru.kovalenko.Storage.MapLinkStorage;
import ru.kovalenko.Storage.MapUserStorage;
import ru.kovalenko.Storage.StorageLink;
import ru.kovalenko.Storage.StorageUser;
import ru.kovalenko.Utils.GeneratorShortLink;
import ru.kovalenko.Utils.GsonLoader;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Main {
    private final static StorageUser users = new MapUserStorage();
    private final static StorageLink links = new MapLinkStorage();

    private final static Config CONFIG = Config.getInstance();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        UUID uuid = null;
        while (true) {
            System.out.println("---------------------------");
            System.out.println("Ваш uuid в системе: " + uuid);
            System.out.println("Введите одну из команд");
            System.out.println("login uuid | logout | create link lifetime | go shortLink | update shortLink transitionLimit | delete shortLink | inspect shortLink | getlinks | getusers | exit ");
            String[] params = reader.readLine().trim().split(" ");
            if (params.length < 1 || params.length > 3) {
                System.out.println("Неверная команда");
                continue;
            }
            switch (params[0].toLowerCase()) {
                case "login":
                    try {
                        UUID checkUUID = UUID.fromString(params[1]);
                        if (users.existsKey(checkUUID)) {
                            uuid = checkUUID;
                            System.out.println("Успешный вход в систему с uuid " + uuid);
                        } else {
                            System.err.println("Такого UUID нет в базе");
                            System.out.println("Создаете короткую ссылку командой create link и вам будет присвоен uuid");
                        }
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Неправильный формат UUID " + params[1]);
                    }
                    break;
                case "logout":
                    uuid = null;
                    break;
                case "create":
                    if (uuid == null) {
                        User newUser = new User();
                        users.save(newUser);
                        uuid = newUser.getUuid();
                        System.out.println("Создан uuid для работы с программой " + uuid);
                    }
                    String shortLink;
                    do {
                        shortLink = GeneratorShortLink.generate();
                    } while (links.existsKey(shortLink));
                    int lifetime;
                    if (params.length == 3) {
                        try {
                            int timeFromConsole = Integer.parseInt(params[2]);
                            lifetime = Math.min(timeFromConsole, CONFIG.getLIFETIME());
                        } catch (NumberFormatException e) {
                            System.err.println("Неверно введен лимит жизни ссылки в секундах" + e);
                            System.err.println("Будет присвоено значение по умолчанию:  " + CONFIG.getLIFETIME());
                            lifetime = CONFIG.getLIFETIME();
                        }
                    } else {
                        lifetime = CONFIG.getLIFETIME();
                    }
                    Link newLink = new Link(params[1], shortLink, uuid,
                            LocalDateTime.now().plusSeconds(lifetime), CONFIG.getTRANSITION_COUNT());
                    links.save(newLink);
                    System.out.println("Создана короткая ссылка: " + shortLink + " для сайта: " + params[1]);
                    break;
                case "go":
                    doAction(uuid, params[1], transitionLink -> {
                        int maxTransition = Math.max(transitionLink.getTransitionLimit(), CONFIG.getTRANSITION_COUNT());
                        boolean checkLimitTransition = transitionLink.getTransitionCount() < maxTransition;
                        boolean checkTime = transitionLink.getExpired().isAfter(LocalDateTime.now());
                        if (checkTime && checkLimitTransition) {
                            try {
                                Desktop.getDesktop().browse(new URI(transitionLink.getUrl()));
                                transitionLink.increaseTransitionCount();
                                System.out.println("Переход выполнен успешно");
                            } catch (IOException | URISyntaxException e) {
                                System.err.println("Что-то пошло не так при переходе по ссылке");
                            }
                        }
                        if (!checkLimitTransition) {
                            System.err.println("Исчерпан лимит переходов. Использовано: " + transitionLink.getTransitionLimit());
                        }
                        if (!checkTime) {
                            System.err.println("Время жизни ссылки истекло. Она будет удалена из системы");
                            links.delete(params[1]);
                        }
                    });
                    break;
                case "update":
                    doAction(uuid, params[1], updateLink -> {
                        try {
                            int limitFromConsole = Integer.parseInt(params[2]);
                            if (limitFromConsole < 0) {
                                throw new NumberFormatException("Введено отрицательное число");
                            }
                            updateLink.setTransitionLimit(limitFromConsole);
                            System.out.println("Обновлено: " + updateLink);
                        } catch (NumberFormatException e) {
                            System.err.println("Неверно введен лимит переходов по ссылке " + e);
                        }
                    });
                    break;
                case "delete":
                    doAction(uuid, params[1], deleteLink -> {
                        links.delete(deleteLink.getShortLink());
                        System.out.println("Ссылка была удалена");
                    });
                    break;
                case "inspect":
                    doAction(uuid, params[1], System.out::println);
                    break;
                case "getlinks":
                    if (uuid == null) {
                        System.err.println("Ошибка доступа: UUID is null");
                        break;
                    }
                    List<Link> myLink = links.getAll(uuid);
                    if (myLink.isEmpty()) {
                        System.out.println("Не найдено коротких ссылок");
                    } else {
                        myLink.forEach(System.out::println);
                    }
                    break;
                case "getusers":
                    List<String> allUUID = users.getUsers();
                    if (allUUID.isEmpty()) {
                        System.out.println("Упс..все пользователи системы куда-то пропали...");
                    } else {
                        allUUID.forEach(System.out::println);
                    }
                    break;
                case "exit":
                    GsonLoader.saveUsers(((MapUserStorage) users).getMapUsers());
                    GsonLoader.saveLinks(((MapLinkStorage) links).getMapLinks());
                    return;
                default:
                    System.out.println("Неверная команда");
                    break;
            }
        }
    }

    private static boolean doAction(UUID uuid, String shortLink, Consumer<Link> consumer) {
        if (uuid == null) {
            System.err.println("Ошибка доступа: UUID is null");
            return false;
        }
        if (!links.existsKey(shortLink)) {
            System.err.println("Ошибка доступа: такой короткой ссылки нет");
            return false;
        }
        Link link = links.get(shortLink);
        if (!link.getOwner().equals(uuid)) {
            System.err.println("Ошибка доступа: UUID не соответствует");
            return false;
        }
        consumer.accept(link);
        return true;
    }
}