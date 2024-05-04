package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.message.ConnectMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.UsernameMessage;
import it.polimi.ingsw.network.server.ServerInterface;
import it.polimi.ingsw.network.server.SocketServerStub;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.utilities.Printer;
import it.polimi.ingsw.view.View;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;

public class Client implements ClientInterface {
    private final ConnectionType connection;
    private final ClientInterface skeleton;
    private ServerInterface server;
    private final Queue<Message> messages;

    public Client(ConnectionType connection) throws RemoteException {
        this.connection = connection;
        this.messages = new LinkedList<>();
        this.skeleton = connection == ConnectionType.RMI
                ?(ClientInterface) UnicastRemoteObject.exportObject(this, 0)
                : null;
    }

    public void connectToServer(View view) {
        if (connection == ConnectionType.RMI) {
            try {
                this.server = setupRmiConnection();
            } catch (RemoteException | NotBoundException e) {
                Printer.printError("Cannot connect to the RMI Server", e);
                System.exit(1);
            }
        }
        else {
            try {
                this.server = setupSocketConnection();
            } catch (RemoteException e) {
                Printer.printError("Cannot connect to the RMI Server", e);
                System.exit(1);
            }
        }

        view.addListener((message) -> {
            try {
                if (message instanceof UsernameMessage m) {
                    server.connectClient(m, this.skeleton);
                } else {
                    server.messageFromClient(message);
                }
            } catch (RemoteException e) {
                Printer.printError("Remote exception", e);
            }
        });
    }

    private ServerInterface setupRmiConnection() throws RemoteException, NotBoundException {
        System.out.println("Connecting to the RMI server...");
        Registry registry = LocateRegistry.getRegistry(Config.HOSTNAME, Config.RMI_PORT);
        ServerInterface stub = (ServerInterface) registry.lookup(Config.RMI_NAME);
        System.out.println("Connection established successfully...");
        return stub;
    }

    //TODO sistemare eccezioni
    private ServerInterface setupSocketConnection() throws RemoteException {
        System.out.println("Connecting to the Socket server...");
        SocketServerStub stub = new SocketServerStub();
        System.out.println("Connection established successfully...");
        new Thread(() -> {
            try {
                while (true) {
                    Message message = stub.receiveMessage();
                    this.messageFromServer(message);
                }
            } catch (RemoteException e) {
                Printer.printError("Cannot receive data from socket server");
            }
        }).start();
        return stub;
    }

    @Override
    public void messageFromServer(Message message) throws RemoteException {
        synchronized (messages) {
            this.messages.add(message);
            messages.notifyAll();
        }
    }

    public Message waitForMessage() {
        synchronized (messages) {
            while (this.messages.isEmpty()) {
                try {
                    messages.wait();
                } catch (InterruptedException e) {
                   Printer.printError("Interrupted while waiting for server response");
                   System.exit(1);
                }
            }
            return this.messages.poll();
        }
    }
}
