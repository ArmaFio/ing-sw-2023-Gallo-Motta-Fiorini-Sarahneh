package it.polimi.ingsw.server.network;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.polimi.ingsw.RMI_InterfaceConnection;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.Logger;


public class RMI_ClientHandler extends ClientHandler {
    private final RMInterfaceSImpl rmi;
    private RMI_InterfaceConnection client;

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
            while (isConnected()) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    client.ping();
                } catch (RemoteException e) {
                    disconnect();
                    Logger.info(username + " disconnected");
                }
            }
        }).start();
    }


    /**
     * reads a message sent from the client
     * @return the read message
     */
    @Override
    synchronized Message read() {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return rmi.getMessage();
    }
}
