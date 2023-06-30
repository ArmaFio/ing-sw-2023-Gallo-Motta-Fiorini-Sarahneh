package it.polimi.ingsw.server.network;

import it.polimi.ingsw.RMI_InterfaceConnection;
import it.polimi.ingsw.messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMInterfaceSImpl extends UnicastRemoteObject implements RMI_InterfaceConnection {
    private final RMI_ClientHandler server;
    private Message m;

    protected RMInterfaceSImpl(RMI_ClientHandler s) throws RemoteException {
        server = s;
    }

    @Override
    public void selfSend(RMI_InterfaceConnection r) {
        server.setClient(r);
    }

    @Override
    public synchronized void write(Message m) {
        this.m = m;
        server.update();
    }

    public Message getMessage() {
        return m;
    }

    @Override
    public void ping() {
        server.ping();
    }
}
