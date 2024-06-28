package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing the server in a communication with any network protocol.
 */
public interface ServerInterface extends Remote {
    /**
     * Register a client and its username in the server.
     * The client must call this method once at the beginning of the communication.
     *
     * @param message the message containing the username of the client
     * @param client the client that is sending the message
     * @throws RemoteException if there is an error during the communication
     */
    void connectClient(UsernameMessage message, ClientInterface client) throws RemoteException;
    /**
     * @param message the message to update the server
     * @throws RemoteException if there is an error during the communication
     */
    void messageFromClient(ClientToServerMessage message) throws RemoteException;
}
