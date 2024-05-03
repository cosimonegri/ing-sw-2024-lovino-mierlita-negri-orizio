package it.polimi.ingsw.network.message;

import java.io.Serializable;

public interface Message extends Serializable {
    public void execute();
}
