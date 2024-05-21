package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the player tries to place a gold card in the field
 * when he doesn't have enough resources.
 */
public class NotEnoughResourcesException extends RuntimeException {
    public NotEnoughResourcesException() {
        super();
    }
}

