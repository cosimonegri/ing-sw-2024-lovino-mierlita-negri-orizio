package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void connectClient(UsernameMessage message, ClientInterface client) throws RemoteException;
    void messageFromClient(ClientToServerMessage message) throws RemoteException;
}
