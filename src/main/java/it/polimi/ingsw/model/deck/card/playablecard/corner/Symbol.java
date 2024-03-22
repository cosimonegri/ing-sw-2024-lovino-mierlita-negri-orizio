package it.polimi.ingsw.model.deck.card.playablecard.corner;

import java.util.Arrays;
import java.util.List;

/**
 * Enumeration for the symbols of the game. They can be either resources or items.
 */
public enum Symbol {
    ANIMAL("Animal"),
    FUNGI("Fungi"),
    INSECT("Insect"),
    PLANT("Plant"),
    MANUSCRIPT("Manuscript"),
    INKWELL("Inkwell"),
    QUILL("Quill");

    /**
     * Textual representation of the resource.
     */
    private final String name;

    Symbol(String name) {
        this.name = name;
    }

    public List<Symbol> getResources() {
        return Arrays.asList(Symbol.ANIMAL,
                             Symbol.FUNGI,
                             Symbol.INSECT,
                             Symbol.PLANT);
    }

    public List<Symbol> getItems() {
        return Arrays.asList(Symbol.INKWELL,
                             Symbol.QUILL,
                             Symbol.MANUSCRIPT);
    }

    @Override
    public String toString() { return this.name; }
}
