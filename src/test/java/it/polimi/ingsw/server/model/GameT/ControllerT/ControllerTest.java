package it.polimi.ingsw.server.model.GameT.ControllerT;

import it.polimi.ingsw.messages.GameUpdate;
import it.polimi.ingsw.messages.StartMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.SocketClientHandler;
import it.polimi.ingsw.server.User;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    @Test
    void createStart() {
        ClientHandler c = new SocketClientHandler();
        User x = new User("x", "y", c);
        Lobby l= new Lobby(1, x,4);
        String [] s={"x","a","b","c"};
        TestableController con= new TestableController(l,s);
        Game g = con.getGame();
        StartMessage m = new StartMessage(g.getPlayer("x").pgc.getMatrix(), new HashMap<>(), -1);
        assertArrayEquals(m.getPersonalGoal(),con.createStart("x").getPersonalGoal());
    }

    @Test
    void CreateUpdate(){
        ClientHandler c = new SocketClientHandler();
        User x = new User("x", "y", c);
        Lobby l= new Lobby(1, x,4);
        String [] s={"x","a","b","c"};
        TestableController con= new TestableController(l,s);
        Game g= con.getGame();
        GameUpdate mes= new GameUpdate("x");
        for(String pl :s)
            mes.addShelf(pl,g.getPlayer(pl).getShelf());
        mes.setBoard(g.getBoard());
        mes.setCommonGoals(g.getCommonGoalsUpdate());
        GameUpdate upd=con.createUpdateMessage("x");
        for(int p=0; p<9; p++) {
            for (int q = 0; q < 9; q++) {
                assertTrue(mes.getBoard()[p][q].equalsId(upd.getBoard()[p][q]));
                assertTrue(mes.getBoard()[p][q].equalsType(upd.getBoard()[p][q]));
            }
        }
        for(String i: s)
            assertArrayEquals(mes.getShelves().get(i),upd.getShelves().get(i));
        assertEquals(mes.getCommonGoals().keySet(),upd.getCommonGoals().keySet());
        for (Integer k : mes.getCommonGoals().keySet()) {
            assertEquals(mes.getCommonGoals().get(k).keySet(), upd.getCommonGoals().get(k).keySet());
            for (String j : mes.getCommonGoals().get(k).keySet())
                assertEquals(mes.getCommonGoals().get(k).get(j), upd.getCommonGoals().get(k).get(j));
        }
        assertEquals(mes.getPlayerTurn(), upd.getPlayerTurn());
    }

    @Test
    void filterTest(){
        ClientHandler cl = new SocketClientHandler();
        User x = new User("x", "y", cl);
        Lobby l= new Lobby(1, x,4);
        String [] s={"x","a","b","c"};
        TestableController con= new TestableController(l,s);
        Tile a= new Tile(TileType.CAT);
        Tile b= new Tile(TileType.PLANT);
        Tile c= new Tile(TileType.TROPHY);
        Tile n= new Tile(TileType.EMPTY);
        Tile[] A= {a};
        Tile[] B= {a,b};
        Tile[] C= {a,b,c};
        Tile[][] input={A,B,C};
        Tile[][] matrix={{n,n,n,n,n},{n,n,n,n,n},{a,a,a,a,a},{a,a,a,a,a},{b,b,b,b,b},{c,c,c,c,c}};
        con.getGame().getPlayer("x").getShelfObj().setMatrix(matrix);
        Tile[][] filtered= con.filter(input);
        boolean contains= false;
        for (Tile[] tiles : filtered) {
            if (Arrays.equals(tiles, C)) {
                contains = true;
                break;
            }
        }
        assertFalse(contains);
        matrix= new Tile[][]{{n, n, n, n, n}, {a, a, a, a, a}, {a, a, a, a, a}, {a, a, a, a, a}, {b, b, b, b, b}, {c, c, c, c, c}};
        con.getGame().getPlayer("x").getShelfObj().setMatrix(matrix);
        filtered= con.filter(input);
        for (Tile[] tiles : filtered) {
            if (Arrays.equals(tiles, C)||Arrays.equals(tiles,B)) {
                contains = true;
                break;
            }
        }
        assertFalse(contains);
        matrix= new Tile[][]{{b, b, b, b, b}, {a, a, a, a, a}, {a, a, a, a, a}, {a, a, a, a, a}, {b, b, b, b, b}, {c, c, c, c, c}};
        con.getGame().getPlayer("x").getShelfObj().setMatrix(matrix);
        filtered= con.filter(input);
        for (Tile[] tiles : filtered) {
            if (Arrays.equals(tiles, C)||Arrays.equals(tiles,B)||Arrays.equals(tiles,A)) {
                contains = true;
                break;
            }
        }
        assertFalse(contains);
        matrix= new Tile[][]{{n,n,n,n,n},{n,n,n,n,n},{n,n,n,n,n},{n,n,n,n,n},{n,n,n,n,n},{n,n,n,n,n}};
        con.getGame().getPlayer("x").getShelfObj().setMatrix(matrix);
        filtered= con.filter(input);
        assertArrayEquals(input,filtered);
    }
}
