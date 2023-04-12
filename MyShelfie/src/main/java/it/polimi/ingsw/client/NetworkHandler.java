package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utils.Logger;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkHandler extends Thread {
    private boolean connected = false; //TODO fai locale
    private boolean firstTime = true; //TODO fai locale
    private boolean running = true;
    @Deprecated
    private Scanner sc = new Scanner(System.in);
    private String user;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    @Deprecated
    private BufferedReader reader;
    @Deprecated
    private PrintWriter writer;
    private ClientView view;

    public static void main(String[] args) throws InterruptedException {
        NetworkHandler nh = new NetworkHandler();
        nh.start();
    }

    @Override
    public void run() {
        String username, password;
        LoginResponse login;
        Message response;
        System.out.println("Welcome to MyShelfie!\nPlease wait while we connect you to the server!");
        view = new ClientView();
        //try until connection succeeds.
        connect();
        //start listening for server instructions
        while (running) {
            try (Message message = read()) {

                switch (message.getType()) {
                    case NONE:
                        break;
                    case LOBBY_JOINED:
                        String[] lobbyUsers = ((LobbyJoined) message).getLobbyUsers();
                        Logger.warning("joined succeed\nUsers in lobby:\n");
                        for (String str : lobbyUsers) {
                            Logger.info(str + '\n');
                        }
                        break;
                    case LOBBY_LIST:
                        int[] lobbiesDim = ((LobbyList) message).lobbiesDim;

                        if (lobbiesDim.length > 0) {
                            int lobbyId = view.askLobby(lobbiesDim);
                            response = new Message(lobbyId);
                            response.setType(ResponseType.JOIN_LOBBY);
                            write(response);
                        } else {
                            Logger.warning("Non ci sono ancora lobby");
                        }
                        break;
                    case JOIN_SUCCESS:

                        break;
                    case JOIN_FAILURE:
                        Logger.warning("joined failed");
                        //TODO manda un'altra richiesta
                        break;
                    case TILES:
                        TilesRequest res = (TilesRequest) message;
                        ArrayList<Tile> sel = view.v_pick_tiles(res.getAvailable());
                        int i = view.v_put_tiles(sel);
                        res.setChosen(sel);
                        res.setCol(i);
                        write(res);
                        break;
                    case CURSOR:
                        break;
                    case START:
                        view = new ClientView();
                        break;
                    case STRING:
                        //TODO this will be probably  used for the chat
                        StringRequest line = (StringRequest) message;
                        System.out.println(line.message());
                        break;
                    case LOGIN_RESPONSE:
                        break;
                    case LOGIN_FAILURE:
                        Logger.info(user + " non corretto!");
                        System.out.println("Enter your username:"); //TODO askCredential() di view
                        username = sc.nextLine().trim();
                        System.out.println("Enter the password");
                        password = sc.nextLine().trim();
                        login = new LoginResponse(username, password);
                        user = username;
                        write(login);
                        break;
                    case LOGIN_SUCCESS:
                        Logger.info(user + " connesso");

                        if (view.askJoinOrCreate()) {//false join create true
                            response = new Message(ResponseType.CREATE);
                        } else {
                            response = new Message(ResponseType.JOIN);
                        }

                        write(response);
                        break;
                    case LOGIN_REQUEST:
                        System.out.println("Enter your username:");
                        username = sc.nextLine().trim();
                        System.out.println("Enter the password");
                        password = sc.nextLine().trim();
                        login = new LoginResponse(username, password);
                        user = username;
                        Logger.debug(login.getAuthor() + login.getPassword());
                        write(login);
                        break;
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Error: Class not found!");
            } catch (IOException e) {
                System.out.println("Connection to the server lost, trying to reconnect...");
                connect();
            }
        }
    }

    /**
     * Creates a connection between client and server, keeps trying until success.
     */
    private void connect() {
        connected = false;
        while (!connected) {
            try {
                Socket socket = new Socket("127.0.0.1", 59090);
                InputStream input = socket.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                outputStream = new ObjectOutputStream(output);
                writer = new PrintWriter(output, true);
                inputStream = new ObjectInputStream(input);
                firstTime = true;
                connected = true;
                Logger.info("Connection established!");
            } catch (IOException e) {
                if (firstTime) {
                    Logger.warning("Cannot connect to the server, keep trying...");
                    firstTime = false;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException i) {
                    Logger.error("InterruptedException occurred!");
                }
            }
        }
    }

    /**
     * Reads a serialized object received from the client.
     *
     * @return the object read.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Message read() throws ClassNotFoundException, IOException {
        return (Message) inputStream.readObject();
    }

    /**
     * Writes a serialized object and sends it to the client.
     *
     * @param obj the object we want to send to the client.
     * @throws IOException
     */
    public void write(Message obj) throws IOException {
        obj.setAuthor(user);
        outputStream.writeObject(obj);
    }
}
    //public void send(String s) {
      //  writer.println(s);
    //}

   // public String read() throws IOException {
     //   return reader.readLine();
    //}

    //public void close() throws IOException {
      //  socket.close();
    //}
