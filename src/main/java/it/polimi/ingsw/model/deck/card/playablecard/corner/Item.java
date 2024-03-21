package it.polimi.ingsw.model.deck.card.playablecard.corner;

/**
 * Enumeration for the item symbols.
 */
public enum Item implements Symbol {
    MANUSCRIPT("Manuscript"), INKWELL("Inkwell"), QUILL("Quill");

    /**
     * Textual representation of the item.
     */
    private final String name;

    Item(String name) {
        this.name = name;
    }

    @Override
    public String toString() { return this.name; }
}
