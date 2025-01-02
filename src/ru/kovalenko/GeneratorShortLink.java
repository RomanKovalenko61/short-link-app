package ru.kovalenko;

import java.util.Random;

public class GeneratorShortLink {
    public static int GEN_LENGTH = 6;
    public static final String SEQUENCE;

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
        for (int i = 0; i < GEN_LENGTH; i++) {
            int index = random.nextInt(SEQUENCE.length());
            builder.append(SEQUENCE.charAt(index));
        }
        return builder.toString();
    }
}
