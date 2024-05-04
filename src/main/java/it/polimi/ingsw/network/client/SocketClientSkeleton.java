package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.SocketMiddleware;
import it.polimi.ingsw.network.message.ConnectMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.UsernameMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClientSkeleton implements SocketMiddleware, ClientInterface, AutoCloseable {
    private final Socket clientSocket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public SocketClientSkeleton(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.output = new ObjectOutputStream(clientSocket.getOutputStream());
        this.input = new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
    public Message receiveMessage() throws RemoteException {
        try {
            Message message = (Message) this.input.readObject();
            if (message instanceof UsernameMessage m) {
                return new ConnectMessage(m.getUsername(), this);
            }
            return message;
        } catch (ClassNotFoundException | IOException e) {
            throw new RemoteException("Cannot receive message from the client");
        }
    }

    @Override
    public void messageFromServer(Message message) throws RemoteException {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            throw new RemoteException("Cannot send message to the client");
        }
    }

    @Override
    public void close() throws IOException {
        this.clientSocket.close();
        System.out.println("Socket connection closed");
    }
}
