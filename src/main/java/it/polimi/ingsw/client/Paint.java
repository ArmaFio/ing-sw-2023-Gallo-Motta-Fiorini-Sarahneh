package it.polimi.ingsw.client;

public class Paint {
    private static final String RESET = "\033[0m";

    public static String Space(int n) {
        if (n < 0) {
            n = 0;
        }
        return " ".repeat(n);
    }

    //TODO da rifare
    public static String formatColors(String str) {
        StringBuilder builder = new StringBuilder(str);

        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == '[') {
                switch (builder.charAt(i + 1)) {
                    case 'G' -> builder.replace(i, i + 2, "\033[0;42m" + "  " + RESET);
                    case 'W' -> builder.replace(i, i + 2, "\033[0;47m" + "  " + RESET);
                    case 'Y' -> builder.replace(i, i + 2, "\033[0;43m" + "  " + RESET);
                    case 'B' -> builder.replace(i, i + 2, "\033[0;44m" + "  " + RESET);
                    case 'C' -> builder.replace(i, i + 2, "\033[0;46m" + "  " + RESET);
                    case 'M' -> builder.replace(i, i + 2, "\033[0;45m" + "  " + RESET);
                }
            }
        }

        return builder.toString();
    }

    public static String Black(String str) {
        return "\033[:90m" + str + RESET;
    }

    public static String Green(String str) {
        return "\033[:92m" + str + RESET;
    }

    public static String BlackBg(String str) {
        return "\033[0;40m" + str + RESET;
    }

    public static String GreenBg(String str) {
        return "\033[0;42m" + str + RESET;
    }

    public static String CyanBg(String str) {
        return "\033[0;46m" + str + RESET;
    }

    public static String WhiteBg(String str) {
        return "\033[0;47m" + str + RESET;
    }

    public static String YellowBg(String str) {
        return "\033[0;43m" + str + RESET;
    }

    public static String BlueBg(String str) {
        return "\033[0;44m" + str + RESET;
    }

    public static String MagentaBg(String str) {
        return "\033[0;45m" + str + RESET;
    }

    enum AnsiCodes {
        RESET("\033[0m", "*"),
        /*
                // Regular Colors. Normal color, no bold, background color etc.
                BLACK("\033[0;30m"),    // BLACK
                RED("\033[0;31m"),      // RED
                GREEN("\033[0;32m"),    // GREEN
                YELLOW("\033[0;33m"),   // YELLOW
                BLUE("\033[0;34m"),     // BLUE
                MAGENTA("\033[0;35m"),  // MAGENTA
                CYAN("\033[0;36m"),     // CYAN
                WHITE("\033[0;37m"),    // WHITE

                // Bold
                BLACK_BOLD("\033[1;30m"),   // BLACK
                RED_BOLD("\033[1;31m"),     // RED
                GREEN_BOLD("\033[1;32m"),   // GREEN
                YELLOW_BOLD("\033[1;33m"),  // YELLOW
                BLUE_BOLD("\033[1;34m"),    // BLUE
                MAGENTA_BOLD("\033[1;35m"), // MAGENTA
                CYAN_BOLD("\033[1;36m"),    // CYAN
                WHITE_BOLD("\033[1;37m"),   // WHITE

                // Underline
                BLACK_UNDERLINED("\033[4;30m"),     // BLACK
                RED_UNDERLINED("\033[4;31m"),       // RED
                GREEN_UNDERLINED("\033[4;32m"),     // GREEN
                YELLOW_UNDERLINED("\033[4;33m"),    // YELLOW
                BLUE_UNDERLINED("\033[4;34m"),      // BLUE
                MAGENTA_UNDERLINED("\033[4;35m"),   // MAGENTA
                CYAN_UNDERLINED("\033[4;36m"),      // CYAN
                WHITE_UNDERLINED("\033[4;37m"),     // WHITE
        */
        // Background
        BLACK_BACKGROUND("\033[40m", "[N]"),   // BLACK
        RED_BACKGROUND("\033[41m", "[R]"),     // RED
        GREEN_BACKGROUND("\033[42m", "[G]"),   // GREEN
        YELLOW_BACKGROUND("\033[43m", "[Y]"),  // YELLOW
        BLUE_BACKGROUND("\033[44m", "[B]"),    // BLUE
        MAGENTA_BACKGROUND("\033[45m", "[M]"), // MAGENTA
        CYAN_BACKGROUND("\033[46m", "[C]"),    // CYAN
        WHITE_BACKGROUND("\033[47m", "[W]");  // WHITE

        /*
        // High Intensity
        BLACK_BRIGHT("\033[0;90m"),     // BLACK
        RED_BRIGHT("\033[0;91m"),       // RED
        GREEN_BRIGHT("\033[0;92m"),     // GREEN
        YELLOW_BRIGHT("\033[0;93m"),    // YELLOW
        BLUE_BRIGHT("\033[0;94m"),      // BLUE
        MAGENTA_BRIGHT("\033[0;95m"),   // MAGENTA
        CYAN_BRIGHT("\033[0;96m"),      // CYAN
        WHITE_BRIGHT("\033[0;97m"),     // WHITE

        // Bold High Intensity
        BLACK_BOLD_BRIGHT("\033[1;90m"),    // BLACK
        RED_BOLD_BRIGHT("\033[1;91m"),      // RED
        GREEN_BOLD_BRIGHT("\033[1;92m"),    // GREEN
        YELLOW_BOLD_BRIGHT("\033[1;93m"),   // YELLOW
        BLUE_BOLD_BRIGHT("\033[1;94m"),     // BLUE
        MAGENTA_BOLD_BRIGHT("\033[1;95m"),  // MAGENTA
        CYAN_BOLD_BRIGHT("\033[1;96m"),     // CYAN
        WHITE_BOLD_BRIGHT("\033[1;97m"),    // WHITE

        // High Intensity backgrounds
        BLACK_BACKGROUND_BRIGHT("\033[0;100m"),     // BLACK
        RED_BACKGROUND_BRIGHT("\033[0;101m"),       // RED
        GREEN_BACKGROUND_BRIGHT("\033[0;102m"),     // GREEN
        YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),    // YELLOW
        BLUE_BACKGROUND_BRIGHT("\033[0;104m"),      // BLUE
        MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"),   // MAGENTA
        CYAN_BACKGROUND_BRIGHT("\033[0;106m"),      // CYAN
        WHITE_BACKGROUND_BRIGHT("\033[0;107m");     // WHITE
        */
        AnsiCodes(String color, String code) {

        }
    }
}

