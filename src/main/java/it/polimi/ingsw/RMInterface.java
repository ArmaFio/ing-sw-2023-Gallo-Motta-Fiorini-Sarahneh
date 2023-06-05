package it.polimi.ingsw;

import it.polimi.ingsw.client.RMI_NetworkHandler;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StateUpdate;
import it.polimi.ingsw.server.model.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface RMInterface extends Remote {
    void selfSend(RMInterface r) throws ServerNotActiveException, RemoteException;

    void write(Message m) throws RemoteException;

    Message getM() throws RemoteException;
}


