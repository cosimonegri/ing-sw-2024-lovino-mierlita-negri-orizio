package it.polimi.ingsw.model.deck.card.playablecard.corner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enumeration for the symbols of the game. They can be either resources or items.
 */
public interface Symbol {
    static List<Symbol> values() {
        List<Symbol> symbols = new ArrayList<>();
        symbols.addAll(Arrays.asList(Resource.values()));
        symbols.addAll(Arrays.asList(Item.values()));
        return symbols;
    }
}