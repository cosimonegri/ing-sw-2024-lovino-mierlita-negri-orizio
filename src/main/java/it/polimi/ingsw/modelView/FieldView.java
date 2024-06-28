package it.polimi.ingsw.modelView;

import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.model.player.Field;
import it.polimi.ingsw.model.player.PlacedCard;

import java.io.Serializable;
import java.util.*;

/**
 * Immutable version of the {@link Field} class of the model
 */
public class FieldView implements Serializable {
    private final PlacedCard[][] placedCards;
    private final int cardsCount;
    private final Coordinates topLeftBound;
    private final Coordinates bottomRightBound;
    private final List<Coordinates> allValidCoords;

    public FieldView(Field field) {
        this.placedCards = new PlacedCard[field.size()][field.size()];
        for (int x = 0; x < field.size(); x++) {
            for (int y = 0; y < field.size(); y++) {
                Coordinates coords = new Coordinates(x, y);
                this.placedCards[x][y] = field.getPlacedCard(coords);
            }
        }
        this.cardsCount = field.getCardsCount();
        this.topLeftBound = field.getTopLeftBound();
        this.bottomRightBound = field.getBottomRightBound();
        this.allValidCoords = new ArrayList<>(field.getAllValidCoords());
    }

    public int size() {
        return placedCards.length;
    }

    public int getCardsCount() {
        return this.cardsCount;
    }

    public List<Coordinates> getAllValidCoords(){
        return Collections.unmodifiableList(this.allValidCoords);
    }

    /**
     * @param coords coordinates
     * @return whether the coordinates are out of bound
     */
    public boolean areCoordsOutOfBound(Coordinates coords) {
        return coords.x() < 0 || coords.x() >= size() || coords.y() < 0 || coords.y() >= size();
    }

    /**
     * This method must be called with valid coordinates
     *
     * @param coords cooridnates of a card
     * @return the PlacedCard at the given coordinates
     */
    public PlacedCard getPlacedCard(Coordinates coords) {
        return this.placedCards[coords.x()][coords.y()];
    }

    /**
     * This method must be called with a valid placementIndex (from 0 to cardsCount - 1)
     *
     * @param placementIndex placementIndex of a card
     * @return the coordinates of the field in which there is the card with the given placement index
     */
    public Coordinates getCoords(int placementIndex) {
        for (int x = 0; x < size(); x++) {
            for (int y = 0; y < size(); y++) {
                Coordinates coords = new Coordinates(x, y);
                PlacedCard placedCard = getPlacedCard(coords);
                if (placedCard != null && placedCard.placementIndex() == placementIndex) {
                    return coords;
                }
            }
        }
        return null;
    }

    public Coordinates getTopLeftBound() {
        return this.topLeftBound;
    }

    public Coordinates getBottomRightBound() {
        return this.bottomRightBound;
    }
}
