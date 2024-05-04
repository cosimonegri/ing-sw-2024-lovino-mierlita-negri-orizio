package it.polimi.ingsw.network;

import it.polimi.ingsw.network.message.Message;

import java.rmi.RemoteException;

public interface SocketMiddleware {
    Message receiveMessage() throws RemoteException;
}
