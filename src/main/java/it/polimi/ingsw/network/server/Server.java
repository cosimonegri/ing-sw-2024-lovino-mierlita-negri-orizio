package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.exceptions.UsernameNotPlayingException;
import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.message.clienttoserver.PingResponse;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.GameControllerMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.ConnectMessage;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.MainControllerMessage;
import it.polimi.ingsw.utilities.Config;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Server implements ServerInterface {
    private static Server instance;
    private final MainController controller;
    private final Queue<ClientToServerMessage> messages;

    private Server() {
        this.controller = MainController.getInstance();
        this.messages = new LinkedList<>();

        Thread messagesThread = new Thread(() -> {
            while (true) {
                ClientToServerMessage message = pollMessage();
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

                if (message instanceof PingResponse m) {
                    // todo use execute?
                    new Thread(() -> {
                        this.controller.pingResponse(m.getUsername());
                    }).start();

                }
                else if (message instanceof MainControllerMessage m) {
                    new Thread(() -> {
                        m.execute(this.controller);
                    }).start();
                }
                else if (message instanceof GameControllerMessage m) {
                    try {
                        GameController game = this.controller.getGameOfPlayer(m.getUsername());
                        m.execute(game);
                    } catch (UsernameNotPlayingException ignored) {
                        //TODO ignore or catch exception?
                    }
                }
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
    public void messageFromClient(ClientToServerMessage message) throws RemoteException {
        synchronized (messages) {
            if (message != null) {
                messages.add(message);
            }
        }
    }

    private ClientToServerMessage pollMessage() {
        synchronized (messages) {
            return messages.poll();
        }
    }
}
