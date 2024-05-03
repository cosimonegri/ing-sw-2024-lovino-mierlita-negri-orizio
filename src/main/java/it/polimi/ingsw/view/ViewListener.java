package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.Message;

public interface ViewListener {
    public void updateFromView(Message m);
}
