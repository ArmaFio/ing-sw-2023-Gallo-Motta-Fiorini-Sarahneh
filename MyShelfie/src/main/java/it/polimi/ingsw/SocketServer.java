package it.polimi.ingsw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    private Socket socket;
    public final ResponseData data;
    private ObjectOutputStream output;
    private boolean run;
    private ObjectInputStream input;

    public SocketServer() {
        data = new ResponseData();
    }

    public void run() {
        Response res;
        run = true;

        try (ServerSocket listener = new ServerSocket(59090)) {
            try (Socket socket = listener.accept()) {

                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());

                while (run) {
                    res = (Response) input.readObject();

                    addResponse(res);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                output.close();
                input.close();
                listener.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addResponse(Response res) {

        switch (res.type) {
            case JOIN:
                data.addNewUser(res);
                break;
            case TILES, CURSOR:
                System.out.println("Non ancora implementato");
                break;
            default:
                System.out.println("Response not valid");
        }
    }

    public void write(Object obj) throws IOException {
        output.writeObject(obj);
    }


    public void close() {
        run = false;
    }
}
