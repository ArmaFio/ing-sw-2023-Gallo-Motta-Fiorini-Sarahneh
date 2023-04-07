package it.polimi.ingsw.client;

import it.polimi.ingsw.Response;
import it.polimi.ingsw.model.utils.LoadSave;
import it.polimi.ingsw.response.StringRequest;
import it.polimi.ingsw.server.MainServer;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class NetworkHandler extends Thread {
    private Socket socket;
    private boolean connected = false;
    private boolean firstTime = true;
    private boolean closeClient = false;
    private Scanner sc = new Scanner(System.in);
    private Response response;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private BufferedReader reader;
    private PrintWriter writer;

    @Override
    public void run() {
        System.out.println("Welcome to MyShelfie!\nPlease wait while we connect you to the server!");
        //try until connection succeeds.
        while (!connected) {
            try  {
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
                }catch (InterruptedException i) {
                    System.out.println("InterruptedException occurred!");
                }
            }
        }
        //start listening for server instructions
        while (!closeClient) {
            try {
                response = read(objectInputStream);
                switch (response.type) { //TODO alcuni case probabilmente non servono.
                    case NONE:
                    case JOIN:
                    case TILES:
                    case CURSOR:
                    case START:
                    case STRING:
                        //TODO this will be probably  used for the chat
                        StringRequest line = (StringRequest) response;
                        System.out.println(line.user() + " " + line.message());
                    case LOGIN_REQUEST:
                        System.out.println("Enter your username:");
                        writer.println(sc.nextLine().trim());
                        String reply = reader.readLine();
                        //TODO does not verify client disconnections and reconnections.
                        while (!reply.equals("Success")) {
                            if (!reply.equals("Failure")) {
                                System.out.println("Invalid message received from server, ignoring it.");
                            } else {
                                System.out.println("[Server] Username already taken, please enter a different username.");
                                writer.println(sc.nextLine().trim());
                                reply = reader.readLine();
                            }
                        }
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Error: Class not found!");
            } catch (IOException e) {
                System.out.println("Cannot read the message from the server.");
                return;
            }
        }
    }

    public Response read(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        return (Response) objectInputStream.readObject();
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        NetworkHandler nh = new NetworkHandler();
        nh.start();
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
