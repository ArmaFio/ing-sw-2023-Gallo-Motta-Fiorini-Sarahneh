package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PersonalGoalCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Scanner;

/**
 * @author Armando Fiorini
 */
public class ClientView implements EventListener {
    private final Scanner clientInput;
    private final ArrayList<String> names;
    private Board gameBoard;
    private CommonGoalCard[] commonCards;
    private Player p;
    private int[] points;

    public ClientView() {
        clientInput = new Scanner(System.in);
        names = new ArrayList<>();
        commonCards = new CommonGoalCard[2];
    }

    public void setPlayer(Player player) {
        this.p = player;
    }

    public void setOthers(ArrayList<Player> players) {
        points = new int[players.size()];
        for (Player player : players) {
            names.add(player.getUsername());
            points[players.indexOf(player)] = 0;
        }
    }

    /**
     * Implements the dialog between the
     *
     * @param AvailableTiles ArrayList containing all the possible combinations by 1,2 and 3 tiles that can be taken from the board
     * @return Am ArrayList containing the chosen combination
     */
    public ArrayList<Tile> v_pick_tiles(ArrayList<ArrayList<Tile>> AvailableTiles) {
        int i;
        System.out.println("Which tiles do you want to take?");
        for (ArrayList<Tile> Combination : AvailableTiles) {
            System.out.println("\n" + AvailableTiles.indexOf(Combination) + ") " + Combination.toString());
        }
        i = clientInput.nextInt();
        while (!(i >= 0 && i < AvailableTiles.size())) {
            System.out.println("Unvalid Choice, try again");
            i = clientInput.nextInt();
        }
        gameBoard.removeTiles(AvailableTiles.get(i));
        return AvailableTiles.get(i);
    }

    /**
     * @param selected Contains the tiles which have to be inserted in selection order, that is not necessarily the insertion one
     * @return the coloumn in which the player wants the tiles to be put
     */
    public int v_put_tiles(ArrayList<Tile> selected) {
        int i;
        int[] j = new int[selected.size()];
        ArrayList<Tile> orderedTiles = new ArrayList<>();
        System.out.println("Which coloumn do you want to insert the tiles in?");
        System.out.println(p.getShelf().available_columns(selected.size()).toString());
        i = clientInput.nextInt();
        System.out.println("Select the order you want to put the Tiles in");
        for (Tile T : selected)
            System.out.println(selected.indexOf(T) + ") " + T.toString());
        for (int k = 0; k < selected.size(); k++)
            j[k] = clientInput.nextInt();
        for (int k = 0; k < selected.size(); k++)
            orderedTiles.add(selected.get(j[k]));
        selected.clear();
        selected.addAll(orderedTiles);//TODO verificare che modifichi effettivamente la lista nel chiamante
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

    public CommonGoalCard[] getCommonCards() {
        return commonCards;
    }

    public void setCommonCards(CommonGoalCard[] commonCards) {
        this.commonCards = commonCards;
    }

    public PersonalGoalCard getPgc() {
        return p.pgc;
    }

    /**
     * Prints the board
     */
    public void print_board() {
        System.out.println("\n" + gameBoard.toString());
    }

    /**
     * Prints player's shelf
     */
    public void print_shelf() {
        System.out.println("\n" + p.getShelf().toString());
    }

    /**
     * Prints player's personal objectives
     */
    public void print_pgc() {
        System.out.println("\n" + p.pgc.toString());
    }

    /**
     * Prints common Objectives of the game
     */
    public void print_common_goal_cards() {
        System.out.println("\nCommon Objective 1:\n" + commonCards[0].toString() + "\n Common Objective 2:\n" + commonCards[1].toString());
    }

    public void print_points() {
        System.out.println("\n" + p.getUsername() + " : " + p.getPoints() + " points");
        for (int i = 0; i < points.length; i++) {
            System.out.println("\n" + names.get(i) + " :" + points[i] + "points");
        }
    }

    public void print_names() {
        for (String name : names)
            System.out.println("\n" + name);
    }

    public void declare_winner(String winner) {
        System.out.println("\nThe winner is: " + winner);
    }

    public boolean askJoinOrCreate() {
        String choice;
        do {
            Logger.info("Choose an option:\n[0] Join Lobby\n[1] Create Lobby");
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

    public int askLobby(int[] lobbiesDim) {
        String choice;
        boolean correct = false;

        Logger.info("Choose a Lobby:");
        for (int i = 0; i < lobbiesDim.length; i++) {
            Logger.info("[" + i + "] " + lobbiesDim[i] + "/4");
        }

        choice = clientInput.nextLine().trim();
        for (int i = 0; i < lobbiesDim.length; i++) {
            if (Integer.toString(i).equals(choice)) {
                correct = true;
                break;
            }
        }

        while (!correct) {
            Logger.warning("Not a choice. Retry.");
            choice = clientInput.nextLine().trim();
            for (int i = 0; i < lobbiesDim.length; i++) {
                if (Integer.toString(i).equals(choice)) {
                    correct = true;
                    break;
                }
            }
        }

        return Integer.parseInt(choice);
    }
}
