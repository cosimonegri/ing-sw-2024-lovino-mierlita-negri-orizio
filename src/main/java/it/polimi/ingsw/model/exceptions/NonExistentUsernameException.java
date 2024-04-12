package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the controller tries to remove a nonexistent player
 */
public class NonExistentUsernameException extends Exception{
    public NonExistentUsernameException() {super();}
}
