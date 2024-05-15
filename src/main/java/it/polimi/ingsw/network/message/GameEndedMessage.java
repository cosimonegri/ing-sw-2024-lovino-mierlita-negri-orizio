package it.polimi.ingsw.network.message;

import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;

public class GameEndedMessage implements ServerToClientMessage {
    private final String message;
    public GameEndedMessage(String message){
        this.message = message;
    }
    public String toString() {
        return message;
    }
    public void execute(){
        System.out.println(message);
    }
}
