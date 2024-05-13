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

public class SocketServerStub implements ServerInterface {
    private final Socket clientSocket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    //TODO add ip as parameter
    public SocketServerStub() throws IOException {
        this.clientSocket = new Socket(Config.HOSTNAME, Config.SOCKET_PORT);
        this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.input = new ObjectInputStream(this.clientSocket.getInputStream());
    }

    public ServerToClientMessage receiveMessage() throws RemoteException {
        ServerToClientMessage message;
        try {
            message = (ServerToClientMessage) this.input.readObject();
            return message;
        } catch (ClassNotFoundException | IOException e) {
            throw new RemoteException("Cannot receive message from the socket server");
        }
    }

    @Override
    public void connectClient(UsernameMessage message, ClientInterface client) throws RemoteException {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            throw new RemoteException("Cannot send message to the socket server ");
        }
    }

    @Override
    public void messageFromClient(ClientToServerMessage message) throws RemoteException {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            throw new RemoteException("Cannot send message to the socket server ");
        }
    }
}
