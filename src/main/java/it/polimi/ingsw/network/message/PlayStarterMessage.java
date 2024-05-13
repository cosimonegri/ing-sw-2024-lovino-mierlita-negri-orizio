package it.polimi.ingsw.network.message;


public class PlayStarterMessage implements Message{
    private final String username;
    private final boolean flipped;

    public PlayStarterMessage(String username, boolean flipped){
        this.username = username;
        this.flipped = flipped;
    }


    public String getUsername() {
        return username;
    }

    public boolean isFlipped() {
        return flipped;
    }
}
