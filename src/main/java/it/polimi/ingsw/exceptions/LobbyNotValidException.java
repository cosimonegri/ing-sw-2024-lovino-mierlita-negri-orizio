package it.polimi.ingsw.exceptions;

public class LobbyNotValidException extends Exception {
    public LobbyNotValidException() {
        super("This lobby doesn't exists");
    }
}
