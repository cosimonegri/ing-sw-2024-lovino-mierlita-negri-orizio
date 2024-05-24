package it.polimi.ingsw.exceptions;

public class CardNotOnBoardException extends Exception {
    public CardNotOnBoardException() {
        super("This card is not visible on the board.");
    }
}
