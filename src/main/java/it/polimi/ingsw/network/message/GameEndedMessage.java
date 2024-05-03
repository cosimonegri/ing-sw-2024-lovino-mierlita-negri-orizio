package it.polimi.ingsw.network.message;

public class GameEndedMessage {
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
