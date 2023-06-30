package it.polimi.ingsw;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_MainServerInterface extends Remote {
    int connect() throws RemoteException;
}
