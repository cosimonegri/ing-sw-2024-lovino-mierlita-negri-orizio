package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when a player tries to enter a full lobby
 */
public class LobbyFullException extends Exception{
    public LobbyFullException(){ super(); }
}
