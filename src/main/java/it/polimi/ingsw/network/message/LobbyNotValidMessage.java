package it.polimi.ingsw.network.message;

public class LobbyNotValidMessage {
    private final String message;
    public LobbyNotValidMessage(String message){
        this.message = message;
    }
    public String toString() {
        return message;
    }
    public void execute(){
        System.out.println(message);
    }
}
