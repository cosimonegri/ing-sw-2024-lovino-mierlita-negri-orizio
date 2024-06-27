package it.polimi.ingsw.exceptions;

/**
 * When the username in the message is not connected
 */
public class UsernameNotPlayingException extends Exception {
    public UsernameNotPlayingException(String username) {
        super(username + " is not playing");
    }
}
