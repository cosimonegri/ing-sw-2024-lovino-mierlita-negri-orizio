package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when server tries to start game before all players have joined
 */
public class StillWaitingPlayersException extends Exception{
    public StillWaitingPlayersException(){ super(); }
}
