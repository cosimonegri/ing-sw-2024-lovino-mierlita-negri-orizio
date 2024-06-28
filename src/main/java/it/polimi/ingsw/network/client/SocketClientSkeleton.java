package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.ConnectMessage;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * Class representing a skeleton of the client. It is used on the server-side when using socket.
 */
public class SocketClientSkeleton implements ClientInterface, AutoCloseable {
    private final Socket clientSocket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    /**
     * Constructor of the class
     *
     * @param clientSocket the socket of the client
     * @throws IOException if there is an error during the initialization of the socket stream
     */
    public SocketClientSkeleton(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.output = new ObjectOutputStream(clientSocket.getOutputStream());
        this.input = new ObjectInputStream(clientSocket.getInputStream());
    }

    /**
     * Read a message from the socket stream, and enrich it with a reference to this object
     * in case it is a {@link UsernameMessage}
     *
     * @return the message read
     * @throws RemoteException if there is an error while reading the message
     */
    public ClientToServerMessage receiveMessage() throws RemoteException {
        try {
            ClientToServerMessage message = (ClientToServerMessage) this.input.readObject();
            if (message instanceof UsernameMessage m) {
                return new ConnectMessage(m.getUsername(), this);
            }
            return message;
        } catch (ClassNotFoundException | IOException e) {
            throw new RemoteException();
        }
    }

    /**
     * Write a message in the socket stream.
     *
     * @param message the message to update the client
     * @throws RemoteException if there is an error during the communication
     */
    @Override
    public void messageFromServer(ServerToClientMessage message) throws RemoteException {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            throw new RemoteException();
        }
    }

    /**
     * Close the socket
     *
     * @throws IOException if an error occurs when closing the socket
     */
    @Override
    public void close() throws IOException {
        this.clientSocket.close();
        System.out.println("Socket connection closed");
    }
}
