package ru.kovalenko;

import ru.kovalenko.Model.User;
import ru.kovalenko.Storage.MapUserStorage;
import ru.kovalenko.Storage.StorageUser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class Main {
    private static final StorageUser users = new MapUserStorage();

    public static void main(String[] args) throws IOException, URISyntaxException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        UUID uuid = null;
        while (true) {
            System.out.println("Введите одну из команд");
            System.out.println("help | login uuid | create link | go shortLink | update shortLink | delete shortLink | inspect shortLink OR all | exit ");
            String[] params = reader.readLine().trim().toLowerCase().split(" ");
            if (params.length < 1 || params.length > 2) {
                System.out.println("Неверная команда. Введите команду help для получения подробной справки");
                continue;
            }
            switch (params[0]) {
                case "help":
                    System.out.println("Справка по работе с программой");
                    System.out.println("Для работы нужно иметь uuid. Если он известен, то необходимо его ввести");
                    System.out.println("Пример: login e52232e1-0ded-4587-999f-4dd135a4a94f");
                    System.out.println("Или создайте короткую ссылку и uuid вам присвоят автоматически. Сохраните его для следующих запусков программы");
                    System.out.println("Пример: create https://ru.stackoverflow.com");
                    System.out.println("Для перехода по короткой ссылке используйте команду go clck.ru/3DZHeG");
                    System.out.println("Система проверит принадлежит ли она вам. Если да, то будет осуществлен переход по ссылке при условии, что:");
                    System.out.println("ссылка не просрочена по времени или не исчерпан лимит переходов, иначе переход не произойдет");
                    System.out.println("Для обновления параметров ссылки используете команду update clck.ru/3DZHeG");
                    System.out.println("Для удаления ссылки используете команду delete clck.ru/3DZHeG");
                    System.out.println("Для просмотра информации о ссылке используете команду inspect clck.ru/3DZHeG");
                    System.out.println("Для просмотра списка ваших ссылок используете команду inspect all");
                    System.out.println("Для выхода из программы введите exit");
                    break;
                case "login":
                    try {
                        UUID checkUUID = UUID.fromString(params[1]);
                        if (!users.checkExistKey(checkUUID)) {
                            System.err.println("Такого UUID нет в базе");
                            System.out.println("Создаете короткую ссылку командой create link и вам будет присвоен uuid");
                        } else {
                            uuid = checkUUID;
                        }
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Неправильный формат UUID " + params[1]);
                    }
                    break;
                case "create":
                    if (uuid == null) {
                        User newUser = new User();
                        users.save(newUser);
                        uuid = newUser.getUuid();
                        System.out.println("Ваш uuid для работы с программой " + uuid + " Сохраните его");
                    }
                    //saveLink();
                    break;
                case "go":
                    //access check
                    if (uuid != null) {
                        Desktop.getDesktop().browse(new URI("https://ru.stackoverflow.com"));
                    } else {
                        System.err.println("Ошибка перехода по ссылке");
                    }
                    break;
                case "update":
                    //access check
                    break;
                case "delete":
                    //access check
                    break;
                case "inspect":
                    //access check
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