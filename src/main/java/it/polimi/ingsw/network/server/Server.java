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

/**
 * Class that represents the server.
 * <br/>
 * <br/>
 * This class is exported as a remote object in {@link it.polimi.ingsw.AppServer}.
 * <br/>
 * The clients that use RMI communicate directly with this class, and the ones that use socket communicate with
 * {@link it.polimi.ingsw.network.server.SocketServerStub}.
 */
public class Server implements ServerInterface {
    /**
     * Singletone instance of the server.
     */
    private static Server instance;
    /**
     * Reference to the main controller, used to handle all the games
     */
    private final MainController controller;
    /**
     * Queue of messages received from all clients
     */
    private final Queue<ClientToServerMessage> messages;
    /**
     * Object to map a username to its timer.
     * The timer is used to disconnect a client if the server doesn't receive a ping from it.
     */
    private final Map<String, Timer> usernameToTimer;

    /**
     * Construtor of the class.
     * Start a thread to handle the messages received from all clients.
     */
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

    /**
     * @return the singletone instance of the server
     */
    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    /**
     * Add a {@link ConnectMessage} to the messages queue of the server
     * in order to register a client and its username in the server.
     * The client must call this method once at the beginning of the communication.
     *
     * @param message the message containing the username of the client
     * @param client the client that is sending the message
     * @throws RemoteException if there is an error during the communication
     */
    @Override
    public void connectClient(UsernameMessage message, ClientInterface client) throws RemoteException {
        synchronized (messages) {
            if (message != null) {
                messages.add(new ConnectMessage(message.getUsername(), client));
                messages.notifyAll();
            }
        }
    }

    /**
     * Add a message to the messages queue of the server.
     *
     * @param message the message to update the server
     * @throws RemoteException if there is an error during the communication
     */
    @Override
    public void messageFromClient(ClientToServerMessage message) throws RemoteException {
        synchronized (messages) {
            if (message != null) {
                messages.add(message);
                messages.notifyAll();
            }
        }
    }

    /**
     * Wait until there is a message in the queue and return it.
     *
     * @return the oldest message received from the client
     */
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

    /**
     * Handle the ping received from a client.
     *
     * @param username username of the client that sent the ping
     */
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

    /**
     * Send a ping to a client.
     *
     * @param username username of the client to ping
     */
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
