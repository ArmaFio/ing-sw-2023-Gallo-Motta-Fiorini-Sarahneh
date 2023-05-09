package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.ColumnResponse;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.messages.TilesResponse;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputHandler extends Thread {
    private final ClientView view;
    private ArrayList<Character> buffer;

    public InputHandler(ClientView view) {
        this.view = view;
        start();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine();
            switch (view.getGameState()) {
                case CREATE_JOIN -> {
                    Message response;
                    try {
                        if (input.equals("0")) {
                            response = new Message(MessageType.CREATE);
                            view.write(response);
                            //view.updateState(GameState.INSIDE_LOBBY);
                        } else {
                            if (input.equals("1")) {
                                response = new Message(MessageType.JOIN);
                                view.write(response);
                                //view.updateState(GameState.LOBBY_CHOICE);
                            } else {
                                Logger.error("Not an option");
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case LOBBY_CHOICE -> {
                    switch (input) {
                        case "/back" -> view.updateState(GameState.CREATE_JOIN);
                        case "/update" -> Logger.info("Qualcosa");
                        default -> {
                            if (view.lobbiesData.length > 0 && view.lobbiesData[0] != null && view.lobbiesData.length > Integer.parseInt(input)) {
                                Message response = new Message(MessageType.JOIN_LOBBY, view.lobbiesData[Integer.parseInt(input)].id);
                                try {
                                    view.write(response); //TODO cambia
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Logger.info("Invalid lobby choice");
                                view.updateState(GameState.LOBBY_CHOICE);
                            }
                        }
                    }
                }
                case INSIDE_LOBBY -> {
                    switch (input) {
                        case "/start" -> {
                            Message response = new Message(MessageType.START);
                            try {
                                view.write(response);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case "/chat" -> {

                        }
                    }
                }
                case IN_GAME -> {
                    switch (view.getPhase()) {
                        case WAIT -> {
                            switch (input) {
                                case "1" -> {
                                    //TODO stampa a schermo le common goal cards
                                    System.out.println("0) Back to menu");
                                    int j;
                                    do {
                                        j = scanner.nextInt();
                                    } while (j != 0);
                                }
                                case "2" -> {
                                    System.out.println(view.shelfWindow(view.getPersonalgoal()));
                                    System.out.println("0) Back to menu");
                                    int j;
                                    do {
                                        j = scanner.nextInt();
                                    } while (j != 0);
                                }
                                case "3" -> {
                                    System.out.println(ClientView.shelfWindow(view.getShelves().get(view.getUsername())));
                                    System.out.println("0) Back to menu");
                                    int j;
                                    do {
                                        j = scanner.nextInt();
                                    } while (j != 0);
                                }
                                case "4" -> {
                                    for (String name : view.getShelves().keySet()) {
                                        if (!Objects.equals(name, view.getUsername()))
                                            System.out.println(ClientView.shelfWindow(view.getShelves().get(name)));
                                    }
                                    System.out.println("0) Back to menu");
                                    int j;
                                    do {
                                        j = scanner.nextInt();
                                    } while (j != 0);
                                }
                                case "/chat" -> {

                                }
                                default -> {
                                }
                            }

                        }
                        case TILES_REQUEST -> {
                            if (!input.equals("") && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= view.getAvailableTiles().length) {
                                int index = Integer.parseInt(input);
                                int n = view.getAvailableTiles()[Integer.parseInt(input) - 1].length;
                                //TODO Dire al client che deve scegliere l'ordine delle tessere e mostrare come indicarlo
                                System.out.println("Tiles combination chosen: ");
                                for (int i = 0; i < n; i++) {
                                    System.out.print(view.getAvailableTiles()[Integer.parseInt(input) - 1][i]);
                                    System.out.print(", ");
                                }
                                System.out.println(" ");
                                System.out.println("Pleas choose the order in which you would like to insert the tiles in the shelf!");
                                ArrayList<Integer> selected = new ArrayList<>();
                                Tile[] ordered = new Tile[n];
                                for (int i = 0; i < n; i++) {
                                    int j = scanner.nextInt();
                                    if (!selected.contains(j) && j > 0 && j <= n) {
                                        selected.add(j);
                                        //ordered[i] = view.getAvailableTiles()[Integer.parseInt(input)][i];
                                        //i++;
                                    } else {
                                        System.out.println("Already Chosen/Not Valid");
                                        i--;
                                    }
                                }
                                for (int i = 0; i < n; i++) {
                                    ordered[i] = view.getAvailableTiles()[index - 1][selected.get(0) - 1];
                                    selected.remove(0);
                                }
                                TilesResponse response = new TilesResponse(ordered);
                                try {
                                    view.write(response);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (!input.equals("")) {
                                System.out.println("Not An Option");
                            }
                        }
                        case COLUMN_REQUEST -> {
                            boolean ok = false;
                            for (int i : view.getAvailableColumns()) {
                                if (i == Integer.parseInt(input))
                                    ok = true;
                            }
                            if (ok) {
                                ColumnResponse response = new ColumnResponse(Integer.parseInt(input));
                                try {
                                    view.write(response);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else
                                System.out.println("Unvalid Choice, retry");

                        }
                    }
                }
            }
        }
    }

    private String getBufferAsString() {
        return buffer.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }

}
