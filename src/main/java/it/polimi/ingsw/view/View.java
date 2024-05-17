package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class View extends Application {
    private final List<ViewListener> listeners;
    private final Queue<ServerToClientMessage> messages;
    protected String username;

    public View() {
        this.listeners = new ArrayList<>();
        this.messages = new LinkedList<>();
    }

    public void addListener(ViewListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ViewListener listener) {
        this.listeners.remove(listener);
    }

    protected void notifyAllListeners(ClientToServerMessage message) {
        for (ViewListener listener : this.listeners) {
            listener.updateFromView(message);
        }
    }

    public void addMessage(ServerToClientMessage message) {
        synchronized (messages) {
            if (message != null) {
                this.messages.add(message);
                messages.notifyAll();
            }
        }
    }

    protected ServerToClientMessage pollMessage() {
        synchronized (messages) {
            return messages.poll();
        }
    }

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

    abstract public void run();
}
