package ru.kovalenko;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class Config {
    protected static final File PROPS = new File("config\\app.properties");

    private final String appName;

    private final int GENERATE_LENGTH;

    private final int LIFETIME;

    private final int TRANSITION_COUNT;

    private static final Config INSTANCE = new Config();

    private Config() {
        try {
            InputStream is = Files.newInputStream(PROPS.toPath());
            Properties props = new Properties();
            props.load(is);
            appName = props.getProperty("app");
            GENERATE_LENGTH = Integer.parseInt(props.getProperty("generate-length", "6"));
            LIFETIME = Integer.parseInt(props.getProperty("lifetime", "3600"));
            TRANSITION_COUNT = Integer.parseInt(props.getProperty("transition-count", "10"));
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

    public int getGENERATE_LENGTH() {
        return GENERATE_LENGTH;
    }

    public int getLIFETIME() {
        return LIFETIME;
    }

    public int getTRANSITION_COUNT() {
        return TRANSITION_COUNT;
    }
}
