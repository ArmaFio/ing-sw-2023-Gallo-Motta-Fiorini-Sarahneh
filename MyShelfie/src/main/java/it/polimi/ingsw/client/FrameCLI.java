package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Tile;

public class FrameCLI {
    public final int width;
    public final int height;
    private String window;

    public FrameCLI(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void clearScreen() {
        for (int i = 0; i < 10; ++i) {
            System.out.println();
        }
    }

    public void paintWindow(String message, Tile[][] board, String[] players, int menuChoice) {
        setFrame();
        addMessage(message);
        if (board.length > 0) {
            addBoard(board);
        }
        addLegend(100, 5);
        addMenu(menuChoice, players);

        window = Paint.formatColors(window);

        clearScreen();
        System.out.print(window);
    }


    private void addMessage(String message) {
        String str = "│ " + message + "\n";
        str += "├" + "─".repeat(width) + "\n";
        addComponent(str, -1, 0);
    }

    private void addMenu(int menuChoice, String[] players) {
        String buttonsBar = "│ [0]Change view │ [1]Chat │ [2]Settings │\n";
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

        /*
        if (barChoice != -1) {
            String[] content = content_ChangeView().split("-");

            for (int c = 0, i = 0, j = 0; c < window.length(); c++, i++) {
                if (window.charAt(c) == '\n') {
                    String str;
                    if (j > 10) {
                        if (j - 10 < content.length) {
                            str = Paint.Space(WIDTH_WINDOW - i - buttonsBar.length()) + "│ " + content[j - 10] + Paint.Space(buttonsBar.length() - content[j - 10].length()) + "\n";
                        } else {
                            str = Paint.Space(WIDTH_WINDOW - i - buttonsBar.length()) + "│ " + Paint.Space(buttonsBar.length()) + "\n";
                        }
                        window.replace(c, c + 1, str);
                        c += str.length() + 1;
                    }

                    j++;
                    i = 0;
                }
            }
        }

        for (int i = 1; i < buttonsBar.length() - 1; i++){
            if (buttonsBar.charAt(i) == '│') {
                upperBar.insert(i, '┬');
            } else {
                upperBar.insert(i, '─');
            }
        }
         */

        String str = upperBar + buttonsBar + bottomBar;

        addComponent(str, width - buttonsBar.length() + 2, height - 2);

        if (menuChoice != -1) {
            String[] content = content_ChangeView(players).split("-");
            StringBuilder menu = new StringBuilder("╭" + "─".repeat(buttonsBar.length() - 3) + "┤\n");
            for (String line : content) {
                menu.append("│ ").append(line).append(Paint.Space(buttonsBar.length() - line.length() - 4)).append("│\n");
            }

            addComponent(menu.toString(), width - buttonsBar.length() + 2, height - 3 - content.length);
        }
    }

    private String content_ChangeView(String[] players) {
        StringBuilder str = new StringBuilder("[0] Board-");

        for (int i = 0; i < players.length; i++) {
            str.append("[").append(i + 1).append("] Shelf ").append(players[i]).append("-");
        }

        return str + "-";
    }

    private void addLegend(int x, int y) {
        String legend = "\n" +
                "╭───────────────╮\n" +
                "│ Legend:       │\n" +
                "│ " + "[G" + " = Cats     │\n" +
                "│ " + "[W" + " = Books    │\n" +
                "│ " + "[Y" + " = Games    │\n" +
                "│ " + "[B" + " = Frames   │\n" +
                "│ " + "[C" + " = Trophies │\n" +
                "│ " + "[M" + " = Plants   │\n" +
                "╰───────────────╯";

        addComponent(legend, x, y);
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

    private void addBoard(Tile[][] board) {
        StringBuilder window;

        window = new StringBuilder();
        if (board[0][0].isNone()) {
            window.append("     ");
        } else {
            window.append("╭────");
        }
        for (int j = 1; j < board[0].length; j++) {
            if (board[0][j].isNone() && board[0][j - 1].isNone()) {
                window.append("     ");
            } else if (!board[0][j].isNone() && board[0][j - 1].isNone()) {
                window.append("╭────");
            } else if (board[0][j].isNone() && !board[0][j - 1].isNone()) {
                window.append("╮    ");
            } else {
                window.append("┬────");
            }
        }
        window.append("\n");
        if (!board[0][0].isNone()) {
            window.append("│ ").append(paintTile(board[0][0])).append(" ");
        } else {
            window.append("     ");
        }
        for (int j = 1; j < board[0].length; j++) {
            if (board[0][j].isNone() && board[0][j - 1].isNone()) {
                window.append("     ");
            } else if (board[0][j].isEmpty() || (board[0][j].isNone() && !board[0][j - 1].isNone())) {
                window.append("│    ");
            } else {
                window.append("│ ").append(paintTile(board[0][j])).append(" ");
            }
        }
        window.append("\n");
        for (int i = 1; i < board.length; i++) {
            if (!board[i][0].isNone()) {
                if (!board[i - 1][0].isNone()) {
                    window.append("├────");
                } else {
                    window.append("╭────");
                }
            } else {
                if (!board[i - 1][0].isNone()) {
                    window.append("╰────");
                } else {
                    window.append("     ");
                }
            }

            for (int j = 1; j < board[i].length; j++) {
                //TODO semplifica ste condizioni
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

            if (!board[i][0].isNone()) {
                window.append("│ ").append(paintTile(board[i][0])).append(" ");
            } else {
                window.append("     ");
            }
            for (int j = 1; j < board[0].length; j++) {
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
        window.insert(0, "  ");
        for (int i = 0, j = board.length; i < window.length(); i++) {
            if (window.charAt(i) == '\n') {
                if (flag) {
                    window.insert(i + 1, j + " ");
                    j--;
                    flag = false;
                } else {
                    window.insert(i + 1, "  ");
                    flag = true;
                }
            }
        }

        for (char i = 'A'; i < board[0].length + 'A'; i++) {
            window.append("  ").append(i).append("  ");
        }

        addComponent(window.toString(), 25, height / 2 - window.toString().split("\n").length / 2);
    }

    private String paintTile(Tile tile) {
        //TODO valuta se è meglio hashmap che può essere salvata altrove insieme ad altro
        //String str = tile.type.toString().substring(0, 2);
        String str = "  ";

        switch (tile.type) {
            case CAT -> {
                return "[G";
            }
            case BOOK -> {
                return "[W";
            }
            case GAME -> {
                return "[Y";
            }
            case FRAME -> {
                return "[B";
            }
            case TROPHY -> {
                return "[C";
            }
            case PLANT -> {
                return "[M";
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

}
