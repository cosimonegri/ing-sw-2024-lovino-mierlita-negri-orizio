package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.exceptions.UsernameNotPlayingException;
import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.GameEndedMessage;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.message.clienttoserver.PingResponse;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.GameControllerMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.ConnectMessage;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.CreateGameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.JoinGameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.MainControllerMessage;
import it.polimi.ingsw.network.message.servertoclient.PingRequest;
import it.polimi.ingsw.utilities.Config;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server implements ServerInterface {
    private static Server instance;
    private final MainController controller;
    private final Queue<ClientToServerMessage> messages;

    private final Map<String, Timer> usernameToTimer;

    private Server() {
        this.controller = MainController.getInstance();
        this.messages = new LinkedList<>();
        this.usernameToTimer = new HashMap<>();

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

                switch (message) {
                    case PingResponse m ->
                            new Thread(m::execute).start();
                    case MainControllerMessage m -> new Thread(() -> {
                        m.execute(this.controller);
                        // Start ping timer
                        if (message instanceof ConnectMessage) {
                            this.pingRequest(m.getUsername());
                        }
                    }).start();
                    case GameControllerMessage m -> {
                        try {
                            GameController game = this.controller.getGameOfPlayer(m.getUsername());
                            m.execute(game);
                        } catch (UsernameNotPlayingException ignored) {
                            //TODO ignore or catch exception?
                        }
                    }
                    default -> {
                        System.err.println("Message not valid");
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

    public void pingResponse(String username) {
        synchronized (usernameToTimer.get(username)) {
            try {
                // cancel timer
                this.usernameToTimer.get(username).cancel();
                // wait some time
                usernameToTimer.get(username).wait(5000);
                // send new request
                this.pingRequest(username);
            } catch (InterruptedException  | NullPointerException ignored) {
                System.err.println("Could not find the user timer");
            }
        }
    }

    private void pingRequest(String username) {
        // send ping request
        controller.notifyListener(username, new PingRequest(username));
        // create new timer
        Timer timer = new Timer();
        // if the player doesn't respond leave the game
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    usernameToTimer.remove(username);
                    controller.getGameOfPlayer(username).notifyAllListenersExcept(username, new GameEndedMessage(username + " left the lobby"));
                    controller.leaveGame(username);

                } catch (UsernameNotPlayingException e) {
                    System.err.println("Username not playing");
                }
            }
        }, 5000);

        usernameToTimer.put(username, timer);
    }
}
