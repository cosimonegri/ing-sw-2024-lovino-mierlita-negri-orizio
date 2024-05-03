package it.polimi.ingsw.network.message;

public class ActionNotValidMessage {
    private final String message;
    public ActionNotValidMessage(String message){
        this.message = message;
    }
    public String toString() {
        return message;
    }
    public void execute(){
        System.out.println(message);
    }
}

