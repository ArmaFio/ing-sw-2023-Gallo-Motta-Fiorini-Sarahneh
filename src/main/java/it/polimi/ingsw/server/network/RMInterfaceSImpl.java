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

    /**
     * sends the Client RMI interface to the server
     * @param r the Client RMI Interface
     */
    @Override
    public void selfSend(RMI_InterfaceConnection r) {
        server.setClient(r);
    }


    /**
     * sets the message sent by the client and notifies the server that it's arrived
     * @param m the message to be sent
     */
    @Override
    public synchronized void write(Message m) {
        this.m = m;
        server.update();
    }

    public Message getMessage() {
        return m;
    }

    /**
     * signals to the server that the client is still connected
     */
    @Override
    public void ping() {
        server.ping();
    }
}
