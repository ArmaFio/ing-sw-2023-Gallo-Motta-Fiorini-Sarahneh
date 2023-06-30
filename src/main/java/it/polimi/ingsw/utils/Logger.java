package it.polimi.ingsw.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static final String PATH = "./logs.txt";

    /**
     * Log of "info" severity. Used for generic purposes.
     *
     * @param str The description of the log.
     */
    public static void info(String str) {
        printLog(str, LogSeverity.INFO);
    }

    /**
     * Log of "debug" severity. Used to show debugging messages.
     *
     * @param str The description of the log.
     */
    public static void debug(String str) {
        printLog(str, LogSeverity.DEBUG);
    }

    /**
     * Log of "warning" severity. Used to show warning messages.
     *
     * @param str The description of the log.
     */
    public static void warning(String str) {
        printLog(str, LogSeverity.WARNING);
    }

    /**
     * Log of "error" severity. Used to show error messages.
     *
     * @param str The description of the log.
     */
    public static void error(String str) {
        printLog(str, LogSeverity.ERROR);
    }

    /**
     * Method that prints the message in the terminal and saves it.
     *
     * @param str      The description of the log.
     * @param severity The severity of the log.
     */
    private synchronized static void printLog(String str, LogSeverity severity) {
        str = formatLog(str, severity);

        if (severity.isPrintedTerminal) {
            System.out.print(str);
        }

        if (severity.isSaved) {
            try (FileWriter file = new FileWriter(PATH, true)) {
                file.write(str);
                file.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Method that formats the description of the log.
     *
     * @param str      The description of the log.
     * @param severity The severity of the log.
     */
    private static String formatLog(String str, LogSeverity severity) {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return '[' + time.format(formatter) + "][" + severity + "]: " + str + '\n';
    }

    /**
     * An enum containing all severities and all their options.
     */
    private enum LogSeverity {
        INFO("INFO", false, false),
        DEBUG("DEBUG", false, false),
        WARNING("WARNING", false, false),
        ERROR("ERROR", true, true);

        public final boolean isPrintedTerminal;
        public final boolean isSaved;
        private final String name;

        /**
         * Private constructor of the enum. The parameters passed can be changed as needed.
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
