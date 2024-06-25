package it.polimi.ingsw.model;

import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void boardSetUp() throws IOException {
        board = new Board();
    }
    @Test
    public void boardSizeCheck(){
        assertEquals(38, board.getGoldDeck().size());
        assertEquals(38, board.getResourceDeck().size());
        assertEquals(16, board.getObjectiveDeck().size());
        assertEquals(6, board.getStarterDeck().size());
    }

    @Test
    public void replaceVisibleCardTest(){
        //case: taking goldCard from board, both decks are not empty
        board.replaceVisibleCard(board.getVisibleCards()[0]);
        //checking if goldDeck size decreased and Visiblecards is still full
        assertEquals(4, board.getVisibleCards().length);
        assertEquals(37, board.getGoldDeck().size());
        //case:taking resourceCard, both decks are not empty
        board.replaceVisibleCard(board.getVisibleCards()[2]);
        assertEquals(4, board.getVisibleCards().length);
        assertEquals(37, board.getResourceDeck().size());

        for(int i = 0; i < 37 ; i++)
            board.getGoldDeck().draw();
        //case: taking goldCard but GoldDeck is emtpy
        assertEquals(0, board.getGoldDeck().size());
        board.replaceVisibleCard(board.getVisibleCards()[0]);
        assertInstanceOf(PlayableCard.class, board.getVisibleCards()[0]);
        assertEquals(36, board.getResourceDeck().size());

        for(int i = 0; i< 36; i++){
            board.getResourceDeck().draw();
        }
        //case: both decks are empty, trying to draw
        assertEquals(0, board.getResourceDeck().size());
        board.replaceVisibleCard(board.getVisibleCards()[0]);
        assertNull(board.getVisibleCards()[0]);
    }
}
