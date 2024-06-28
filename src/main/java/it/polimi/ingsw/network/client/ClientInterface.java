package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing a client in a communication with any network protocol.
 */
public interface ClientInterface extends Remote {
    /**
     * @param message the message to update the client
     * @throws RemoteException if there is an error during the communication
     */
    void messageFromServer(ServerToClientMessage message) throws RemoteException;
}
