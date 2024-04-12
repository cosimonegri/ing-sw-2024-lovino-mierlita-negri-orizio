package it.polimi.ingsw.model.deck;


import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Item;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static it.polimi.ingsw.model.deck.card.playablecard.corner.Resource.*;
import static it.polimi.ingsw.model.deck.card.playablecard.corner.Resource.INSECT;

/**
 *
 * @param <T> the type of card to be used
 */
public abstract class Deck<T extends Card> {

    protected final List<T> cards;
    protected  Map<String, Resource> stringToResource = new HashMap<>();
    protected Map<String, Item> stringToItem = new HashMap<>();
    protected Map<String, Position> stringToPosition = new HashMap<>();



    /**
     * Create a list of cards from a JSON file and initialize it
     */
    public Deck()  {
        cards = new ArrayList<>();
        // Parse the JSON file
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

    public T draw() {
        if (cards.isEmpty()) throw new IllegalStateException("Deck is empty");
        return cards.removeFirst();
    }

    public List<T> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public boolean isEmpty(){
        return this.cards.isEmpty();
    }
}
