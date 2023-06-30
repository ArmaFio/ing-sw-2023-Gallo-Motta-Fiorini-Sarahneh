package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.messages.ChatMessage;
import it.polimi.ingsw.messages.LobbiesList;
import it.polimi.ingsw.server.model.tiles.Tile;
import it.polimi.ingsw.server.model.tiles.TileType;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.io.Serializable;

public class FrameCLI implements Serializable {
    private static final int NAME_MAX_LEN = 15;
    private static final int MENU_MAX_LEN = 38;
    private static final int MENU_MAX_HEIGHT = 10;
    public final int width;
    public final int height;
    private String window;
    private ChatMessage[] chat;

    public FrameCLI(int width, int height) {
        this.width = width;
        this.height = height;
        this.chat = new ChatMessage[]{};
    }

    /**
     * Clears the screen.
     */
    public void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            Logger.error("Can't clear screen");
        }
    }

    /**
     * Shows the window with the list of lobbies.
     *
     * @param message The message to show.
     * @param lobbies The data of the lobbies.
     */
    public void paintWindow(String message, LobbiesList.LobbyData[] lobbies) {
        setFrame();
        if (lobbies.length == 0) {
            addMessage("Currently there are no lobbies available │");
        } else {
            addMessage(message);
        }
        addLobbies(lobbies);
        addMenu("│ [0] Exit │\n", -1);

        clearScreen();
        System.out.print(window);
    }

    /**
     * Shows the window inside the lobby.
     *
     * @param message    The message to show.
     * @param players    The list of players inside the lobby.
     * @param admin      The admin of the lobby.
     * @param menuChoice The current menu open.
     */
    public void paintWindow(String message, String[] players, boolean admin, int menuChoice) {
        setFrame();
        addMessage(message);
        addPlayerList(players);

        if (admin) {
            addMenu("│ [0] Exit │ [1]Chat │ [2]Start game │\n", menuChoice);
        } else {
            addMenu("│ [0] Exit │ [1]Chat │\n", menuChoice);
        }

        clearScreen();
        System.out.print(window);
    }

    /**
     * Shows the login window.
     *
     * @param message  The message to show.
     * @param username The username inserted.
     * @param password The password inserted.
     */
    public void paintWindow(String message, String username, String password) {
        setFrame();
        addMessage(message);
        addLogin(new String[]{username, password});

        clearScreen();
        System.out.print(window);
    }

    /**
     * Shows the window with the choice of creating or joining a lobby.
     *
     * @param message The message to show.
     */
    public void paintWindow(String message) {
        setFrame();
        addMessage(message);
        addCreateJoin();

        clearScreen();
        System.out.print(window);
    }

    /**
     * Shows the window with the main board or the shelf.
     *
     * @param message    The message to show.
     * @param board      The board to show.
     * @param players    The list of player of the lobby.
     * @param menuChoice The current menu open.
     */
    public void paintWindow(String message, Tile[][] board, String[] players, int menuChoice) {
        setFrame();
        addMessage(message);
        if (board.length > 0) {
            addComponent(new String[]{getLegend(), getBoard(board)}, width / 5 * 2, height / 2);
        }

        addMenu("│ [0]Change view │ [1]Chat │\n", menuChoice, players);
        addPlayerList(players);

        window = Paint.formatColors(window);

        clearScreen();
        System.out.print(window);
    }


    /**
     * Shows the window with the goals of the game.
     *
     * @param message    The message to show.
     * @param personal   The personal goal card to show.
     * @param commons    The common goal cards to show.
     * @param players    The list of player of the lobby.
     * @param menuChoice The current menu open.
     */
    public void paintWindow(String message, Tile[][] personal, String[] commons, String[] players, int menuChoice) {
        setFrame();
        addMessage(message);

        if (personal.length > 0) {
            addComponent(new String[]{getLegend(), getBoard(personal), getCommon(commons)}, width / 2, height / 2);
        }

        addMenu("│ [0]Change view │ [1]Chat │\n", menuChoice, players);
        addPlayerList(players);

        window = Paint.formatColors(window);

        clearScreen();
        System.out.print(window);
    }

    /**
     * Formats the create-join lobby buttons.
     */
    private void addCreateJoin() {
        String str = """
                    ╭─────────────────╮
                [0] │ Create a lobby  │
                    ╰─────────────────╯
                    ╭─────────────────╮
                [1] │ Join a lobby    │
                    ╰─────────────────╯""";

        addComponent(str, width / 2 - getWidthStr(str) / 2, height / 2 - getHeightStr(str) / 2);
    }

    /**
     * Formats the login text fields.
     *
     * @param credentials An array containing username and password.
     */
    private void addLogin(String[] credentials) {
        StringBuilder str = new StringBuilder("Username:\n");

        for (int i = 0; i < credentials.length; i++) {
            if (i == 1) {
                str.append("\nPassword:\n");
            }

            str.append("╭").append("─".repeat(NAME_MAX_LEN + 2)).append("╮\n");

            if (credentials[i].length() <= NAME_MAX_LEN) {
                str.append("│ ").append(credentials[i]).append(" ".repeat(NAME_MAX_LEN - credentials[i].length())).append(" │\n");
            } else {
                str.append("│ ").append(credentials[i], 0, NAME_MAX_LEN).append(" │\n");
            }

            str.append("╰").append("─".repeat(NAME_MAX_LEN + 2)).append("╯\n");
        }

        addComponent(str.toString(), width / 2 - getWidthStr(str.toString()) / 2, height / 2 - getHeightStr(str.toString()) / 2);
    }


    /**
     * Formats the common goal cards.
     *
     * @param commons An array containing the descriptions of the common goal cards.
     * @return The common goal card formatted.
     */
    private String getCommon(String[] commons) {
        int max_length = 50, k;
        StringBuilder str = new StringBuilder();
        String[] splitted;

        str.append("│ Common Goal Cards").append(" ".repeat(max_length - "Common Goal Cards".length())).append("│\n");

        for (String common : commons) {
            k = 0;
            str.append("├").append("─".repeat(max_length + 1)).append("┤\n");
            str.append("│");
            splitted = common.split(" ");
            for (String s : splitted) {
                if (k + s.length() < max_length) {
                    str.append(" ").append(s);
                } else {
                    str.append(" ".repeat(max_length - k)).append(" │\n│ ").append(s);
                    k = 0;
                }
                k += s.length() + 1;
            }
            str.append(" ".repeat(max_length - k)).append(" │\n");
        }


        str.append("╰").append("─".repeat(max_length + 1)).append("╯\n");

        str.insert(0, "╭" + "─".repeat(str.toString().split("\n")[0].length() - 2) + "╮\n");

        return str.toString();
    }


    /**
     * Formats the list of players and adds it in the top-right corner.
     *
     * @param players The list of players.
     */
    private void addPlayerList(String[] players) {
        StringBuilder str = new StringBuilder("│ Players:").append(" ".repeat(NAME_MAX_LEN - "Players:\n".length() + 3)).append(" │\n");
        for (int i = 0; i < players.length; i++) {
            if (i != 0) {
                str.append("│   ");
            } else {
                str.append("┤   ");
            }
            if (players[i].length() <= NAME_MAX_LEN) {
                str.append(players[i]).append(" ".repeat(NAME_MAX_LEN - players[i].length())).append(" │\n");
            } else {
                str.append(players[i], 0, NAME_MAX_LEN).append(" │\n");
            }
        }

        for (int i = 0; i < 4 - players.length; i++) {
            str.append("│").append(" ".repeat(NAME_MAX_LEN + 3)).append(" │\n");
        }

        str.append("╰");
        str.append("─".repeat(NAME_MAX_LEN + 4));
        str.append("┤\n");

        str.insert(0, "┬" + "─".repeat(getWidthStr(str.toString()) - 2) + "╮\n");
        addComponent(str.toString(), width - getWidthStr(str.toString()) + 1, -1);
    }

    private void addMessage(String message) {
        String str = "│ " + message + "\n";
        str += "├" + "─".repeat(width) + "\n";
        addComponent(str, -1, 0);
    }

    /**
     * Adds the bar menu in the bottom-right corner for the non-game windows.
     *
     * @param buttonsBar The buttons of the menu.
     * @param menuChoice The current menu open.
     */
    private void addMenu(String buttonsBar, int menuChoice) {
        addMenu(buttonsBar, menuChoice, new String[0]);
    }

    /**
     * Adds the bar menu in the bottom-right corner for the game.
     *
     * @param buttonsBar The buttons of the menu.
     * @param menuChoice The current menu open.
     * @param players    The list of players.
     */
    private void addMenu(String buttonsBar, int menuChoice, String[] players) {
        StringBuilder upperBar;

        if (menuChoice != -1) {
            if (menuChoice == 0) {
                upperBar = new StringBuilder("│\n");
            } else {
                upperBar = new StringBuilder("├\n");
            }
        } else {
            upperBar = new StringBuilder("╭\n");
        }

        for (int i = 1, j = 0; i < buttonsBar.length() - 2; i++) {
            if (buttonsBar.charAt(i) == '│') {
                if (menuChoice == j) {
                    upperBar.insert(i, '╭');
                } else if (menuChoice == j + 1) {
                    upperBar.insert(i, '╮');
                } else {
                    upperBar.insert(i, '┬');
                }
                j++;
            } else {
                if (menuChoice == j) {
                    upperBar.insert(i, ' ');
                } else {
                    upperBar.insert(i, '─');
                }
            }
        }
        if (menuChoice == 2) {
            upperBar.insert(upperBar.length() - 1, "│");
        } else {
            upperBar.insert(upperBar.length() - 1, "┤");
        }

        StringBuilder bottomBar = new StringBuilder("┴\n");
        for (int i = 1; i < buttonsBar.length() - 2; i++) {
            if (buttonsBar.charAt(i) == '│') {
                bottomBar.insert(i, '┴');
            } else {
                bottomBar.insert(i, '─');
            }
        }
        bottomBar.insert(bottomBar.length() - 1, "╯");

        String str = upperBar + buttonsBar + bottomBar;

        addComponent(str, width - buttonsBar.length() + 2, height - 2);

        if (menuChoice == 0 || menuChoice == 1) {
            String[] content;
            if (menuChoice == 0) {
                content = content_ChangeView(players);
            } else {
                content = content_Chat();
            }
            StringBuilder menu = new StringBuilder("╭" + "─".repeat(buttonsBar.length() - 3) + "┤\n");
            for (String line : content) {
                menu.append("│ ").append(line).append(Paint.Space(buttonsBar.length() - line.length() - 4)).append("│\n");
            }

            addComponent(menu.toString(), width - buttonsBar.length() + 2, height - 3 - content.length);
        }
    }

    /**
     * @return The content of the chat.
     */
    private String[] content_Chat() {
        StringBuilder str = new StringBuilder();
        String[] splitted;
        int k, height = 0;

        for (int i = chat.length - 1; i >= 0; i--) {
            ChatMessage msg = chat[i];
            if (msg.getReceiver().equals("")) {
                str.append("[All]");
            } else {
                str.append("[You]");
            }

            if (msg.getAuthor().length() > NAME_MAX_LEN) {
                str.append(msg.getAuthor(), 0, NAME_MAX_LEN).append(":");
            } else {
                str.append(msg.getAuthor()).append(":");
            }

            splitted = msg.getMessage().split(" ");
            k = msg.getAuthor().length() + 6;
            for (String s : splitted) {
                if (k + s.length() < MENU_MAX_LEN) {
                    str.append(" ").append(s);
                } else {
                    str.append("-").append(s);
                    k = 0;
                    height++;
                }
                k += s.length() + 1;
            }
            str.append("-");
            height++;
            if (height >= MENU_MAX_HEIGHT) {
                break;
            }
        }

        return str.toString().split("-");
    }

    /**
     * @param players The list of players of the game.
     * @return The content of the menu for changing windows.
     */
    private String[] content_ChangeView(String[] players) {
        StringBuilder str = new StringBuilder("[A] Goals-");

        str.append("[B] Board -");

        for (int i = 0; i < players.length; i++) {
            if (players[i].length() < NAME_MAX_LEN) {
                str.append("[").append((char) ('A' + i + 2)).append("] ").append(players[i]).append("'s Shelf ").append("-");
            } else {
                str.append("[").append((char) ('A' + i + 2)).append("] ").append(players[i], 0, NAME_MAX_LEN).append("'s Shelf ").append("-");
            }
        }

        return (str + "-").split("-");
    }

    /**
     * Formats the legend of tiles.
     *
     * @return The legend formatted.
     */
    private String getLegend() {

        return "\n" +
                "╭───────────────╮\n" +
                "│ Legend:       │\n" +
                "│ " + paintTile(new Tile(TileType.CAT)) + " = Cats     │\n" +
                "│ " + paintTile(new Tile(TileType.BOOK)) + " = Books    │\n" +
                "│ " + paintTile(new Tile(TileType.GAME)) + " = Games    │\n" +
                "│ " + paintTile(new Tile(TileType.FRAME)) + " = Frames   │\n" +
                "│ " + paintTile(new Tile(TileType.TROPHY)) + " = Trophies │\n" +
                "│ " + paintTile(new Tile(TileType.PLANT)) + " = Plants   │\n" +
                "╰───────────────╯";
    }

    /**
     * Adds a string to the window at a precise position.
     *
     * @param component The string to add.
     * @param x         The x position.
     * @param y         The y position.
     */
    private void addComponent(String component, int x, int y) {
        StringBuilder str = new StringBuilder(this.window);
        String[] lines = component.split("\n");
        int offset;

        for (int i = 0; i < lines.length; i++) {
            offset = (y + i + 1) * (width + 3) + x + 1;
            str.replace(offset, offset + lines[i].length(), lines[i]);
        }

        this.window = str.toString();
    }

    /**
     * Adds multiple strings to the window at a precise position.
     *
     * @param components The string to add.
     * @param x          The x position.
     * @param y          The y position.
     */
    private void addComponent(String[] components, int x, int y) {
        int totalWidth = 0;

        for (String c : components) {
            totalWidth += getWidthStr(c);
        }

        int offset = 0;
        for (String c : components) {
            offset += (2 * x - totalWidth) / (components.length + 1);
            addComponent(c, offset, y - getHeightStr(c) / 2);
            offset += getWidthStr(c);
        }
    }

    /**
     * Formats the board to fit in the window.
     *
     * @param board The board to format.
     * @return The formatted board.
     */
    private String getBoard(Tile[][] board) {
        StringBuilder window;
        board = expandMatrix(board);

        window = new StringBuilder();

        int minHeight = board.length;
        int maxHeight = 0;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (!board[j][i].isNone()) {
                    if (minHeight > j) {
                        minHeight = j;
                    }
                    break;
                }
            }
            for (int j = board.length - 1; j > 0; j--) {
                if (!board[j][i].isNone()) {
                    if (maxHeight < j) {
                        maxHeight = j;
                    }
                    break;
                }
            }
        }

        int minWidth = board[0].length;
        int maxWidth = 0;
        for (Tile[] tiles : board) {
            for (int j = 0; j < board[0].length; j++) {
                if (!tiles[j].isNone()) {
                    if (minWidth > j) {
                        minWidth = j;
                    }
                    break;
                }
            }
            for (int j = board[0].length - 1; j > 0; j--) {
                if (!tiles[j].isNone()) {
                    if (maxWidth < j) {
                        maxWidth = j;
                    }
                    break;
                }
            }
        }

        for (int i = minHeight; i < maxHeight + 2; i++) {

            for (int j = minWidth; j < maxWidth + 2; j++) {
                if (!board[i][j].isNone() && board[i][j - 1].isNone()) {
                    if (!board[i - 1][j].isNone() && board[i - 1][j - 1].isNone()) {
                        window.append("├────");
                    } else if (!board[i - 1][j].isNone() && !board[i - 1][j - 1].isNone()) {
                        window.append("┼────");
                    } else {
                        window.append("╭────");
                    }
                } else if (!board[i][j].isNone() && !board[i][j - 1].isNone()) {
                    if (board[i - 1][j].isNone() && board[i - 1][j - 1].isNone()) {
                        window.append("┬────");
                    } else {
                        window.append("┼────");
                    }
                } else if (!board[i][j - 1].isNone()) {
                    if (board[i - 1][j - 1].isNone() && board[i - 1][j].isNone()) {
                        window.append("╮    ");
                    } else if (board[i - 1][j].isNone()) {
                        window.append("┤    ");
                    } else {
                        window.append("┼────");
                    }
                } else {
                    if (!board[i - 1][j].isNone() && board[i - 1][j - 1].isNone()) {
                        window.append("╰────");
                    } else if (!board[i - 1][j].isNone() && !board[i - 1][j - 1].isNone()) {
                        window.append("┴────");
                    } else if (board[i - 1][j].isNone() && !board[i - 1][j - 1].isNone()) {
                        window.append("╯    ");
                    } else {
                        window.append("     ");
                    }
                }
            }
            window.append("\n");

            for (int j = minWidth; j < maxWidth + 2; j++) {
                if (board[i][j].isNone() && board[i][j - 1].isNone()) {
                    window.append("     ");
                } else if (board[i][j].isEmpty() || (board[i][j].isNone() && !board[i][j - 1].isNone())) {
                    window.append("│    ");
                } else {
                    window.append("│ ").append(paintTile(board[i][j])).append(" ");
                }
            }
            window.append("\n");
        }


        boolean flag = true;
        window.insert(0, "   ");
        for (int i = 0, j = maxHeight - minHeight + 1; i < window.length(); i++) {
            if (window.charAt(i) == '\n') {
                if (flag) {
                    if (j >= 1) {
                        window.insert(i + 1, j + "  ");
                    }
                    j--;
                    flag = false;
                } else {
                    window.insert(i + 1, "   ");
                    flag = true;
                }
            }
        }

        char c = 'A';
        for (int i = 0; i < maxWidth - minWidth + 1; i++) {
            window.append("  ").append(c).append("  ");
            c++;
        }

        return window.toString();
    }

    /**
     * Used to turn the board into a 9x9 matrix.
     *
     * @param board The board to expand.
     * @return The expanded board.
     */
    private Tile[][] expandMatrix(Tile[][] board) {
        Tile[][] m = new Tile[board.length + 2][board[0].length + 2];

        for (int i = 1; i < m.length - 1; i++) {
            for (int j = 0; j < m[0].length; j++) {
                if (j == 0 || j == m[0].length - 1) {
                    m[i][j] = new Tile(TileType.NONE);
                } else {
                    m[i][j] = board[i - 1][j - 1];
                }
            }
        }

        for (int i = 0; i < m[0].length; i++) {
            m[0][i] = new Tile(TileType.NONE);
        }

        for (int i = 0; i < m[0].length; i++) {
            m[m.length - 1][i] = new Tile(TileType.NONE);
        }

        return m;
    }

    /**
     * Given a tile, returns a string with the corresponding color.
     *
     * @param tile The tile to paint.
     * @return The string with the color.
     */
    private String paintTile(Tile tile) {
        String str = "  ";

        switch (tile.type) {
            case CAT -> {
                return "{G";
            }
            case BOOK -> {
                return "{W";
            }
            case GAME -> {
                return "{Y";
            }
            case FRAME -> {
                return "{B";
            }
            case TROPHY -> {
                return "{C";
            }
            case PLANT -> {
                return "{M";
            }
            default -> {
                return str;
            }
        }
    }

    /**
     * Draws the frame of the window.
     */
    private void setFrame() {
        StringBuilder window = new StringBuilder();

        window.append("│\n".repeat(height));

        String str;
        for (int i = 1; i < window.length(); i++) {
            if (window.charAt(i) == '\n') {
                str = Paint.Space(width) + "│";
                window.insert(i, str);
                i += str.length();
            }
        }

        window.insert(0, '╭' + "─".repeat(width) + "╮\n");

        window.append('╰').append("─".repeat(width)).append("╯\n");

        this.window = window.toString();
    }

    /**
     * Returns the width of a sequence of lines of character.
     *
     * @param str The sequence of lines.
     * @return The width of the sequence.
     */
    private int getWidthStr(String str) {
        String[] v = str.split("\n");
        int max = 0;

        for (String s : v) {
            if (s.length() > max) {
                max = s.length();
            }
        }

        return max;
    }

    /**
     * Returns the height of a sequence of lines of character.
     *
     * @param str The sequence of lines.
     * @return The height of the sequence.
     */
    private int getHeightStr(String str) {
        return str.split("\n").length;
    }


    /**
     * Formats the list of lobbies.
     *
     * @param lobbies The list of lobbies.
     */
    private void addLobbies(LobbiesList.LobbyData[] lobbies) {
        StringBuilder str = new StringBuilder();

        str.append("    ╭─────────────────────────┬─────┬─────╮\n");
        str.append("    │ Name                    │ Dim │ Ids │\n");

        int cont = 1;
        for (LobbiesList.LobbyData l : lobbies) {
            if (l == null) {
                break;
            } else {
                str.append("    ├─────────────────────────┼─────┼─────┤" + "\n");
                if (l.admin.length() < NAME_MAX_LEN) {
                    str.append("[").append(cont).append("] │ ").append(l.admin).append("'s lobby").append(" ".repeat(NAME_MAX_LEN - l.admin.length())).append(" │ ").append(l.capacity).append("/").append(l.lobbyDim).append(" │ ").append(l.id).append(" │\n");
                } else {
                    str.append("[").append(cont).append("] │ ").append(l.admin).append("'s lobby").append(" │ ").append(l.capacity).append("/").append(l.lobbyDim).append(" │\n");
                }
                cont++;
            }
        }

        str.append("    ╰─────────────────────────┴─────┴─────╯\n");

        addComponent(str.toString(), width / 2 - getWidthStr(str.toString()) / 2, height / 2 - getHeightStr(str.toString()) / 2);
    }


    public void setChat(ChatMessage[] chat) {
        this.chat = chat;
    }
}
