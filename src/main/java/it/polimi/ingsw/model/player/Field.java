package it.polimi.ingsw.model.player;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.*;
import it.polimi.ingsw.model.exceptions.CoordinatesAreNotValidException;

/**
 * Field class to represent the placed cards of a player.
 *
 */
public class Field {
    /**
     * Maximum number of rows and columns in the field.
     */
    static final int SIZE = 81;
    /**
     * The field is a matrix of placed cards. To get the cards attached to a given card,
     * have to look in the 4 diagonal directions.
     */
    private final PlacedCard[][] placedCards;
    /**
     * The number of cards placed on the field.
     */
    private int cardsCount;

    /**
     * Constructor of the class
     */
    public Field() {
        this.placedCards = new PlacedCard[SIZE][SIZE];
        this.cardsCount = 0;
    }

    /**
     * @return the number of cards placed on the field
     */
    public int getCardsCount() { return this.cardsCount; }

    /**
     * Add a card to the field.
     *
     * @param card the card to add
     * @param flipped true if the card is placed on its back, false if it is placed on its front
     * @param coords x and y coordinates
     * @throws IllegalArgumentException when parameters are null
     * @throws ArrayIndexOutOfBoundsException when the coordinates are out of bound
     * @throws CoordinatesAreNotValidException when the coordinates are not valid
     */
    public void addCard(PlayableCard card, boolean flipped, Coordinates coords) throws CoordinatesAreNotValidException {
        if (card == null) {
            throw new IllegalArgumentException("The card cannot be null");
        }
        if (coords == null) {
            throw new IllegalArgumentException("The coordinates cannot be null");
        }
        if (areCoordsOutOfBound(coords.x(), coords.y())) {
            throw new ArrayIndexOutOfBoundsException("The coordinates are out of bound.");
        }
        if (!areCoordsValid(coords.x(), coords.y())) {
            throw new CoordinatesAreNotValidException();
        }
        this.placedCards[coords.x()][coords.y()] = new PlacedCard(card, flipped, this.cardsCount);
        this.cardsCount++;
    }

    /**
     * @param coords x and y coordinates
     * @return the placed card at the given coordinates
     */
    public PlacedCard getPlacedCard(Coordinates coords) {
        if (areCoordsOutOfBound(coords.x(), coords.y())) {
            throw new ArrayIndexOutOfBoundsException("The coordinates are out of bound.");
        }
        return this.placedCards[coords.x()][coords.y()];
    }

    /**
     * @return a list containing all the coordinates where the player could place a card
     */
    public List<Coordinates> getAllValidCoords() {
        List<Coordinates> coords = new ArrayList<>();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (areCoordsValid(x, y)) {
                    coords.add(new Coordinates(x, y));
                }
            }
        }
        return coords;
    }

    /**
     * @param symbol the symbol to count
     * @return the amount of those symbols on the field, without counting the covered ones
     * @throws IllegalArgumentException when the parameter is null
     */
    public int getSymbolCount(Symbol symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("The symbol cannot be null");
        }
        int count = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                // if there isn't a card, there is no symbol to count
                if (this.placedCards[x][y] == null) {
                    continue;
                }
                PlayableCard card = this.placedCards[x][y].card();
                boolean flipped = this.placedCards[x][y].flipped();
                // if the card is placed on its back, analyze the back resources
                if (flipped) {
                    for (Symbol resource : card.getBackResources()) {
                        if (resource == symbol) {
                            count++;
                        }
                    }
                }
                for (CornerPos cornerPos : CornerPos.values()) {
                    // if the symbol on a corner is different, don't count it
                    if (card.getCorner(cornerPos, flipped).symbol() != symbol) {
                        continue;
                    }
                    // if the symbol on a corner is the same, count it only if it is not covered
                    Coordinates neighborCoords = getNeighborOfCornerPos(cornerPos, x, y);
                    if (areCoordsOutOfBound(neighborCoords.x(), neighborCoords.y())
                            || this.placedCards[neighborCoords.x()][neighborCoords.y()] == null
                            || this.placedCards[neighborCoords.x()][neighborCoords.y()].placementIndex() < this.placedCards[x][y].placementIndex()
                    ) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * @param x x coordinate
     * @param y y coordinate
     * @return whether the coordinates are out of bound
     */
    private boolean areCoordsOutOfBound(int x, int y) {
        return x < 0 || x >= SIZE || y < 0 || y >= SIZE;
    }

    /**
     * @param x x coordinate
     * @param y y coordinate
     * @return whether a card can be placed at the given coordinates
     */
    private boolean areCoordsValid(int x, int y) {
        if (areCoordsOutOfBound(x, y)) {
            return false;
        }
        // the first card can be placed only at the center of the field
        if (this.cardsCount == 0) {
            return x == SIZE / 2 && y == SIZE / 2;
        }
        // since we can attach cards only in a diagonal direction, half of the coordinates in the field cannot be valid.
        // a coordinate is not valid if there is already a card in it.
        if ((x + y) % 2 == 1 || this.placedCards[x][y] != null) {
            return false;
        }
        int visibleAdjacentCorners = 0;
        int hiddenAdjacentCorners = 0;
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dy = -1; dy <= 1; dy += 2) {
                // if there is no neighbor card, skip it
                if (areCoordsOutOfBound(x + dx, y + dy) || this.placedCards[x + dx][y + dy] == null) {
                    continue;
                }
                // if there is a neighbor card, increase the correct counter based on the type of the corner of the
                // neighbor card that is adjacent to the current position
                PlayableCard neighborCard = this.placedCards[x + dx][y + dy].card();
                boolean neighborFlipped = this.placedCards[x + dx][y + dy].flipped();
                CornerPos neighborCornerPos = getCornerPosOfNeighbor(dx, dy);
                if (neighborCard.getCorner(neighborCornerPos, neighborFlipped).type() == CornerType.VISIBLE) {
                    visibleAdjacentCorners++;
                } else {
                    hiddenAdjacentCorners++;
                }
            }
        }
        return visibleAdjacentCorners >= 1 && hiddenAdjacentCorners == 0;
    }

    /**
     * Get the corner position of the neighbor card that might cover the current card<br>
     *
     * E.g. If dx=1 and dy=1 it means that the neighbor card is the one at the top-right of the current card.
     * So in that case bottom-left is returned because that is the corner of the neighbor card that might cover the current card.
     *
     * @param dx difference between the x coordinate of the neighbor and the x coordinate of the current card
     * @param dy difference between the y coordinate of the neighbor and the y coordinate of the current card
     * @return the corner position of the neighbor card
     */
    private CornerPos getCornerPosOfNeighbor(int dx, int dy) {
        if (dx == 1 && dy == -1) {
            return CornerPos.TOPLEFT;
        }
        if (dx == -1 && dy == -1) {
            return CornerPos.TOPRIGHT;
        }
        if (dx == 1 && dy == 1) {
            return CornerPos.BOTTOMLEFT;
        }
        if (dx == -1 && dy == 1) {
            return CornerPos.BOTTOMRIGHT;
        }
        throw new IllegalArgumentException("Both parameters must be 1 or -1");
    }

    /**
     * Get the coordinates of the neighbor card that might cover the specified corner of the card passed as parameter<br>
     *
     * E.g. If x=10, y=10 and cornerPos=top-right, it returns x=11 and y=11 because those are the coordinates that might
     * cover that corner position
     *
     * @param cornerPos corner position of the current card
     * @param x x coordinate of that card
     * @param y y coordinate of that card
     * @return the coordinates of the neighbor card
     */
    private Coordinates getNeighborOfCornerPos(CornerPos cornerPos, int x, int y) {
        if (cornerPos == CornerPos.TOPLEFT) {
            return new Coordinates(x - 1, y + 1);
        }
        else if (cornerPos == CornerPos.TOPRIGHT) {
            return new Coordinates(x + 1, y + 1);
        }
        else if (cornerPos == CornerPos.BOTTOMLEFT) {
            return new Coordinates(x - 1, y - 1);
        }
        else {
            return new Coordinates(x + 1, y - 1);
        }
    }

    /**
     * Get the coordinates of the required card
     *
     * @param id of the card
     * @return the coordinates of the card
     */
    public Coordinates findCard(int id) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (this.placedCards[x][y] != null && this.placedCards[x][y].card().getId() == id) return new Coordinates(x, y);
            }
        }
        return null;
    }


    /**
     * @param coords of the given position
     * @return the number of cards around the given position
     */

    public int numOfNeighbors(Coordinates coords){
        int i = 0;
        if(!areCoordsOutOfBound(coords.x() + 1, coords.y() + 1) && this.placedCards[coords.x() + 1][coords.y() + 1] != null){
            i++;}
        else if(!areCoordsOutOfBound(coords.x() + 1, coords.y() - 1) && this.placedCards[coords.x() + 1][coords.y() - 1] != null){
            i++;}
        else if(!areCoordsOutOfBound(coords.x() - 1, coords.y() - 1) && this.placedCards[coords.x() - 1][coords.y() - 1] != null){
            i++;}
        else if(!areCoordsOutOfBound(coords.x() - 1, coords.y() + 1) && this.placedCards[coords.x() - 1][coords.y() + 1] != null){
            i++;}
        return i;
    }
}
