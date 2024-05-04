package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void messageFromServer(Message message) throws RemoteException;
}
