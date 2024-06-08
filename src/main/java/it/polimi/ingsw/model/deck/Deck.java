package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.deck.card.Card;

import java.util.*;

/**
 * Class of the deck
 *
 * @param <T> the type of cards in the deck
 */
public class Deck<T extends Card> {
    /**
     * list of cards
     */
    private final List<T> cards;

    /**
     * @param cards list of cards
     */
    public Deck(List<T> cards) {
        this.cards = new ArrayList<>(cards);
        Collections.shuffle(this.cards);
    }

    /**
     * Remove the card at the top of the deck and return it. The deck must not be empty.
     *
     * @return the card at the top of the deck
     */
    public T draw() {
        return cards.removeLast();
    }

    /**
     * @return the card at the top of the deck if the deck is not empty, otherwise null
     */
    public T getTop() {
        if (this.cards.isEmpty()) {
            return null;
        }
        return cards.getLast();
    }
    public List<T> getCards(){
        return this.cards;
    }

    /**
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty(){
        return this.cards.isEmpty();
    }
}
