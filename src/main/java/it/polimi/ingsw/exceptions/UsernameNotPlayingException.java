package it.polimi.ingsw.exceptions;

public class UsernameNotPlayingException extends Exception {
    public UsernameNotPlayingException(String username) {
        super(username + " is not playing");
    }
}
