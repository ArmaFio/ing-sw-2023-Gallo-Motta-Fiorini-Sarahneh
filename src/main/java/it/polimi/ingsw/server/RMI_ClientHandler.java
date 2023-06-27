package it.polimi.ingsw.server;

import it.polimi.ingsw.RMInterface;
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
    private RMInterface client;
    private Timestamp ping;

    public RMI_ClientHandler(MainServer server, int id) throws IOException, AlreadyBoundException {
        super(server, id);
        rmi = new RMInterfaceSImpl(this);
        Logger.info("The thread " + id + " is now connected");
        Registry registry = LocateRegistry.getRegistry(1099);
        registry.bind(id + "RMInterface", rmi);
        Logger.info("RMI Ready");
    }

    @Override
    String getAddress() {
        return null;
    }

    @Override
    public void send(Message m) throws IOException {
        if (isConnected()) {
            m.setAuthor(this.username);
            try {
                client.write(m);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void setClient(RMInterface client) {
        this.client = client;
        connect();
        ping = Timestamp.valueOf(LocalDateTime.now());
        connChecker();
        this.start();
    }


    public synchronized void update() {
        notifyAll();
    }


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

    void ping() {
        ping = Timestamp.valueOf(LocalDateTime.now());
    }

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
