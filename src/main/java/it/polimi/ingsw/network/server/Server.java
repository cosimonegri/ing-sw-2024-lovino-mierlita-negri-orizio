package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.ConnectMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.UsernameMessage;
import it.polimi.ingsw.utilities.Config;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Server implements ServerInterface {
    private static Server instance;
    private final MainController controller;
    private final Queue<Message> messages;

    private Server() {
        this.controller = MainController.getInstance();
        this.messages = new LinkedList<>();

        Thread messagesThread = new Thread(() -> {
            while (true) {
                Message message = pollMessage();
                if (message == null) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(Config.SLEEP_TIME_MS);
                    } catch (InterruptedException e) {
                        System.err.println("Messages thread interrupted while sleeping");
                        System.exit(1);
                    }
                    continue;
                }
                System.out.println("Received message: " + message.getClass());
                message.execute();
            }
        });
        messagesThread.start();
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    @Override
    public void connectClient(UsernameMessage message, ClientInterface client) throws RemoteException {
        synchronized (messages) {
            if (message != null) {
                messages.add(new ConnectMessage(message.getUsername(), client));
            }
        }
    }

    @Override
    public void messageFromClient(Message message) throws RemoteException {
        synchronized (messages) {
            if (message != null) {
                messages.add(message);
            }
        }
    }

    private Message pollMessage() {
        synchronized (messages) {
            return messages.poll();
        }
    }
}
