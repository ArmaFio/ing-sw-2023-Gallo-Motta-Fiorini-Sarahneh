package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.ChatMessage;
import it.polimi.ingsw.messages.LobbiesList;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.io.Serializable;

public class FrameCLI implements Serializable {
    public final int width;
    public final int height;
    private String window;
    private static final int NAME_MAX_LEN = 15;
    private static final int MENU_MAX_LEN = 38;
    private static final int MENU_MAX_HEIGHT = 10;
    private ChatMessage[] chat;

    public FrameCLI(int width, int height) {
        this.width = width;
        this.height = height;
        this.chat = new ChatMessage[]{};
    }

    public void clearScreen() {
        for (int i = 0; i < 10; ++i) {
            System.out.println();
        }
    }

    public void paintWindow(String message, LobbiesList.LobbyData[] lobbies) {
        setFrame();
        if (lobbies.length == 0) {
            addMessage("Currently there are no lobbies available │");
        }
        addLobbies(lobbies);
        addMenu("│ [0] Exit │\n", -1);

        clearScreen();
        System.out.print(window);
    }

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

    public void paintWindow(String message, String username, String password){
            setFrame();
            addMessage(message);
            addLogin(new String[]{username, password});

            clearScreen();
            System.out.print(window);
    }

    public void paintWindow(String message){
            setFrame();
            addMessage(message);
            addCreateJoin();

            clearScreen();
            System.out.print(window);
    }

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

    private void addLogin(String[] credentials) {
        StringBuilder str = new StringBuilder("Username:\n");

        for(int i = 0; i < credentials.length; i++){
            if(i == 1){
                str.append("\nPassword:\n");
            }

            str.append("╭").append("─".repeat(NAME_MAX_LEN + 2)).append("╮\n");

            if(credentials[i].length() <= NAME_MAX_LEN){
                str.append("│ ").append(credentials[i]).append(" ".repeat(NAME_MAX_LEN - credentials[i].length())).append(" │\n");
            } else {
                str.append("│ ").append(credentials[i], 0, NAME_MAX_LEN).append(" │\n");
            }

            str.append("╰").append("─".repeat(NAME_MAX_LEN + 2)).append("╯\n");
        }

        addComponent(str.toString(), width / 2 - getWidthStr(str.toString()) / 2, height / 2 - getHeightStr(str.toString()) / 2);
    }


    private String getCommon(String[] commons) {
        int max_length = 50, k; //TODO fai global
        StringBuilder str = new StringBuilder();
        String[] splitted;

        str.append("│ Common Goal Cards").append(" ".repeat(max_length - "Common Goal Cards".length())).append("│\n");

        for (String common : commons) {
            k = 0;
            str.append("├").append("─".repeat(max_length + 1)).append("┤\n");
            str.append("│");
            splitted = common.split(" ");
            for(String s : splitted){
                if(k + s.length() < max_length){
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

    private void addPlayerList(String[] players) {
        StringBuilder str = new StringBuilder("│ Players:").append(" ".repeat(NAME_MAX_LEN - "Players:\n".length() + 3)).append(" │\n");
        for (int i = 0; i < players.length; i++) {
            if(i != 0){
                str.append("│   ");
            } else {
                str.append("┤   ");
            }
            if(players[i].length() <= NAME_MAX_LEN){
                str.append(players[i]).append(" ".repeat(NAME_MAX_LEN - players[i].length())).append(" │\n");
            } else{
                str.append(players[i], 0, NAME_MAX_LEN).append(" │\n");
            }
        }

        for(int i = 0; i < 4 - players.length; i++){
            str.append("│").append(" ".repeat(NAME_MAX_LEN+3)).append(" │\n");
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


    private void addMenu(String msg, int menuChoice) {
        addMenu(msg, menuChoice, new String[0]);
    }

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

    private String[] content_Chat() { //TODO manca limite altezza
        StringBuilder str = new StringBuilder();
        String[] splitted;
        int k;

        for (ChatMessage msg : chat) {
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
                }
                k += s.length() + 1;
            }
            str.append("-");
        }

        return str.toString().split("-");
    }

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

    private void addComponent(String[] components, int x, int y) {
        int totalWidth = 0;

        for(String c : components){
            totalWidth += getWidthStr(c);
        }

        int offset = 0;
        for(String c : components){
            offset += (2 * x - totalWidth) / (components.length + 1);
            addComponent(c, offset, y - getHeightStr(c) / 2);
            offset += getWidthStr(c);
        }
    }

    private String getBoard(Tile[][] board) {
        StringBuilder window;
        board = expandMatrix(board);

        window = new StringBuilder();

        int minHeigth = board.length;
        int maxHeigth = 0;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (!board[j][i].isNone()) {
                    if (minHeigth > j) {
                        minHeigth = j;
                    }
                    break;
                }
            }
            for (int j = board.length - 1; j > 0; j--) {
                if (!board[j][i].isNone()) {
                    if (maxHeigth < j) {
                        maxHeigth = j;
                    }
                    break;
                }
            }
        }

        int minWidth = board[0].length;
        int maxWidth = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (!board[i][j].isNone()) {
                    if (minWidth > j) {
                        minWidth = j;
                    }
                    break;
                }
            }
            for (int j = board[0].length - 1; j > 0; j--) {
                if (!board[i][j].isNone()) {
                    if (maxWidth < j) {
                        maxWidth = j;
                    }
                    break;
                }
            }
        }

        for (int i = minHeigth; i < maxHeigth + 2; i++) {

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
        for (int i = 0, j = maxHeigth - minHeigth + 1; i < window.length(); i++) {
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

    private Tile[][] expandMatrix(Tile[][] board) {
        Tile[][] m = new Tile[board.length + 2][board[0].length + 2];

        for(int i = 1; i < m.length - 1; i++){
            for (int j = 0; j < m[0].length; j++) {
                if(j == 0 || j == m[0].length - 1){
                    m[i][j] = new Tile(TileType.NONE);
                } else {
                    m[i][j] = board[i - 1][j - 1];
                }
            }
        }

        for(int i = 0; i < m[0].length; i++){
            m[0][i] = new Tile(TileType.NONE);
        }

        for(int i = 0; i < m[0].length; i++){
            m[m.length - 1][i] = new Tile(TileType.NONE);
        }

        return m;
    }

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

    private int getWidthStr(String str){
        String[] v = str.split("\n");
        int max = 0;

        for(String s : v){
            if(s.length() > max) {
                max = s.length();
            }
        }

        return max;
    }

    private int getHeightStr(String str){
        return str.split("\n").length;
    }

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
