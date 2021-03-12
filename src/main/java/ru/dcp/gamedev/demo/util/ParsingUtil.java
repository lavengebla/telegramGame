package ru.dcp.gamedev.demo.util;

public class ParsingUtil {
    private ParsingUtil() {
    }
    public static String extractCommand(String text) {
        return text.split(" ")[0];
    }

    public static String extractArguments(String text) {
        return text.substring(text.indexOf(" ") + 1);
    }
}
