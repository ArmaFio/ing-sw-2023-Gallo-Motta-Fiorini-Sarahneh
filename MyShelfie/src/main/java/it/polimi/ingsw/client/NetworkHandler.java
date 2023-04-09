package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Tile;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkHandler extends Thread {
    private boolean connected = false;
    private boolean firstTime = true;
    private boolean closeClient = false;
    private Scanner sc = new Scanner(System.in);
    private Message message;
    private String user;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private BufferedReader reader;
    private PrintWriter writer;
    private ClientView view;

    public static void main(String[] args) throws InterruptedException {
        NetworkHandler nh = new NetworkHandler();
        nh.start();
    }

    @Override
    public void run() {
        System.out.println("Welcome to MyShelfie!\nPlease wait while we connect you to the server!");
        //try until connection succeeds.
        connect();
        //start listening for server instructions
        while (!closeClient) {
            try {
                message = read();
                switch (message.getType()) { //TODO alcuni case probabilmente non servono.
                    case NONE:
                    case LOBBY_LIST:
                        //TODO fai segliere un lobby
                        break;
                    case JOIN_SUCCESS:
                        //TODO prendi dati lobby
                        break;
                    case JOIN_FAILURE:
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
                    case START:
                        view = new ClientView();
                        break;
                    case STRING:
                        //TODO this will be probably  used for the chat (stoca, chiamalo CHAT)
                        StringRequest line = (StringRequest) message;
                        System.out.println(line.user() + " " + line.message());
                    case LOGIN_RESPONSE:
                    case LOGIN_OUTCOME:
                        LoginOutcome reply = (LoginOutcome) message;
                        if (reply.getOutcome().equals("Failure")) {
                            System.out.println("[" + reply.getAuthor() + "] " + reply.getMessage());
                            System.out.println("Enter your username:");
                            String username = sc.nextLine().trim();
                            System.out.println("Enter the password");
                            String password = sc.nextLine().trim();
                            LoginResponse login = new LoginResponse(username, password);
                            write(login);
                        } else if (reply.getOutcome().equals("Success")) {
                            System.out.println("[" + reply.getAuthor() + "] " + reply.getMessage());
                        }
                        break;
                    case LOGIN_REQUEST:
                        System.out.println("Enter your username:");
                        String username = sc.nextLine().trim();
                        System.out.println("Enter the password");
                        String password = sc.nextLine().trim();
                        LoginResponse login = new LoginResponse(username, password);
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
                System.out.println("Connection established!");
            } catch (IOException e) {
                if (firstTime) {
                    System.out.println("Cannot connect to the server, keep trying");
                    firstTime = false;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException i) {
                    System.out.println("InterruptedException occurred!");
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
