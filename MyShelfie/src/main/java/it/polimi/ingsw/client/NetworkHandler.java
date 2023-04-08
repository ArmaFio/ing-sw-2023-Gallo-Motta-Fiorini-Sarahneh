package it.polimi.ingsw.client;

import it.polimi.ingsw.Response;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.response.LoginOutcome;
import it.polimi.ingsw.response.LoginResponse;
import it.polimi.ingsw.response.StringRequest;
import it.polimi.ingsw.response.TilesRequest;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkHandler extends Thread {
    private boolean connected = false;
    private boolean firstTime = true;
    private boolean closeClient = false;
    private Scanner sc = new Scanner(System.in);
    private Response response;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
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
                response = read(objectInputStream);
                switch (response.type) { //TODO alcuni case probabilmente non servono.
                    case NONE:
                    case JOIN:
                    case TILES:
                        TilesRequest res = (TilesRequest) response;
                        ArrayList<Tile> sel = view.v_pick_tiles(res.getAvailable());
                        int i = view.v_put_tiles(sel);
                        res.setChosen(sel);
                        res.setCol(i);
                        write(objectOutputStream, res);
                        break;
                    case CURSOR:
                    case START:
                        view = new ClientView();
                        break;
                    case STRING:
                        //TODO this will be probably  used for the chat
                        StringRequest line = (StringRequest) response;
                        System.out.println(line.user() + " " + line.message());
                    case LOGIN_RESPONSE:
                    case LOGIN_OUTCOME:
                        LoginOutcome reply = (LoginOutcome) response;
                        if (reply.getOutcome().equals("Failure")) {
                            System.out.println("[" + reply.getAuthor() + "] " + reply.getMessage());
                            System.out.println("Enter your username:");
                            String username = sc.nextLine().trim();
                            System.out.println("Enter the password");
                            String password = sc.nextLine().trim();
                            LoginResponse login = new LoginResponse(username, password);
                            write(objectOutputStream, login);
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
                        write(objectOutputStream, login);
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
                objectOutputStream = new ObjectOutputStream(output);
                writer = new PrintWriter(output, true);
                objectInputStream = new ObjectInputStream(input);
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
     * @param objectInputStream the InputStream for serialized objects.
     * @return the object read.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Response read(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        return (Response) objectInputStream.readObject();
    }

    /**
     * Writes a serialized object and sends it to the client.
     *
     * @param objectOutputStream the OutputStream for the serialized object.
     * @param obj                the object we want to send to the client.
     * @throws IOException
     */
    public void write(ObjectOutputStream objectOutputStream, Response obj) throws IOException {
        objectOutputStream.writeObject(obj);
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
