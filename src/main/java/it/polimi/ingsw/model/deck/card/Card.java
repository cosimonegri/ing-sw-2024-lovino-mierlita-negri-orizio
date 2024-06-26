package it.polimi.ingsw.model.deck.card;

import java.io.Serializable;

/**
 * A general card.
 */
public abstract class Card implements Serializable {
    /**
     * Number of points given by the card.
     */
    private final int points;
    /**
     * Unique identifier of the card.
     */
    private final int id;

    /**
     *
     * @param points the number of points obtained playing/completing the card
     * @param id identifier of the card
     * @throws IllegalArgumentException if points is a negative number
     */
    public Card(int points, int id) {
        if (points < 0) throw new IllegalArgumentException("Points must be a positive integer");
        this.points = points;
        this.id = id;
    }

    public int getPoints() {
        return this.points;
    }

    public int getId() { return this.id; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Card other)) {
            return false;
        }
        return this.id == other.id;
    }

}
