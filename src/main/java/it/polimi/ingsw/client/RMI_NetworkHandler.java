package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.RMI_InterfaceConnection;
import it.polimi.ingsw.RMI_MainServerInterface;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;

public class RMI_NetworkHandler extends NetworkHandler implements Remote, Serializable {
    private final RMI_InterfaceConnection remoteClient;
    private final String serverIp;
    private String username;
    private int nThread;
    private RMI_InterfaceConnection rmi;

    public RMI_NetworkHandler(int choice, String ip) throws IOException, NotBoundException {
        super(choice);
        serverIp = ip;
        nThread = -1;
        remoteClient = new RMIClientConnection(this);
        connect();
        clientStart();
        //start listening for server instructions
    }

    /**
     * reads a message received from the server
     * @return the read message
     */
    @Override
    public synchronized Message read() {
        try {
            wait();
            return remoteClient.getMessage();
        } catch (InterruptedException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * sends a message to the server
     * @param x the message to be sent
     */
    @Override
    public synchronized void write(Message x) {
        x.setAuthor(username);
        new Thread(() -> {
            try {
                rmi.write(x);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void disconnect() {
        view.updateState(GameState.CLOSE);
        view.disconnect();
    }


    /**
     * connects the client to the {@code MainserverRMInterface} and then
     */
    public void connect() {
        boolean firstTime = true;
        Registry registry = null;
        while (registry == null) {
            try {
                registry = LocateRegistry.getRegistry(serverIp, 1099);
            } catch (RemoteException e) {
                System.out.println("Cannot Find RMI Registry, retrying...");
            }
        }
        while (nThread == -1) {
            try {
                RMI_MainServerInterface server = (RMI_MainServerInterface) registry.lookup("SocketMainServer");
                nThread = server.connect();
            } catch (RemoteException | NotBoundException e) {
                if (firstTime) {
                    System.out.println("Cannot connect to the server, keep trying...");
                    firstTime = false;
                }
            }
            if (nThread == -1) {
                if (firstTime) {
                    System.out.println("Cannot connect to the server, keep trying...");
                    firstTime = false;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException i) {
                    Logger.error("InterruptedException occurred!");
                }
            }
        }
        System.out.println("Connection with the Main Server Estabilished!");
        try {
            this.rmi = (RMI_InterfaceConnection) registry.lookup(nThread + "RMI_InterfaceConnection");
            rmi.selfSend(remoteClient);
        } catch (RemoteException | NotBoundException | ServerNotActiveException | InterruptedException e) {
            System.out.println("Cannot find personal rmi connection" + e);
        }
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
        view.setUsername(username);
    }

    public synchronized void update() {
        this.notifyAll();
    }

    /**
     * periodically notifies the server that the client is still connected
     */
    public void ping(){}

}

