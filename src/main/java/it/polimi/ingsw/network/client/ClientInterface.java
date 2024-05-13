package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void messageFromServer(ServerToClientMessage message) throws RemoteException;
}
