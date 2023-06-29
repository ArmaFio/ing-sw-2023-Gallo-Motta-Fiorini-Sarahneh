package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.ChatMessage;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.Test;

public class ClientViewTest {


    @Test
    public void shelfWindowTest() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 1},
                {5, 6, 4, 5, 1},
                {3, 3, 3, 1, 1},
                {5, 1, 1, 1, 1},
                {5, 1, 1, 2, 3},
                {5, 3, 3, 2, 5}
        };
        Tile[][] shelf = convert_to_shelf(matrix);
        FrameCLI frame = new FrameCLI(150, 25);
        String description = "Six groups each containing at least " +
                "2 tiles of the same type (not necessarily " +
                "in the depicted shape). " +
                "The tiles of one group can be different " +
                "from those of another group.";
        frame.paintWindow("ciao", shelf, new String[]{description +"t", description}, new String[]{"pippo", "pluto", "paperinosrhfestnrsythxd"}, 0);
    }

    @Test
    public void boardWindowTest() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 3, 4, 0, 0, 0, 0},
                {0, 0, 0, 2, 2, 4, 0, 0, 0},
                {0, 0, 3, 2, 2, 2, 3, 0, 0},
                {0, 4, 2, 2, 2, 2, 2, 2, 1},
                {4, 2, 2, 2, 2, 2, 2, 2, 0},
                {3, 2, 2, 2, 2, 2, 2, 4, 0},
                {0, 0, 3, 2, 2, 2, 3, 0, 0},
                {0, 0, 0, 4, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 4, 3, 0, 0, 0}
        };
        Tile[][] shelf = convert_to_shelf(matrix);
        FrameCLI frame = new FrameCLI(150, 25);
        String description = "Six groups each containing at least " +
                "2 tiles of the same type (not necessarily" +
                "in the depicted shape). " +
                "The tiles of one group can be different " +
                "from those of another group.";
        frame.paintWindow("ciao", shelf, new String[]{"pippo", "pluto", "paperinosrhfestnrsythxd"}, -1);
    }

    @Test
    public void chatWindowTest() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 3, 4, 0, 0, 0, 0},
                {0, 0, 0, 2, 2, 4, 0, 0, 0},
                {0, 0, 3, 2, 2, 2, 3, 0, 0},
                {0, 4, 2, 2, 2, 2, 2, 2, 1},
                {4, 2, 2, 2, 2, 2, 2, 2, 0},
                {3, 2, 2, 2, 2, 2, 2, 4, 0},
                {0, 0, 3, 2, 2, 2, 3, 0, 0},
                {0, 0, 0, 4, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 4, 3, 0, 0, 0}
        };
        Tile[][] shelf = convert_to_shelf(matrix);
        FrameCLI frame = new FrameCLI(150, 25);
        String description = "Six groups each containing at least " +
                "2 tiles of the same type (not necessarily" +
                "in the depicted shape). " +
                "The tiles of one group can be different " +
                "from those of another group.";
        frame.setChat(new ChatMessage[]{
                new ChatMessage("pippo", "sgvrwgvervg"),
                new ChatMessage("ervgerbgre g57euh gr shbt rh btr sgbrt fdgbtftrf b htrfg sbh tr fgbtsfb  trsg b rt bgstrb tr gv tre vg ter gt g tr gvt gverbherg"),
                new ChatMessage("pippo", "tefhbtrdtbhtretrbh"),
                new ChatMessage("pippo", "tbhtrfbtr bhtrbhtrbn")});
        frame.paintWindow("ciao", shelf, new String[]{"pippo", "pluto", "paperinosrhfestnrsythxd"}, 1);
    }

    Tile[][] convert_to_shelf(int[][] matrix) {
        Tile[][] shelf = new Tile[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                shelf[i][j] = new Tile(TileType.toEnum(matrix[i][j]));
            }
        }

        return shelf;
    }
}