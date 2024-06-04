package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player tries to make an action that,
 * given the current state of the game, he cannot make.
 */
public class ActionNotValidException extends Exception {
    public ActionNotValidException() {
        super();
    }
}
