package it.polimi.ingsw.server.network;

import it.polimi.ingsw.RMI_MainServerInterface;
import it.polimi.ingsw.server.User;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MainServerRMInterfaceImpl extends UnicastRemoteObject implements RMI_MainServerInterface {
    SocketMainServer m;

    protected MainServerRMInterfaceImpl(SocketMainServer m) throws RemoteException {
        this.m = m;
    }

    /**
     * connects the client to the main server and creates his personal {@code Client Handler}
     * @return the thread count which identifies the personal {@code RMInterfaceSimpl} of the client
     */
    @Override
    public int connect() {
        try {
            int i = m.getThreadCount();
            ClientHandler client = new RMI_ClientHandler(m, i);
            m.users.add(new User(String.valueOf(i), "None", client));
            return i;
        } catch (IOException | AlreadyBoundException e) {
            Logger.warning("Accept failure." + e);
            return -1;
        }
    }
}
