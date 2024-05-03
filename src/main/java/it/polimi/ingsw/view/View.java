package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.utilities.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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

    public void notifyAllListeners(Message message) {
        for (ViewListener listener : this.listeners) {
            listener.updateFromView(message);
        }
    }

    public void run(){}
}
