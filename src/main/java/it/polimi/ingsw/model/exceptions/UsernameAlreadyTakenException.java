package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the player tries to choose a username already taken
 */
public class UsernameAlreadyTakenException extends Exception{
    public UsernameAlreadyTakenException() { super(); }
}
