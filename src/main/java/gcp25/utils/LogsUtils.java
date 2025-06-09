package gcp25.utils;

import java.util.List;

public class LogsUtils {

    public static String tail(String input) {
        List<String> lines = List.of(input.split("\n"));
        int from = Math.max(0, lines.size() - 10);
        return String.join("\n", lines.subList(from, lines.size()));
    }
}
