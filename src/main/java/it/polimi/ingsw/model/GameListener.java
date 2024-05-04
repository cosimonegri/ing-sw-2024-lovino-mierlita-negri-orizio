package it.polimi.ingsw.model;

import it.polimi.ingsw.network.message.Message;

public interface GameListener {
    public void updateFromModel(Message message);
}
