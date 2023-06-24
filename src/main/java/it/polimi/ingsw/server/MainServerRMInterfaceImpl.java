package it.polimi.ingsw.server;

import it.polimi.ingsw.MainServerRMInterface;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MainServerRMInterfaceImpl extends UnicastRemoteObject implements MainServerRMInterface {
    MainServer m;

    protected MainServerRMInterfaceImpl(MainServer m) throws RemoteException {
        this.m = m;
    }

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
