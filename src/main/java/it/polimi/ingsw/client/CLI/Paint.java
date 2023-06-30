package it.polimi.ingsw.client.CLI;

public class Paint {
    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[0;42m";
    private static final String WHITE = "\033[0;47m";
    private static final String YELLOW = "\033[0;43m";
    private static final String BLUE = "\033[0;44m";
    private static final String CYAN = "\033[0;46m";
    private static final String MAGENTA = "\033[0;45m";


    public static String Space(int n) {
        if (n < 0) {
            n = 0;
        }
        return " ".repeat(n);
    }

    /**
     * Adds colors to a string.
     *
     * @param str the string to be colored
     */
    public static String formatColors(String str) {
        StringBuilder builder = new StringBuilder(str);

        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == '{') {
                switch (builder.charAt(i + 1)) {
                    case 'G' -> builder.replace(i, i + 2, GREEN + "  " + RESET);
                    case 'W' -> builder.replace(i, i + 2, WHITE + "  " + RESET);
                    case 'Y' -> builder.replace(i, i + 2, YELLOW + "  " + RESET);
                    case 'B' -> builder.replace(i, i + 2, BLUE + "  " + RESET);
                    case 'C' -> builder.replace(i, i + 2, CYAN + "  " + RESET);
                    case 'M' -> builder.replace(i, i + 2, MAGENTA + "  " + RESET);
                }
            }
        }

        return builder.toString();
    }
}

