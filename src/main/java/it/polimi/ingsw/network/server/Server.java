package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.exceptions.UsernameNotPlayingException;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.message.clienttoserver.PingResponse;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.GameControllerMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.ConnectMessage;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.MainControllerMessage;
import it.polimi.ingsw.network.message.servertoclient.LobbyMessage;
import it.polimi.ingsw.network.message.servertoclient.PingRequest;
import it.polimi.ingsw.network.message.servertoclient.ViewUpdateMessage;
import it.polimi.ingsw.utilities.Config;

import java.rmi.RemoteException;
import java.util.*;

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
                ClientToServerMessage message = waitForMessage();
                System.out.println("Received message: " + message.getClass().getSimpleName() + " from " + message.getUsername());

                switch (message) {
                    case PingResponse m -> new Thread(() -> respondToPing(m.getUsername())).start();
                    case MainControllerMessage m -> m.execute(this.controller);
                    case GameControllerMessage m -> {
                        try {
                            GameController game = this.controller.getGameOfPlayer(m.getUsername());
                            m.execute(game);
                        } catch (UsernameNotPlayingException ignored) {}
                    }
                    default -> System.err.println("Message not valid");
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
                messages.notifyAll();
            }
        }
    }

    @Override
    public void messageFromClient(ClientToServerMessage message) throws RemoteException {
        synchronized (messages) {
            if (message != null) {
                messages.add(message);
                messages.notifyAll();
            }
        }
    }

    private ClientToServerMessage waitForMessage() {
        synchronized (messages) {
            while (this.messages.isEmpty()) {
                try {
                    messages.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for a message");
                    System.exit(1);
                }
            }
            return this.messages.poll();
        }
    }

    private void respondToPing(String username) {
        synchronized (usernameToTimer) {
            try {
                if (usernameToTimer.containsKey(username)) {
                    // cancel timer
                    this.usernameToTimer.get(username).cancel();
                    // wait some time before sending another ping request
                    usernameToTimer.wait(Config.PING_TIME_MS);
                    sendPing(username);
                }
            } catch (InterruptedException e) {
                System.err.println("Cannot find timer of user " + username);
            }
        }
    }

    public void sendPing(String username) {
        synchronized (usernameToTimer) {
            Timer timer = new Timer();
            controller.notifyListener(username, new PingRequest(username));

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (usernameToTimer) {
                        // if the player doesn't respond leave the game
                        try {
                            usernameToTimer.remove(username);
                            GameController game = controller.leaveGame(username);
                            if (game.getPhase() == GamePhase.WAITING) {
                                game.notifyAllListeners(new LobbyMessage(
                                        game.getPlayersCount(),
                                        game.getPlayers().stream().map(Player::getUsername).toList(),
                                        username + " has left."
                                ));
                            } else {
                                game.notifyAllListeners(new ViewUpdateMessage(
                                        game.getModelView(), username + " has left. The game has ended."
                                ));
                            }
                        } catch (UsernameNotPlayingException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }
            }, Config.PING_TIME_MS);

            usernameToTimer.put(username, timer);
        }
    }
}
