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
     * Path to the front image.
     */
    private final String frontImage;
    /**
     * Path to the back image.
     */
    private final String backImage;
    /**
     * Unique identifier of the card.
     */
    private final int id;

    /**
     *
     * @param points the number of points obtained playing/completing the card
     * @param id identifier of the card
     * @param frontImage path to the front image
     * @param backImage path to the back image
     * @throws IllegalArgumentException if points is a negative number
     */
    public Card(int points,
                int id,
                String frontImage,
                String backImage) {
        if (points < 0) throw new IllegalArgumentException("Points must be a positive integer");
        this.points = points;
        this.id = id;
        this.frontImage = frontImage;
        this.backImage = backImage;
    }

    public int getPoints() {
        return this.points;
    }

    public String getFrontImage() {
        return this.frontImage;
    }

    public String getBackImage() {
        return this.backImage;
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
