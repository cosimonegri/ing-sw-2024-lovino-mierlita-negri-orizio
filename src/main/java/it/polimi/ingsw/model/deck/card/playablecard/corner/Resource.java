package it.polimi.ingsw.model.deck.card.playablecard.corner;

/**
 * Enumeration for the resource symbols.
 */
public enum Resource implements Symbol {
    ANIMAL("Animal"), FUNGI("Fungi"), INSECT("Insect"), PLANT("Plant");

    /**
     * Textual representation of the resource.
     */
    private final String name;

    Resource(String name) {
        this.name = name;
    }

    @Override
    public String toString() { return this.name; }
}
