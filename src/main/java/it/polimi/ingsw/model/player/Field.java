package it.polimi.ingsw.model.player;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.*;
import it.polimi.ingsw.exceptions.CoordinatesNotValidException;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.modelView.FieldView;

/**
 * Field class to represent the placed cards of a player.
 *
 */
public class Field {
    /**
     * Maximum number of rows and columns in the field.
     */
    public static final int SIZE = 81;
    /**
     * The field is a matrix of placed cards. To get the cards attached to a given card,
     * you have to look in the 4 diagonal directions.
     * This means that in a full field half of the cells are always going to be empty.
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
     * @return the dimensions of the squared field
     */
    public int size() { return SIZE; }

    /**
     * @return the number of cards placed on the field
     */
    public int getCardsCount() { return this.cardsCount; }

    /**
     * Add a card to the field.
     *
     * @param card the card to add
     * @param flipped true if the back side should be visible, false otherwise
     * @param coords x and y coordinates
     * @throws CoordinatesNotValidException when the player cannot place a card at the given coordinates
     * @throws NotEnoughResourcesException when the player doesn't have enought resource to place a gold card
     */
    public void addCard(PlayableCard card, boolean flipped, Coordinates coords) throws CoordinatesNotValidException, NotEnoughResourcesException {
        if (!areCoordsValid(coords.x(), coords.y())) {
            throw new CoordinatesNotValidException();
        }
        if (card instanceof GoldCard c && !flipped && !c.hasResourcesNeeded(this)) {
            throw new NotEnoughResourcesException();
        }
        this.placedCards[coords.x()][coords.y()] = new PlacedCard(card, flipped, this.cardsCount);
        this.cardsCount++;
    }

    /**
     * Add a card in the central position of the field.
     *
     * @param card the card to add
     * @param flipped true if the back side should be visible, false otherwise
     * @throws CoordinatesNotValidException when the player cannot place a card at the given coordinates
     * @throws NotEnoughResourcesException when the player doesn't have enought resource to place a gold card
     */
    public void addCentralCard(PlayableCard card, boolean flipped) throws CoordinatesNotValidException, NotEnoughResourcesException {
        addCard(card, flipped, new Coordinates(SIZE / 2, SIZE / 2));
    }

    /**
     * @param coords x and y coordinates; they must be valid
     * @return the placed card at the given coordinates
     */
    public PlacedCard getPlacedCard(Coordinates coords) {
        return this.placedCards[coords.x()][coords.y()];
    }

    /**
     * @return a list containing all the coordinates where the player can place a card
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
     */
    public int getSymbolCount(Symbol symbol) {
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
                for (Position cornerPos : Position.values()) {
                    // if the symbol on a corner is different, don't count it
                    if (card.getCorner(cornerPos, flipped).symbol() != symbol) {
                        continue;
                    }
                    // if the symbol on a corner is the same, count it only if it is not covered
                    Coordinates neighborCoords = getNeighborOfCornerPos(cornerPos, x, y);
                    if (areCoordsOutOfBound(neighborCoords)
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
     * @return the top-left coordinates of the used portion of the field, including the cells in which
     * the player could place a card
     */
    public Coordinates getTopLeftBound() {
        int leftX = size() - 1;
        int topY = 0;
        for (int x = 0; x < size(); x++) {
            for (int y = 0; y < size(); y++) {
                if (getPlacedCard(new Coordinates(x, y)) == null) {
                    continue;
                }
                leftX = Math.min(leftX, x);
                topY = Math.max(topY, y);
            }
        }
        for (Coordinates coords : getAllValidCoords()) {
            leftX = Math.min(leftX, coords.x());
            topY = Math.max(topY, coords.y());
        }
        return new Coordinates(leftX, topY);
    }

    /**
     * @return the bottom-right coordinates of the used portion of the field, including the cells in which
     * the player could place a card
     */
    public Coordinates getBottomRightBound() {
        int rightX = 0;
        int bottomY = size() - 1;
        for (int x = 0; x < size(); x++) {
            for (int y = 0; y < size(); y++) {
                if (getPlacedCard(new Coordinates(x, y)) == null) {
                    continue;
                }
                rightX = Math.max(rightX, x);
                bottomY = Math.min(bottomY, y);
            }
        }
        for (Coordinates coords : getAllValidCoords()) {
            rightX = Math.max(rightX, coords.x());
            bottomY = Math.min(bottomY, coords.y());
        }
        return new Coordinates(rightX, bottomY);
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
     * @param coords x and y coordinates
     * @return whether the coordinates are out of bound
     */
    private boolean areCoordsOutOfBound(Coordinates coords) {
        return areCoordsOutOfBound(coords.x(), coords.y());
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
                Position neighborCornerPos = getCornerPosOfNeighbor(dx, dy);
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
    private Position getCornerPosOfNeighbor(int dx, int dy) {
        if (dx == 1 && dy == -1) {
            return Position.TOPLEFT;
        }
        if (dx == -1 && dy == -1) {
            return Position.TOPRIGHT;
        }
        if (dx == 1 && dy == 1) {
            return Position.BOTTOMLEFT;
        }
        if (dx == -1 && dy == 1) {
            return Position.BOTTOMRIGHT;
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
    private Coordinates getNeighborOfCornerPos(Position cornerPos, int x, int y) {
        if (cornerPos == Position.TOPLEFT) {
            return new Coordinates(x - 1, y + 1);
        }
        else if (cornerPos == Position.TOPRIGHT) {
            return new Coordinates(x + 1, y + 1);
        }
        else if (cornerPos == Position.BOTTOMLEFT) {
            return new Coordinates(x - 1, y - 1);
        }
        else {
            return new Coordinates(x + 1, y - 1);
        }
    }

    /**
     * Get the coordinates of the required card
     *
     * @param card card to find on the field
     * @return the coordinates of the card if it exists in the field, otherwise null
     */
    public Coordinates findCard(PlayableCard card) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (this.placedCards[x][y] != null && this.placedCards[x][y].card().equals(card)) {
                    return new Coordinates(x, y);
                }
            }
        }
        return null;
    }

    /**
     * @param coords of the given position
     * @return the number of cards around the given position
     */
    public int numOfNeighbors(Coordinates coords) {
        int neighborsCount = 0;
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dy = -1; dy <= 1; dy += 2) {
                if (!areCoordsOutOfBound(coords.x() + dx, coords.y() + dy) && this.placedCards[coords.x() + dx][coords.y() + dy] != null) {
                    neighborsCount++;
                }
            }
        }
        return neighborsCount;
    }

    /**
     * @param color color of the cards of the pattern
     * @param mainDiagonal True if the direction of the diagonals is the one in which the longest diagonal
     * contains the elements in the positions (0, 0), (1, 1), (2, 2) etc., false otherwise
     * @return the maximum amount of matches for the given diagonal pattern
     */
    public int numOfDiagonalPatterns(Resource color, boolean mainDiagonal) {
        int timesCompleted = 0;
        // iterate through all the diagonal lines starting from the leftmost one,
        // skipping half of them because they are always empty
        for (int diag = 0; diag < 2 * SIZE - 1; diag += 2) {
            int sequence = 0;

            // for each diagonal, find the x and y coordinates of the leftmost element
            // and the number of elements in that diagonal
            int startX = Math.max(0, diag - SIZE + 1);
            int startY = mainDiagonal ? Math.max(0, SIZE - diag - 1) : Math.min(diag, SIZE - 1);
            int count = Math.min(SIZE, Math.min(SIZE - startX, diag + 1));

            // iterate through the diagonal, and count how many times the pattern is matched using a greedy approach
            for (int i = 0; i < count; i++) {
                int x = startX + i;
                int y = startY + (mainDiagonal ? 1 : -1) * i;
                PlacedCard placedCard = this.placedCards[x][y];

                // reset the current sequence if a card doesn't match and go to the next card
                if (placedCard == null || placedCard.card().getColor() != color) {
                    sequence = 0;
                    continue;
                }
                sequence++;
                // when a complete pattern is found, increase completion counter and reset current sequence
                if (sequence == 3) {
                    timesCompleted++;
                    sequence = 0;
                }
            }
        }
        return timesCompleted;
    }

    /**
     * @param mainColor color of the 2 vertical cards
     * @param thirdCardColor color of the third card
     * @param thirdCardPos  position of the third card, relatively to the other 2 cards of the pattern
     * @return the maximum amount of matches for the given vertical pattern
     */
    public int numOfVerticalPatterns(Resource mainColor, Resource thirdCardColor, Position thirdCardPos) {
        int timesCompleted = 0;
        for (int x = 0; x < SIZE; x++) {
            int sequence = 0;

            // Iterate through the column, and count how many times the pattern is matched using a greedy approach.
            // Start from a coordinate so that x + y is even, since half of them are always empty
            for (int y = x % 2; y < SIZE; y += 2) {
                PlacedCard placedCard = this.placedCards[x][y];
                // reset the current sequence if a card doesn't match and go to the next card
                if (placedCard == null || placedCard.card().getColor() != mainColor) {
                    sequence = 0;
                    continue;
                }

                sequence++;
                // when we find a match for the 2 vertical cards
                if (sequence == 2) {
                    Coordinates thirdCardCoords = getThirdCardCoords(thirdCardPos, x, y);

                    // reset the current sequence to 1 if third card doesn't match, since we could still find
                    // a match at the next iteration
                    if (areCoordsOutOfBound(thirdCardCoords)) {
                        sequence = 1;
                        continue;
                    }
                    PlacedCard thirdPlacedCard = this.placedCards[thirdCardCoords.x()][thirdCardCoords.y()];
                    if (thirdPlacedCard == null || thirdPlacedCard.card().getColor() != thirdCardColor) {
                        sequence = 1;
                        continue;
                    }
                    // otherwise, when a complete pattern is found, increase completion counter and reset current sequence
                    timesCompleted++;
                    sequence = 0;
                }
            }
        }
        return timesCompleted;
    }

    /**
     * @param thirdCardPos position of the third card of the vertical pattern, relatively to the other 2 cards
     * @param x x coordinate of the card (among the other 2 cards) with a higher y coordinate
     * @param y y coordinate of the card (among the other 2 cards) with a higher y coordinate
     * @return the coordinates of the third card of the vertical pattern
     */
    private Coordinates getThirdCardCoords(Position thirdCardPos, int x, int y) {
        if (thirdCardPos == Position.TOPLEFT) {
            return new Coordinates(x - 1, y + 1);
        }
        else if (thirdCardPos == Position.TOPRIGHT) {
            return new Coordinates(x + 1, y + 1);
        }
        else if (thirdCardPos == Position.BOTTOMLEFT) {
            return new Coordinates(x - 1, y - 3);
        }
        else  {
            return new Coordinates(x + 1, y - 3);
        }
    }

    public FieldView getView() {
        return new FieldView(this);
    }
}

