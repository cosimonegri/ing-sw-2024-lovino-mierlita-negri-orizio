package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.UsernameMessage;
import it.polimi.ingsw.network.server.ServerInterface;
import it.polimi.ingsw.network.server.SocketServerStub;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;

public class Client implements ClientInterface {
    private final ConnectionType connection;
    private final Queue<Message> messages;
    private ClientInterface skeleton = null;
    private ServerInterface server = null;

    public Client(ConnectionType connection) {
        this.connection = connection;
        this.messages = new LinkedList<>();
        try {
            if (connection == ConnectionType.RMI) {
                this.skeleton = (ClientInterface) UnicastRemoteObject.exportObject(this, 0);
            }
        } catch (RemoteException e) {
            System.err.println("Cannot export the client as a remote object");
            System.exit(1);
        }
    }

    public void connectToServer(View view) {
        if (this.server != null) {
            return;
        }
        if (connection == ConnectionType.RMI) {
            try {
                this.server = setupRmiConnection();
            } catch (RemoteException | NotBoundException e) {
                System.err.println("Cannot connect to the RMI server");
                System.exit(1);
            }
        }
        else {
            try {
                this.server = setupSocketConnection();
            } catch (IOException e) {
                System.err.println("Cannot connect to the socket server");
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
                System.err.println("Cannot send messages to the RMI server");
                System.exit(1);
            }
        });
    }

    private ServerInterface setupRmiConnection() throws RemoteException, NotBoundException {
        System.out.println("Connecting to the RMI server...");
        Registry registry = LocateRegistry.getRegistry(Config.HOSTNAME, Config.RMI_PORT);
        ServerInterface stub = (ServerInterface) registry.lookup(Config.RMI_NAME);
        System.out.println("Connection established successfully");
        return stub;
    }

    private ServerInterface setupSocketConnection() throws IOException {
        System.out.println("Connecting to the socket server...");
        SocketServerStub stub = new SocketServerStub();
        System.out.println("Connection established successfully");
        new Thread(() -> {
            try {
                while (true) {
                    Message message = stub.receiveMessage();
                    this.messageFromServer(message);
                }
            } catch (RemoteException e) {
                System.err.println("Cannot receive messages from the socket server");
                System.exit(1);
            }
        }).start();
        return stub;
    }

    @Override
    public void messageFromServer(Message message) throws RemoteException {
        synchronized (messages) {
            if (message != null) {
                this.messages.add(message);
                messages.notifyAll();
            }
        }
    }

    public Message waitForMessage() {
        synchronized (messages) {
            while (this.messages.isEmpty()) {
                try {
                    messages.wait();
                } catch (InterruptedException e) {
                   System.err.println("Interrupted while waiting for server response");
                   System.exit(1);
                }
            }
            return this.messages.poll();
        }
    }
}
