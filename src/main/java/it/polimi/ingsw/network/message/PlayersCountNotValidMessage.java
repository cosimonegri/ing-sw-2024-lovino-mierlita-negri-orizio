package it.polimi.ingsw.network.message;

public class PlayersCountNotValidMessage implements Message{
    private final String message;
    public PlayersCountNotValidMessage(String message){
        this.message = message;
    }
    public String toString() {
        return message;
    }

    public void execute(){
        System.out.println(message);
    }
}
