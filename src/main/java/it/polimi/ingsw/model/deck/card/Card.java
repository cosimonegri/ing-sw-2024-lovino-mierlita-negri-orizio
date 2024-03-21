package it.polimi.ingsw.model.deck.card;

/**
 *
 * @author Orizio Davide
 *
 * A general card
 */
public abstract class Card {
    private final int points;
    private final String frontImage;
    private final String backImage;

    /**
     *
     * @param points: the number of points obtained playing/completing the card
     * @param frontImage: path to the front image
     * @param backImage: path to the back image
     * @throws NullPointerException: when a path is null
     * @throws IllegalArgumentException: if points is a negative number
     */
    public Card(int points,
                String frontImage,
                String backImage) throws NullPointerException, IllegalArgumentException {
        if ((frontImage == null) || (backImage == null)) throw new NullPointerException("String cannot be null");
        if (points < 0) throw new IllegalArgumentException("Points must be a positive integer");
        this.points = points;
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
}
