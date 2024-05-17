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

public class SocketClientSkeleton implements ClientInterface, AutoCloseable {
    private final Socket clientSocket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public SocketClientSkeleton(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.output = new ObjectOutputStream(clientSocket.getOutputStream());
        this.input = new ObjectInputStream(clientSocket.getInputStream());
    }

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

    @Override
    public void messageFromServer(ServerToClientMessage message) throws RemoteException {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            throw new RemoteException();
        }
    }

    @Override
    public void close() throws IOException {
        this.clientSocket.close();
        System.out.println("Socket connection closed");
    }
}
