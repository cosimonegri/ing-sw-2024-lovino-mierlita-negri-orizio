package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.message.Message;

public interface GameListener {
    public void updateFromModel(Message message);
}
