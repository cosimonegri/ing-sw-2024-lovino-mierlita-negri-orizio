package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when the player tries to choose a username already taken
 */
public class UsernameAlreadyTakenException extends Exception{
    public UsernameAlreadyTakenException() {
        super("This username is already taken by an other player");
    }
}
