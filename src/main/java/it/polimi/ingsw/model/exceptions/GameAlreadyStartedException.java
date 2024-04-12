package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the game for the current lobby has already started
 */
public class GameAlreadyStartedException extends Exception{
    public GameAlreadyStartedException(){ super(); }
}
