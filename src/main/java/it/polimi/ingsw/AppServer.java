package it.polimi.ingsw;

import it.polimi.ingsw.network.client.SocketClientSkeleton;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.ServerInterface;
import it.polimi.ingsw.utilities.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppServer {
    public static void main(String[] args) {
        Thread rmiThread = new Thread(() -> {
            try {
                initRmiServer();
            } catch (RemoteException e) {
                System.err.println("Cannot start the RMI server");
                System.exit(1);
            }
        });
        rmiThread.start();

        Thread socketThread = new Thread(() -> {
            try {
                initSocketServer();
            } catch (IOException e) {
                System.err.println("Cannot start Socket server");
                System.exit(1);
            }
        });
        socketThread.start();
    }

    private static void initRmiServer() throws RemoteException {
        ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(
                Server.getInstance(), Config.RMI_PORT
        );
        Registry registry = LocateRegistry.createRegistry(Config.RMI_PORT);
        registry.rebind(Config.RMI_NAME, stub);
        System.out.println("RMI server started successfully");
    }

    private static void initSocketServer() throws IOException {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            try (ServerSocket serverSocket = new ServerSocket(Config.SOCKET_PORT)) {
                System.out.println("Socket server started successfully");

                //noinspection InfiniteLoopStatement
                while (true) {
                    System.out.println("Waiting for a socket connection...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New socket connection accepted");

                    executor.submit(() -> {
                        try (SocketClientSkeleton client = new SocketClientSkeleton(clientSocket)) {
                            //noinspection InfiniteLoopStatement
                            while (true) {
                                Message message = client.receiveMessage();
                                Server.getInstance().messageFromClient(message);
                            }
                        } catch (RemoteException e) {
                            System.err.println("Cannot receive data from socket client");
                        } catch (IOException e) {
                            System.err.println("Cannot close connection with socket client");
                        }
                    });
                }
            }
        }
    }
}
