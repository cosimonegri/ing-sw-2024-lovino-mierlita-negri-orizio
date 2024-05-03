package it.polimi.ingsw;

import it.polimi.ingsw.network.client.SocketClientSkeleton;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.ServerInterface;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.utilities.Printer;

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
                Printer.printError("Cannot start RMI server", e);
            }
        });
        rmiThread.start();

        Thread socketThread = new Thread(() -> {
            try {
                initSocketServer();
            } catch (IOException e) {
                Printer.printError("Cannot start Socket server", e);
            }
        });
        socketThread.start();
    }

    private static void initRmiServer() throws RemoteException {
        // System.out.println("Starting RMI server...");
        ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(
                Server.getInstance(), Config.RMI_PORT
        );
        Registry registry = LocateRegistry.createRegistry(Config.RMI_PORT);
        registry.rebind(Config.RMI_NAME, stub);
        System.out.println("RMI server started successfully");
    }

    private static void initSocketServer() throws IOException {
        try (ExecutorService executor = Executors.newCachedThreadPool();) {
            // System.out.println("Starting Socket server...");

            try (ServerSocket serverSocket = new ServerSocket(Config.SOCKET_PORT)) {
                System.out.println("Socket server started successfully");

                while (true) {
                    System.out.println("Waiting for a socket connection...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New socket connection accepted");

                    executor.submit(() -> {
                        try (SocketClientSkeleton client = new SocketClientSkeleton(clientSocket)) {
                            while (true) {
                                Message message = client.receiveMessage();
                                Server.getInstance().messageFromClient(message);
                            }
                        } catch (RemoteException e) {
                            Printer.printError("Cannot receive data from socket client");
                        } catch (IOException e) {
                            Printer.printError("Cannot close connection with socket client", e);
                        }
                    });
                }
            }
        }
    }
}
