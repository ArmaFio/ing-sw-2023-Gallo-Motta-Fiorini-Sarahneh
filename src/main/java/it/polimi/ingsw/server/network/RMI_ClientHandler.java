package it.polimi.ingsw.server.network;

import it.polimi.ingsw.RMI_InterfaceConnection;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class RMI_ClientHandler extends ClientHandler {
    private final RMInterfaceSImpl rmi;
    private RMI_InterfaceConnection client;
    private Timestamp ping;

    public RMI_ClientHandler(SocketMainServer server, int id) throws IOException, AlreadyBoundException {
        super(server, id);
        rmi = new RMInterfaceSImpl(this);
        Logger.info("The thread " + id + " is now connected");
        Registry registry = LocateRegistry.getRegistry(1099);
        registry.bind(id + "RMI_InterfaceConnection", rmi);
        Logger.info("RMI Ready");
    }

    @Override
    public String getAddress() {
        return null;
    }

    /**
     * sends a message to the client
     * @param m
     * @throws IOException if the client has disconnected
     */
    @Override
    public void send(Message m) throws IOException {
        if (isConnected()) {
            m.setAuthor(this.username);
            try {
                client.write(m);
            } catch (RemoteException e) {
                disconnect();
            }
        }
    }

    /**
     * sets the Client RMI interface and starts the connection
     * @param client the client RMI interface
     */
    public synchronized void setClient(RMI_InterfaceConnection client) {
        this.client = client;
        connect();
        ping = Timestamp.valueOf(LocalDateTime.now());
        connChecker();
        this.start();
    }

    /**
     * notfies the server that a message has arrived
     */
    public synchronized void update() {
        notifyAll();
    }


    /**
     * periodically verifies that the client is still connected
     */
    public void connChecker() {
        new Thread(() -> {
            Timestamp lastPing = ping;
            int count = 0;
            while (isConnected()) {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (Objects.equals(ping, lastPing)) {
                    if (count < 3)
                        count++;
                    else {
                        Logger.error("An error occurred on thread " + id + " while waiting for connection or with write method.");
                        disconnect();
                        Logger.debug(username + " disconnected");
                    }
                } else {
                    Logger.info("Ping received from " + username);
                    lastPing = ping;
                    count = 0;
                }

            }
        }).start();

    }

    /**
     * updates the last connection signal from the client
     */
    void ping() {
        ping = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * reads a message sent from the client
     * @return the read message
     */
    @Override
    Message read() {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return rmi.getMessage();
    }
}
