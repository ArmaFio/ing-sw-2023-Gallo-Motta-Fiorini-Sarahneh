package it.polimi.ingsw.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class Logger {
    public static final String PATH = "./logs.txt";

    public static void info(String str) {
        printLog(str, LogSeverity.INFO);
    }

    public static void debug(String str) {
        printLog(str, LogSeverity.DEBUG);
    }

    public static void warning(String str) {
        printLog(str, LogSeverity.WARNING);
    }

    public static void error(String str) {
        printLog(str, LogSeverity.ERROR);
    }

    private static void printLog(String str, LogSeverity severity) {
        str = formatString(str, severity);

        if (severity.isPrintedTerminal) {
            System.out.print(str);
        }

        if (severity.isSaved) {
            try (FileWriter file = new FileWriter(PATH)) {
                file.write(str);
                file.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String formatString(String str, LogSeverity severity) {
        return '[' + LocalTime.now().toString() + "][" + severity + "]: " + str + '\n';
    }

    private enum LogSeverity {
        INFO("INFO", true, true),
        DEBUG("DEBUG", true, false),
        WARNING("WARNING", true, true),
        ERROR("ERROR", true, true);

        public final boolean isPrintedTerminal;
        public final boolean isSaved;
        private final String name;

        /**
         * Private constructor of the enum. The parameters passed che be changed as needed.
         *
         * @param str               The severity as {@code String}.
         * @param isPrintedTerminal Set to {@code true} if the severity will be printed in the terminal.
         * @param isSaved           Set to {@code true} if the severity will be saved in {@code logs.txt}.
         */
        LogSeverity(String str, boolean isPrintedTerminal, boolean isSaved) {
            name = str;
            this.isPrintedTerminal = isPrintedTerminal;
            this.isSaved = isSaved;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
