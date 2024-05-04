package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.UsernameMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void connectClient(UsernameMessage message, ClientInterface client) throws RemoteException;
    void messageFromClient(Message message) throws RemoteException;
}
