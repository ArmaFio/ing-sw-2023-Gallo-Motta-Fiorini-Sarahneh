package it.polimi.ingsw.server;

import it.polimi.ingsw.RMInterface;
import it.polimi.ingsw.messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

public class RMInterfaceSImpl extends UnicastRemoteObject implements RMInterface {
    private final RMI_ClientHandler server;
    private Message m;

    protected RMInterfaceSImpl(RMI_ClientHandler s) throws RemoteException {
        server = s;
    }

    @Override
    public void selfSend(RMInterface r) throws ServerNotActiveException, InterruptedException {
        server.setClient(r);
        server.setAddress(getClientHost());
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
