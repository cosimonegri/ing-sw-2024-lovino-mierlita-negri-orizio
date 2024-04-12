package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown a player tries to choose an already taken marker
 */
public class MarkerAlreadyTakenException extends Exception{
    public MarkerAlreadyTakenException(){ super(); }
}
