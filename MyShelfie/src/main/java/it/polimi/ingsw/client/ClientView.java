package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.LobbyList;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.ResponseType;
import it.polimi.ingsw.messages.UpdateState;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.server.model.shelf.Shelf;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Scanner;

/**
 * @author Armando Fiorini
 */
public class ClientView implements EventListener, Runnable {
    private final Scanner clientInput;
    private final NetworkHandler client;
    private GameState state;
    private Board gameBoard;
    private ArrayList<CommonGoalCard> commonCards;
    private Player p;
    private Player[] otherPlayers;
    private String turnHandler;
    private boolean running;
    private LobbyList.LobbyData[] lobbiesData;

    public ClientView(NetworkHandler client) {
        this.client = client;
        state = GameState.LOGIN;
        clientInput = new Scanner(System.in);
    }

    public void setGame(Game currGame, String user) {
        int j = 0;
        otherPlayers = new Player[(currGame.getPlayers().length) - 1];
        for (int i = 0; i < currGame.getPlayers().length; i++) {
            if (currGame.getPlayers()[i].getUsername().equals(user))
                p = currGame.getPlayers()[i];
            else {
                otherPlayers[j] = p = currGame.getPlayers()[i];
                j++;
            }
        }
        commonCards = currGame.getCommonObjs();
        gameBoard = currGame.getBoard();
        turnHandler = currGame.getPlayers()[0].getUsername();
    }

    @Override
    public void run() {
        System.out.println("Game Started");
        running = true;
        Thread turn = new Thread(new Runnable() {
            @Override
            public void run() {
                turn();
            }
        });
        while (running) {
            if (turnHandler.equals(p.getUsername())) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                turn.start();
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                turn.interrupt();
            }
        }
    }



    /*
    public void inputManager(){
        //InputManager
        while(true){
            String input = clientInput.nextLine().trim();
            switch(this.state){
                case
            }
        }
    }
    */

    /**
     * Implements the dialog between the
     *
     * @param AvailableTiles ArrayList containing all the possible combinations by 1,2 and 3 tiles that can be taken from the board
     * @return Am ArrayList containing the chosen combination
     */
    public int vPickTiles(ArrayList<ArrayList<Tile>> AvailableTiles) {
        int i, col;
        ArrayList<Tile> selected;
        System.out.println("Which tiles do you want to take?");
        for (ArrayList<Tile> Combination : AvailableTiles) {
            System.out.println(AvailableTiles.indexOf(Combination) + ") " + Combination.toString());
        }
        i = clientInput.nextInt();
        while (!(i >= 0 && i < AvailableTiles.size())) {
            System.out.println("Unvalid Choice, try again");
            i = clientInput.nextInt();
        }
        gameBoard.removeTiles(AvailableTiles.get(i));
        selected = AvailableTiles.get(i);
        col = vPutTiles(selected);
        AvailableTiles.clear();
        AvailableTiles.add(selected);
        System.out.println(p.getShelf().toString());
        return col;
    }

    /**
     * @param selected Contains the tiles which have to be inserted in selection order, that is not necessarily the insertion one
     * @return the coloumn in which the player wants the tiles to be put
     */
    public int vPutTiles(ArrayList<Tile> selected) {
        int i, err = 0;
        ArrayList<Integer> indexes = new ArrayList<>();
        ArrayList<Tile> orderedTiles = new ArrayList<>();
        System.out.println("Which column do you want to insert the tiles in?");
        System.out.println(p.getShelf().available_columns(selected.size()).toString());
        i = clientInput.nextInt();
        while (!(p.getShelf().available_columns(selected.size()).contains(i))) {
            System.out.println("Error: unavailable column selected");
            i = clientInput.nextInt();
        }
        System.out.println("Select the order you want to put the Tiles in");
        for (Tile T : selected)
            System.out.println(selected.indexOf(T) + ") " + T.toString());
        do {
            for (int k = 0; k < selected.size(); k++)
                indexes.add(clientInput.nextInt());
            for (int w = 0; w < selected.size() && err == 0; w++) {
                if (!(indexes.contains(w))) {
                    err = 1;
                    System.out.println("Error: invalid order specified, try again");
                }
            }
        } while (err == 1);
        for (int k = 0; k < selected.size(); k++)
            orderedTiles.add(selected.get(indexes.get(k)));
        selected.clear();
        selected.addAll(orderedTiles);
        p.getShelf().put_tiles(i, orderedTiles);
        return i;
    }

    public Shelf getShelf() {
        return p.getShelf();
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    public ArrayList<CommonGoalCard> getCommonCards() {
        return commonCards;
    }

    public void setCommonCards(ArrayList<CommonGoalCard> commonCards) {
        this.commonCards = commonCards;
    }

    public PersonalGoalCard getPgc() {
        return p.pgc;
    }

    /**
     * Prints the board
     */
    public void print_board() {
        System.out.println(gameBoard.toString());
    }

    /**
     * Prints player's shelf
     */
    public void print_shelf() {
        System.out.println(p.getShelf().toString());
    }

    /**
     * Prints player's personal objectives
     */
    public void print_pgc() {
        System.out.println(p.pgc.toString());
    }

    /**
     * Prints common Objectives of the game
     */
    public void print_common_goal_cards() {
        System.out.println("Common Objective 1:\n" + commonCards.get(0).toString() + "\n Common Objective 2:\n" + commonCards.get(1).toString());
    }

    public void print_points() {
        System.out.println(p.getUsername() + " : " + p.getPoints() + " points");
        System.out.println("You: " + p.getPoints() + "Points");
        for (Player otherPlayer : otherPlayers) {
            System.out.println(otherPlayer.getUsername() + " :" + otherPlayer.getPoints() + "points");
        }
    }

    public void print_names() {
        System.out.println(p.getUsername());
        for (Player pl : otherPlayers)
            System.out.println(pl.getUsername());
    }

    public void declare_winner(String winner) {
        System.out.println("The winner is: " + winner);
    }

    public boolean askJoinOrCreate() {
        String choice;
        do {
            System.out.println("Choose an option:\n[0] Join Lobby\n[1] Create Lobby");
            choice = clientInput.nextLine().trim();
            if (choice.equals("0")) {
                return false;
            }
            if (choice.equals("1")) {
                return true;
            } else {
                Logger.error("Not an option");
            }
        } while (true);
    }

    public int askLobby(LobbyList.LobbyData[] lobbiesData) {
        String choice;
        boolean correct = false;

        Logger.info("Choose a Lobby:");
        for (LobbyList.LobbyData l : lobbiesData) {
            Logger.info("[" + l.id + "] " + l.admin + "'s lobby | " + l.capacity + "/4");
        }

        choice = clientInput.nextLine().trim();
        for (int i = 0; i < lobbiesData.length; i++) {
            if (Integer.toString(lobbiesData[i].id).equals(choice)) {
                correct = true;
                break;
            }
        }

        while (!correct) {
            Logger.warning("Not a choice. Retry.");
            choice = clientInput.nextLine().trim();
            for (int i = 0; i < lobbiesData.length; i++) {
                if (Integer.toString(lobbiesData[i].id).equals(choice)) {
                    correct = true;
                    break;
                }
            }
        }

        return Integer.parseInt(choice);
    }

    public void onLobbyListMessage(LobbyList msg) {
        try {
            this.lobbiesData = msg.lobbiesData;
            if (msg.lobbiesData.length > 0) {
                int lobbyId = askLobby(msg.lobbiesData);
                //InputManager
                Message response = new Message(lobbyId);
                response.setType(ResponseType.JOIN_LOBBY);
                client.write(response);
            } else {
                Logger.warning("Non ci sono ancora lobby");
            }
            //updateState(GameState.LOBBY_CHOICE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GameState getGameState() {
        return state;
    }

    public void updateState(GameState newState) throws IOException {
        this.state = newState;
        Message msg = new UpdateState(this.state);
        client.write(msg);
    }

    public void turn() {
        boolean loop = true;
        do {
            int i;
            System.out.println(gameBoard.toString());
            System.out.println("Menù: \n1)Show Common Objective\n2)Show Personal Objective\n3)Your Shelf\n4)Other Shelves\n");
            i = clientInput.nextInt();
            switch (i) {
                case 1 -> {
                    for (CommonGoalCard c : commonCards)
                        System.out.println(c.toString());
                    System.out.println("0) Back to menù");
                    int a;
                    do {
                        a = clientInput.nextInt();
                    } while (a != 0);
                }
                case 2 -> {
                    System.out.println(p.pgc.toString());
                    System.out.println("0) Back to menù");
                    int b;
                    do {
                        b = clientInput.nextInt();
                    } while (b != 0);
                }
                case 3 -> {
                    System.out.println(p.getShelf().toString());
                    System.out.println("0) Back to menù");
                    int c;
                    do {
                        c = clientInput.nextInt();
                    } while (c != 0);
                }
                case 4 -> {
                    for (Player pl : otherPlayers)
                        System.out.println(p.getUsername() + "\n" + pl.getShelf().toString());
                    System.out.println("0) Back to menù");
                    int d;
                    do {
                        d = clientInput.nextInt();
                    } while (d != 0);
                }
                default -> {
                }
            }
        } while (loop);
    }

    synchronized public void update(Game game, String turnHandler) {
        setGame(game, p.getUsername());
        this.turnHandler = turnHandler;
        notifyAll();
    }
}
