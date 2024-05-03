package it.polimi.ingsw.network;

import it.polimi.ingsw.network.message.Message;

import java.rmi.RemoteException;

public interface SocketMiddleware {
    public Message receiveMessage() throws RemoteException;
}
