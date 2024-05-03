package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.network.message.CreateGameMessage;
import it.polimi.ingsw.network.message.JoinMessage;
import it.polimi.ingsw.network.message.Message;

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
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    continue;
                }
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
    public void messageFromClient(Message message) throws RemoteException {
        synchronized (messages) {
            messages.add(message);
        }
    }

    private Message pollMessage() {
        synchronized (messages) {
            return messages.poll();
        }
    }
}
