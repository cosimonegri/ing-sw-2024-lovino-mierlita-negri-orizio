package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.utilities.Config;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * Class representing a stub of the server. It is used on the client-side when using socket.
 */
public class SocketServerStub implements ServerInterface {
    private final Socket clientSocket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    /**
     * Constructor of the class
     *
     * @param ip ip of the server
     * @throws IOException if there is an error during the initialization of the socket stream
     */
    public SocketServerStub(String ip) throws IOException {
        this.clientSocket = new Socket(ip, Config.SOCKET_PORT);
        this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.input = new ObjectInputStream(this.clientSocket.getInputStream());
    }

    /**
     * Read a message from the socket stream.
     *
     * @return the message read
     * @throws RemoteException if there is an error while reading the message
     */
    public ServerToClientMessage receiveMessage() throws RemoteException {
        ServerToClientMessage message;
        try {
            message = (ServerToClientMessage) this.input.readObject();
            return message;
        } catch (ClassNotFoundException | IOException e) {
            throw new RemoteException("Cannot receive message from the socket server");
        }
    }

    /**
     * Write a {@link it.polimi.ingsw.network.message.clienttoserver.UsernameMessage}
     * in the socket stream in order to register a client and its username in the server.
     * The client must call this method once at the beginning of the communication.
     *
     * @param message the message containing the username of the client
     * @param client the client that is sending the message
     * @throws RemoteException if there is an error during the communication
     */
    @Override
    public void connectClient(UsernameMessage message, ClientInterface client) throws RemoteException {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            throw new RemoteException("Cannot send message to the socket server ");
        }
    }

    /**
     * Write a message in the socket stream.
     *
     * @param message the message to update the server
     * @throws RemoteException if there is an error during the communication
     */
    @Override
    public void messageFromClient(ClientToServerMessage message) throws RemoteException {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            throw new RemoteException("Cannot send message to the socket server ");
        }
    }
}
