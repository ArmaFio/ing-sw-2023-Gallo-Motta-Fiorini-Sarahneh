package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class InputHandler extends Thread implements Serializable {
    private final ViewCLI view;
    private boolean running;

    public InputHandler(ViewCLI view) {
        this.view = view;
        running = true;
        this.start();
    }

    private static boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (running) {
            input = scanner.nextLine().trim();
            switch (view.getGameState()) {
                case LOGIN -> {
                    if(!view.isUsernameSet()){
                        view.setUsername(input);
                        view.updateState();
                    } else if(!view.isPasswordSet()){
                        view.setPassword(input);
                        view.updateState();

                        Message response = new LoginResponse(view.getUsername(), input);
                        try {
                            view.write(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                case CREATE_JOIN -> {
                    Message response;
                    try {
                        switch (input) {
                            case "0" -> {
                                response = new CreateMessage(view.askLobbyDim());
                                view.write(response);
                                //view.updateState(GameState.INSIDE_LOBBY);
                            }
                            case "1" -> {
                                response = new Message(MessageType.JOIN);
                                view.write(response);
                                //view.updateState(GameState.LOBBY_CHOICE);
                            }
                            default -> {
                                Logger.error("Not an option");
                                view.setMessage("Not an option");
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case LOBBY_CHOICE -> {
                    System.out.println(input.length());
                    if (input.equals("")) {
                        view.updateState(GameState.CREATE_JOIN);
                    } else {
                        if (isNumeric(input) && view.lobbiesData.length > 0 && view.lobbiesData[0] != null && view.lobbiesData.length > Integer.parseInt(input)) {
                            Message response = new Message(MessageType.JOIN_LOBBY, view.lobbiesData[Integer.parseInt(input)].id);
                            try {
                                view.write(response);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Logger.info("Invalid lobby choice");
                            view.setMessage("Invalid lobby choice");
                            view.updateState();
                        }
                    }
                }
                case INSIDE_LOBBY -> {
                    switch (input) {
                        case "0" -> {
                            view.updateState(GameState.CREATE_JOIN);
                            Message response = new Message(MessageType.EXIT_LOBBY);
                            try {
                                view.write(response);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case "1" -> {
                            Message response = new Message(MessageType.START);
                            try {
                                view.write(response);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                case IN_GAME -> {
                    switch (view.getMenuValue()) {
                        case -1 -> {
                            if (isNumeric(input)) {
                                view.setMenuValue(Integer.parseInt(input));
                                view.updateState();
                            }
                        }
                        case 0 -> {
                            if (isNumeric(input)) {
                                view.setMenuValue(Integer.parseInt(input));
                                view.updateState();
                            } else if (input.length() == 1) {
                                view.setBoardViewed(input.charAt(0) - 'A');
                                view.updateState();
                            }
                        }
                        case 1 -> {
                            if (isNumeric(input) && Integer.parseInt(input) >= -1 && Integer.parseInt(input) <= 2) {
                                view.setMenuValue(Integer.parseInt(input));
                                view.updateState();
                            } else {
                                try { //TODO togli la possibilità di fare altro
                                    view.write(new StringMessage(input));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    switch (view.getPhase()) {
                        case TILES_REQUEST -> {
                            String[] strings = input.split(" ");
                            ArrayList<Integer[]> coordinate = new ArrayList<>();
                            int row, col;

                            for (String str : strings) {
                                if (str.length() == 2) {
                                    if (isNumeric(String.valueOf(str.charAt(0)))) {
                                        row = Integer.parseInt(String.valueOf(str.charAt(0))) - 1;
                                        if (str.charAt(1) >= 'a' && str.charAt(1) <= 'i') {
                                            col = str.charAt(1) - 'a';
                                            coordinate.add(new Integer[]{row, col});
                                        } else if (str.charAt(1) >= 'A' && str.charAt(1) <= 'I') {
                                            col = str.charAt(1) - 'A';
                                            coordinate.add(new Integer[]{row, col});
                                        }
                                    } else if (isNumeric(String.valueOf(str.charAt(1)))) {
                                        row = Integer.parseInt(String.valueOf(str.charAt(1))) - 1;
                                        if (str.charAt(0) >= 'a' && str.charAt(0) <= 'i') {
                                            col = str.charAt(0) - 'a';
                                            coordinate.add(new Integer[]{row, col});
                                        } else if (str.charAt(0) >= 'A' && str.charAt(0) <= 'I') {
                                            col = str.charAt(0) - 'A';
                                            coordinate.add(new Integer[]{row, col});
                                        }
                                    }
                                } else {
                                    break;
                                }
                            }

                            if (view.getNumPlayers() == 2) {
                                for (Integer[] c : coordinate) {
                                    c[0]++;
                                    c[1]++;
                                }
                            }

                            Tile[] tiles = new Tile[coordinate.size()];
                            for (int i = 0; i < coordinate.size(); i++) {
                                tiles[i] = view.getTileFromBoard(coordinate.get(i)[0], coordinate.get(i)[1]); //TODO attento a out o bound
                            }

                            boolean flag = false;
                            for (Tile[] v : view.getAvailableTiles()) {
                                flag = true;
                                if (tiles.length == v.length) { //TODO serve?
                                    for (int i = 0; i < tiles.length; i++) {
                                        if (!tiles[i].equalsId(v[i])) {
                                            flag = false;
                                        }
                                    }
                                } else {
                                    flag = false;
                                }

                                if (flag) {
                                    TilesResponse response = new TilesResponse(tiles);
                                    try {
                                        view.write(response);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                }

                            }

                            if (!flag) {
                                view.setMessage("Combinazione non valida");
                            }
                        }
                        case COLUMN_REQUEST -> {
                            boolean ok = false;
                            int col = -1;
                            if (input.length() == 1) {
                                if (input.charAt(0) >= 'a' && input.charAt(0) <= 'e') {
                                    col = input.charAt(0) - 'a';
                                } else if (input.charAt(0) >= 'A' && input.charAt(0) <= 'E') {
                                    col = input.charAt(0) - 'A';
                                }
                                for (int i : view.getAvailableColumns()) {
                                    if (i == col) {
                                        ok = true;
                                        break;
                                    }
                                }
                            }

                            if (ok) {
                                ColumnResponse response = new ColumnResponse(col);
                                try {
                                    view.write(response);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                view.setMessage("Invalid choice, retry");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Disconnects the client.
     */
    void disconnect() {
        view.disconnect();
        running = false;
    }
}
