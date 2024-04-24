package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when server tries to advance turn before starting a game
 */
public class GameNotStartedYetException extends Exception{
    public GameNotStartedYetException(){ super(); }
}
