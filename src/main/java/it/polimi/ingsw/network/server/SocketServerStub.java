package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.SocketMiddleware;
import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.ConnectMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.UsernameMessage;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.utilities.Printer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketServerStub implements SocketMiddleware, ServerInterface {
    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public SocketServerStub() {
        try {
            this.clientSocket = new Socket(Config.HOSTNAME, Config.SOCKET_PORT);
        } catch (IOException e) {
            Printer.printError("Cannot establish a connection with the socket server: " + e.getMessage());
        }
        try {
            this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
        } catch (IOException e) {
            Printer.printError("Cannot connect to the socket output stream: " + e.getMessage());
        }
        try {
            this.input = new ObjectInputStream(this.clientSocket.getInputStream());
        } catch (IOException e) {
            Printer.printError("Cannot connect to the socket input stream");
        }
    }

    //TODO sistemare casting e tutti lanciano eccezioni
    @Override
    public Message receiveMessage() {
        Message message = null;
        try {
            message = (Message) this.input.readObject();
        } catch (ClassNotFoundException | IOException e) {
            Printer.printError("Cannot receive the message from the socket server");
        }
        return message;
    }

    @Override
    public void connectClient(UsernameMessage message, ClientInterface client) {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            Printer.printError("Cannot send the message to the socket server ");
        }
    }

    @Override
    public void messageFromClient(Message message) {
        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            Printer.printError("Cannot send the message to the socket server ");
        }
    }
}
