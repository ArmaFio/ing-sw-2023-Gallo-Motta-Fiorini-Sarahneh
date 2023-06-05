package it.polimi.ingsw;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MainServerRMInterface extends Remote {
    int connect() throws RemoteException;
}
