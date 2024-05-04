package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class View {
    private final List<ViewListener> listeners;

    public View() {
        this.listeners = new ArrayList<>();
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

    public abstract void run();
}
