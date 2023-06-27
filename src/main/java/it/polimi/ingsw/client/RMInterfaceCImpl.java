package it.polimi.ingsw.client;

import it.polimi.ingsw.RMInterface;
import it.polimi.ingsw.messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMInterfaceCImpl extends UnicastRemoteObject implements RMInterface {

    private final RMI_NetworkHandler client;
    private Message m;

    public RMInterfaceCImpl(RMI_NetworkHandler client) throws RemoteException {
        this.client = client;
    }

    @Override
    public void selfSend(RMInterface i) {
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
