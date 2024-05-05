package it.polimi.ingsw.view;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class View {
    private final Client client;
    private final List<ViewListener> listeners;
    protected String username;

    public View(Client client) {
        this.client = client;
        this.listeners = new ArrayList<>();
    }

    public Client getClient() {
        return this.client;
    }

    public void addListener(ViewListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ViewListener listener) {
        this.listeners.remove(listener);
    }

    protected void notifyAllListeners(Message message) {
        for (ViewListener listener : this.listeners) {
            listener.updateFromView(message);
        }
    }

    public void run() {
        this.client.connectToServer(this);
    }
}
