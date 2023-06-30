package it.polimi.ingsw.client;

import it.polimi.ingsw.RMI_InterfaceConnection;
import it.polimi.ingsw.messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientConnection extends UnicastRemoteObject implements RMI_InterfaceConnection {

    private final RMI_NetworkHandler client;
    private Message m;

    public RMIClientConnection(RMI_NetworkHandler client) throws RemoteException {
        this.client = client;
    }

    @Override
    public void selfSend(RMI_InterfaceConnection i) {
    }

    @Override
    public synchronized void write(Message m) {
        client.update();
        this.m = m;
    }

    @Override
    public Message getMessage() throws RemoteException {
        return m;
    }

    @Override
    public void ping() {
    }


}
