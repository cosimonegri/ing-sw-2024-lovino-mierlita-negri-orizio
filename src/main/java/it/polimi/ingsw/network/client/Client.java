package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.message.clienttoserver.PingResponse;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.servertoclient.PingRequest;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.network.server.ServerInterface;
import it.polimi.ingsw.network.server.SocketServerStub;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.view.View;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.TimerTask;

/**
 * Class that represents a client.
 * <br/>
 * <br/>
 * When RMI is used, this class is exported as a remote object in the constructor
 * and the server communicates directly with it.
 * <br/>
 * When socket is used, the server communicates with {@link it.polimi.ingsw.network.client.SocketClientSkeleton}.
 */
public class Client implements ClientInterface {
    /**
     * Reference ot the view.
     */
    private final View view;
    /**
     * The network protocol that is being used.
     */
    private final ConnectionType connection;
    /**
     * The ip of the server.
     */
    private final String serverIp;
    /**
     * Reference to this class exported as a remote object.
     * Only used by the server when the network protocol choosen is RMI.
     */
    private ClientInterface skeleton = null;
    /**
     * Reference ot the server
     */
    private ServerInterface server = null;
    /**
     * Timer used to stop the application if the client doesn't receive a ping form the server.
     */
    private Timer timer;

    /**
     * Constructor of the class
     *
     * @param view reference to the view
     * @param connection the network protocol that is being used
     * @param serverIp the ip of the server
     */
    public Client(View view, ConnectionType connection, String serverIp) {
        this.view = view;
        this.connection = connection;
        this.serverIp = serverIp;
        try {
            if (connection == ConnectionType.RMI) {
                this.skeleton = (ClientInterface) UnicastRemoteObject.exportObject(this, 0);
            }
        } catch (RemoteException e) {
            System.err.println("Cannot export the client as a remote object");
            System.exit(1);
        }
    }

    /**
     * Connect to the server using the choosen network protocol, and keep a reference to it.
     * Then add the server as a listener in the view and run the view.
     */
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

    /**
     * Set up an RMI connection with the server.
     *
     * @return a reference to the server
     * @throws RemoteException if the remote communication with the registry fails
     * @throws NotBoundException if in the registry there isn't a remote object bound to the specified name
     */
    private ServerInterface setupRmiConnection() throws RemoteException, NotBoundException {
        try {
            System.setProperty("java.rmi.server.hostname", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.err.println("Cannot resolve IP address of this machine");
            System.exit(1);
        }
        System.out.println("Connecting to the RMI server...");
        Registry registry = LocateRegistry.getRegistry(this.serverIp, Config.RMI_PORT);
        ServerInterface stub = (ServerInterface) registry.lookup(Config.RMI_NAME);
        System.out.println("Connection established successfully");
        return stub;
    }

    /**
     * Set up a socket connection with the server.
     *
     * @return a reference to the server
     * @throws IOException if the connection cannot be established
     */
    private ServerInterface setupSocketConnection() throws IOException {
        System.out.println("Connecting to the socket server...");
        SocketServerStub stub = new SocketServerStub(this.serverIp);
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

    /**
     * @param message the message to update the client
     * @throws RemoteException if there is an error during the communication
     */
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
        else {
            view.addMessage(message);
        }
    }
}
