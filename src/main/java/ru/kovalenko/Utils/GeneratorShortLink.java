package ru.kovalenko.Utils;

import ru.kovalenko.Config;

import java.util.Random;

public class GeneratorShortLink {
    private static final int GEN_LENGTH = Config.getInstance().getGENERATE_LENGTH();
    private static final String PREFIX = Config.getInstance().getPREFIX();
    private static final String SEQUENCE;

    static {
        StringBuilder builder = new StringBuilder();
        for (char c = 'A'; c <= 'z'; c++) {
            builder.append(c);
            if (c == 'Z') c = 'a' - 1;
        }
        for (int i = 0; i < 10; i++) {
            builder.append(i);
        }
        SEQUENCE = builder.toString();
    }

    public static String generate() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        builder.append(PREFIX);
        for (int i = 0; i < GEN_LENGTH; i++) {
            int index = random.nextInt(SEQUENCE.length());
            builder.append(SEQUENCE.charAt(index));
        }
        return builder.toString();
    }
}
