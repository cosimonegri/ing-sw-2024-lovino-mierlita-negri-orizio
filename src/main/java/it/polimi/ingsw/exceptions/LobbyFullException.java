package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player tries to enter a full lobby
 */
public class LobbyFullException extends Exception{
    public LobbyFullException() {
        super("This lobby is already full");
    }
}
