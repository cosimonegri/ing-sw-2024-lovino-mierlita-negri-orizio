package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when the player tries to place a gold card in the field
 * when he doesn't have enough resources.
 */
public class NotEnoughResourcesException extends Exception {
    public NotEnoughResourcesException() {
        super("You don't have enought resources on the field.");
    }
}

