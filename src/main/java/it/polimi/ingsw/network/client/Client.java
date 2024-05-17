package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.message.GameEndedMessage;
import it.polimi.ingsw.network.message.clienttoserver.PingResponse;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.servertoclient.DisconnectMessage;
import it.polimi.ingsw.network.message.servertoclient.PingRequest;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.network.server.ServerInterface;
import it.polimi.ingsw.network.server.SocketServerStub;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.view.View;

import java.util.Timer;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.TimerTask;

public class Client implements ClientInterface {
    private final View view;
    private final ConnectionType connection;
    private final String ip;
    private ClientInterface skeleton = null;
    private ServerInterface server = null;
    private Timer timer;

    public Client(View view, ConnectionType connection, String ip) {
        this.view = view;
        this.connection = connection;
        this.ip = ip;
        try {
            if (connection == ConnectionType.RMI) {
                this.skeleton = (ClientInterface) UnicastRemoteObject.exportObject(this, 0);
            }
        } catch (RemoteException e) {
            System.err.println("Cannot export the client as a remote object");
            System.exit(1);
        }
    }

    public void run() {
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

        this.view.addListener((message) -> {
            try {
                if (message instanceof UsernameMessage m) {
                    this.server.connectClient(m, this.skeleton);
                    // this.server.messageFromClient(new PingResponse(m.getUsername()));
                } else {
                    this.server.messageFromClient(message);
                }
            } catch (RemoteException e) {
                System.err.println("Cannot send messages to the RMI server");
                System.exit(1);
            }
        });
        this.view.run();
    }

    private ServerInterface setupRmiConnection() throws RemoteException, NotBoundException {
        System.out.println("Connecting to the RMI server...");
        Registry registry = LocateRegistry.getRegistry(this.ip, Config.RMI_PORT);
        ServerInterface stub = (ServerInterface) registry.lookup(Config.RMI_NAME);
        System.out.println("Connection established successfully");
        return stub;
    }

    private ServerInterface setupSocketConnection() throws IOException {
        System.out.println("Connecting to the socket server...");
        SocketServerStub stub = new SocketServerStub(this.ip);
        System.out.println("Connection established successfully");
        new Thread(() -> {
            try {
                while (true) {
                    ServerToClientMessage message = stub.receiveMessage();
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
    public void messageFromServer(ServerToClientMessage message) throws RemoteException {
        if (message instanceof PingRequest m) {
            if (this.timer != null) {
                this.timer.cancel();
            }
            this.server.messageFromClient(new PingResponse(m.getUsername()));
            this.timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.err.println("Cannot communicate with the server. Try to start another game.");
                    System.exit(1);
                }
            }, 2 * Config.PING_TIME_MS);
        }
        //todo move these 2 messages in the correct place in the view
        else if (message instanceof GameEndedMessage m) {
            System.out.println(m);
        }
        else if (message instanceof DisconnectMessage m) {
            System.out.println(m);
        }
        view.addMessage(message);
    }
}
