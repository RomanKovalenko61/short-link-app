package ru.kovalenko;

import ru.kovalenko.Model.Link;
import ru.kovalenko.Model.User;
import ru.kovalenko.Storage.MapLinkStorage;
import ru.kovalenko.Storage.MapUserStorage;
import ru.kovalenko.Storage.StorageLink;
import ru.kovalenko.Storage.StorageUser;
import ru.kovalenko.Utils.GeneratorShortLink;

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

    public static void main(java.lang.String[] args) throws IOException, URISyntaxException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        UUID uuid = null;
        java.lang.String name = CONFIG.getAppName();
        System.out.println(name);
        while (true) {
            System.out.println("---------------------------");
            System.out.println("Ваш uuid в системе: " + uuid);
            System.out.println("Введите одну из команд");
            System.out.println("help | login uuid | logout | create link lifetime | go shortLink | update shortLink transitionLimit | delete shortLink | inspect shortLink | getlinks | getusers | exit ");
            String[] params = reader.readLine().trim().split(" ");
            if (params.length < 1 || params.length > 3) {
                System.out.println("Неверная команда. Введите команду help для получения подробной справки");
                continue;
            }
            switch (params[0].toLowerCase()) {
                case "help":
                    System.out.println("Справка по работе с программой");
                    System.out.println("Для работы нужно иметь uuid. Если он известен, то необходимо его ввести");
                    System.out.println("Пример: login e52232e1-0ded-4587-999f-4dd135a4a94f");
                    System.out.println("Или создайте короткую ссылку и uuid вам присвоят автоматически. Сохраните его для следующих запусков программы");
                    System.out.println("Пример: create https://ru.stackoverflow.com [срок жизни ссылки в секундах] Срок не обязательный параметр");
                    System.out.println("Для перехода по короткой ссылке используйте команду go clck.ru/3DZHeG");
                    System.out.println("Система проверит принадлежит ли она вам. Если да, то будет осуществлен переход по ссылке при условии, что:");
                    System.out.println("ссылка не просрочена по времени или не исчерпан лимит переходов, иначе переход не произойдет");
                    System.out.println("Для обновления параметров ссылки используете команду update clck.ru/3DZHeG [15] (установить лимит переходов равный 15) ");
                    System.out.println("Для удаления ссылки используете команду delete clck.ru/3DZHeG");
                    System.out.println("Для просмотра информации о ссылке используете команду inspect clck.ru/3DZHeG");
                    System.out.println("Для просмотра списка ваших ссылок используете команду getlinks");
                    System.out.println("Для просмотра списка uuid зарегистрированных в системе используете команду getusers");
                    System.out.println("Для выхода из программы введите exit");
                    break;
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
                        boolean checkLimitTransition = transitionLink.getTransitionCount() <=
                                Math.max(transitionLink.getTransitionLimit(), CONFIG.getTRANSITION_COUNT());
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
                            System.err.println("Исчерпан лимит переходов");
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
                    }
                    myLink.forEach(System.out::println);
                    break;
                case "getusers":
                    List<String> allUUID = users.getUsers();
                    if (allUUID.isEmpty()) {
                        System.out.println("Упс..все пользователи системы куда-то пропали...");
                    }
                    allUUID.forEach(System.out::println);
                    break;
                case "exit":
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