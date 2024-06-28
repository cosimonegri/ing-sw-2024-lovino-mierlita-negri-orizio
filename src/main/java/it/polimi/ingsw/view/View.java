package it.polimi.ingsw.view;

import it.polimi.ingsw.modelView.GameView;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import javafx.application.Application;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Class representing the user interface.
 */
public abstract class View extends Application {
    /**
     * Listeners of the view.
     */
    private final List<ViewListener> listeners;
    /**
     * Queue of messages received from the server.
     */
    private final Queue<ServerToClientMessage> messages;
    /**
     * Username choosen by the user.
     */
    protected String username;
    /**
     * Object periodically received from the server containing all the information about the game.
     */
    protected GameView gameView;

    /**
     * Constructor of the class.
     */
    public View() {
        this.listeners = new ArrayList<>();
        this.messages = new LinkedList<>();
    }

    public String getUsername() { return username; }

    public GameView getGameView() { return gameView; }

    /**
     * Add a listener to the view.
     *
     * @param listener listener to be added
     */
    public void addListener(ViewListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Notify all the liteners of the view.
     *
     * @param message message to send
     */
    public void notifyAllListeners(ClientToServerMessage message) {
        for (ViewListener listener : this.listeners) {
            listener.updateFromView(message);
        }
    }

    /**
     * Add a message to the messages queue of the view.
     *
     * @param message the message to update the view
     */
    public void addMessage(ServerToClientMessage message) {
        synchronized (messages) {
            if (message != null) {
                this.messages.add(message);
                messages.notifyAll();
            }
        }
    }

    /**
     * Wait until there is a message in the queue and return it.
     *
     * @return the oldest message received from the server
     */
    protected ServerToClientMessage waitForMessage() {
        synchronized (messages) {
            while (this.messages.isEmpty()) {
                try {
                    messages.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server response");
                    System.exit(1);
                }
            }
            return this.messages.poll();
        }
    }

    /**
     * Run the view.
     */
    abstract public void run();
}
