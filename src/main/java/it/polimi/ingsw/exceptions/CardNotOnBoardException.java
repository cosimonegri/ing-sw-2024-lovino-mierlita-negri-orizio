package it.polimi.ingsw.exceptions;

/**
 * When the card to draw is not visible on the board
 */
public class CardNotOnBoardException extends Exception {
    public CardNotOnBoardException() {
        super("This card is not visible on the board.");
    }
}
