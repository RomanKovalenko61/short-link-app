package ru.kovalenko;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class Config {
    protected static final File PROPS = new File("config\\app.properties");

    private final String appName;

    private static final Config INSTANCE = new Config();

    private Config() {
        try {
            InputStream is = Files.newInputStream(PROPS.toPath());
            Properties props = new Properties();
            props.load(is);
            appName = props.getProperty("app");
        } catch (IOException e) {
            throw new IllegalStateException("Не найден файл конфигурации " + PROPS.getAbsolutePath());
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public String getAppName() {
        return appName;
    }
}
