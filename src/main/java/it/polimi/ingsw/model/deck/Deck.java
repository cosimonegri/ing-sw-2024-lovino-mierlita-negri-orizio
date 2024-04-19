package it.polimi.ingsw.model.deck;


import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Item;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;

import java.util.*;

import static it.polimi.ingsw.model.deck.card.playablecard.corner.Resource.*;

/**
 *
 * @param <T> the type of card to be used
 */
public abstract class Deck<T extends Card> {
    /**
     * Deck is implemented as a list of cards
     */
    protected final List<T> cards;
    /**
     * This map is used for parsing the string attributes of the class ParsedCard into the attributes of the card(Gold, Resource...)
     */
    protected  Map<String, Resource> stringToResource = new HashMap<>();
    /**
     * This map is used for parsing the string attributes of the class ParsedCard into the attributes of the card(Gold, Resource...)
     */
    protected Map<String, Item> stringToItem = new HashMap<>();
    /**
     * This map is used for parsing the string attributes of the class ParsedCard into the attributes of the card(Gold, Resource...)
     */
    protected Map<String, Position> stringToPosition = new HashMap<>();



    /**
     * Create a list of cards and initialize the maps used for parsing
     */
    public Deck()  {
        cards = new ArrayList<>();
        stringToResource.put("fungi", FUNGI);
        stringToResource.put("animal", ANIMAL);
        stringToResource.put("plant", PLANT);
        stringToResource.put("insect", INSECT);
        stringToResource.put("empty",null);
        stringToItem.put("inkwell", Item.INKWELL);
        stringToItem.put("manuscript", Item.MANUSCRIPT);
        stringToItem.put("quill", Item.QUILL);
        stringToPosition.put("top-right", Position.TOPRIGHT);
        stringToPosition.put("top-left", Position.TOPLEFT);
        stringToPosition.put("bottom-left", Position.BOTTOMLEFT);
        stringToPosition.put("bottom-right", Position.BOTTOMRIGHT);
    }

    /**
     * @return the first card of the deck (meanwhile the card is removed)
     */
    public T draw() {
        if (cards.isEmpty()) throw new IllegalStateException("Deck is empty");
        return cards.removeFirst();
    }

    public List<T> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public boolean isDeckEmpty(){
        return this.cards.isEmpty();
    }
}
