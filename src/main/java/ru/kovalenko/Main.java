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
            java.lang.String[] params = reader.readLine().trim().split(" ");
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
                    java.lang.String shortLink;
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
                    Link link = new Link(params[1], shortLink, uuid,
                            LocalDateTime.now().plusSeconds(lifetime), CONFIG.getTRANSITION_COUNT());
                    links.save(link);
                    System.out.println("Создана короткая ссылка: " + shortLink + " для сайта: " + params[1]);
                    break;
                case "go":
                    if (uuid == null) {
                        System.err.println("Ошибка доступа: UUID is null");
                        break;
                    }
                    Link to = null;
                    if (links.existsKey(params[1])) {
                        to = links.get(params[1]);
                    } else {
                        System.err.println("Ссылка не найдена");
                        break;
                    }
                    if (uuid.equals(to.getOwner())) {
                        boolean checkLimitTransition = to.getTransitionCount() <= Math.max(to.getTransitionLimit(), CONFIG.getTRANSITION_COUNT());
                        boolean checkTime = to.getExpired().isAfter(LocalDateTime.now());
                        if (checkTime && checkLimitTransition) {
                            to.increaseTransitionCount();
                            Desktop.getDesktop().browse(new URI(to.getUrl()));
                        }
                        if (!checkLimitTransition) {
                            System.err.println("Исчерпан лимит переходов");
                        }
                        if (!checkTime) {
                            System.err.println("Время жизни ссылки истекло. Она будет удалена из системы");
                            links.delete(params[1]);
                        }
                    } else {
                        System.err.println("Ошибка доступа: такая ссылка не найдена");
                    }
                    break;
                case "update":
                    if (uuid == null) {
                        System.err.println("Ошибка доступа: UUID is null");
                    }
                    Link update = links.get(params[1]);
                    if (uuid.equals(update.getOwner())) {
                        try {
                            int limitFromConsole = Integer.parseInt(params[2]);
                            if (limitFromConsole < 0) {
                                throw new NumberFormatException("Введено отрицательное число");
                            }
                            update.setTransitionLimit(limitFromConsole);
                        } catch (NumberFormatException e) {
                            System.err.println("Неверно введен лимит переходов по ссылке" + e);
                        }
                    }
                    break;
                case "delete":
                    if (uuid == null) {
                        System.err.println("Ошибка доступа: UUID is null");
                    }
                    Link toDelete = links.get(params[1]);
                    if (uuid.equals(toDelete.getOwner())) {
                        links.delete(params[1]);
                    }
                    break;
                case "inspect":
                    if (uuid == null) {
                        System.err.println("Ошибка доступа: UUID is null");
                        break;
                    }
                    Link toInspect = links.get(params[1]);
                    if (toInspect != null && toInspect.getOwner().equals(uuid)) {
                        System.out.println(toInspect);
                    } else {
                        System.err.println("Короткая ссылка не найдена " + params[1]);
                    }
                    break;
                case "getlinks":
                    if (uuid == null) {
                        System.err.println("Ошибка доступа: UUID is null");
                        break;
                    }
                    List<Link> myLink = links.getAll(uuid);
                    myLink.forEach(System.out::println);
                    break;
                case "getusers":
                    List<String> allUUID = users.getUsers();
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
}