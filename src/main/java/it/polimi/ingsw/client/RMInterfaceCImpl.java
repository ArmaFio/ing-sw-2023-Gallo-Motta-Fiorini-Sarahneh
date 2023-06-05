package it.polimi.ingsw.client;

import it.polimi.ingsw.RMInterface;
import it.polimi.ingsw.messages.Message;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

public class RMInterfaceCImpl extends UnicastRemoteObject implements RMInterface{
    private Message m;
    private final RMI_NetworkHandler client;

    public RMInterfaceCImpl(RMI_NetworkHandler client) throws RemoteException {
        this.client=client;}
    @Override
    public void selfSend(RMInterface i) throws ServerNotActiveException {}

    @Override
    public void write(Message m) {
        this.m=m;
        client.update();
    }

    @Override
    public Message getM() {
        return m;
    }
}
