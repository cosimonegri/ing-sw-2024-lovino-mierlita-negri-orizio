package it.polimi.ingsw.network.message;

public class UsernameNotValidMessage implements Message{
    private final String message;

    public UsernameNotValidMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void execute() {
        System.out.println(message);
    }
}

