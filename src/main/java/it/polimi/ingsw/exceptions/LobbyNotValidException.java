package it.polimi.ingsw.exceptions;

/**
 * When the requested game id does not exist
 */
public class LobbyNotValidException extends Exception {
    public LobbyNotValidException() {
        super("This lobby doesn't exists");
    }
}
