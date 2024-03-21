package module.player;

import module.deck.card.objectivecard.ObjectiveCard;
import module.deck.card.playablecard.PlayableCard;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author Orizio Davide
 *
 * This class containts all the player info
 */
public class Player {
    private final String username;                  // the username chosen by the player
    private final Marker marker;                    //the marker color associated
    private int score;                              // the actual player score
    private ObjectiveCard objCard;                  // the private objective of the player
    private final ArrayList<PlayableCard> hand;     // the player's list of cards
    private final Field field;                      // where the cards are placed
    private boolean isWinner;                       // true if the player won the match

    /**
     * Create a new player
     *
     * @param username: the username chosen by the player
     * @param marker: the token assigned/chosen by the player
     * @throws NullPointerException: if the username or the marker are null
     */
    public Player(String username, Marker marker) throws NullPointerException {
        if (username == null) throw new NullPointerException("Cannot create player with null username");
        this.username = username;
        if (marker == null) throw new NullPointerException("The marker cannot be null");
        this.marker = marker;
        this.score = 0;
        this.objCard = null;
        this.hand = new ArrayList<>(3);
        this.field = new Field();
        this.isWinner = false;
    }

    /**
     * Set the new private objective for the player
     *
     * @param objCard: the player's personal objective card
     * @throws NullPointerException: if the parameter is null
     */
    public void setObjCard(ObjectiveCard objCard) throws NullPointerException {
        if (objCard == null) throw new NullPointerException("ObjectiveCard is null");
        this.objCard = objCard;
    }

    /**
     * Change the player score. It should only increase
     *
     * @param newPoints: the points to add (>=0)
     * @throws IllegalArgumentException: the value can only increase
     */
    public void increaseScore(int newPoints) throws IllegalArgumentException {
        // Check if >= 0
        if (newPoints < 0) throw new IllegalArgumentException("Invalid newPoints value");
        this.score += newPoints;
    }


    /**
     * Take a new card. Maximum three cards
     *
     * @param newCard: the new card to add to the player's hand
     * @throws NullPointerException: the new card is a null pointer
     * @throws IllegalStateException: the player already has three card
     */
    public void addToHand(PlayableCard newCard) throws NullPointerException, IllegalStateException {
        // If the card is null
        if (newCard == null) throw new NullPointerException("The Card is null");
        // Else find the first empty space
        for (PlayableCard c: this.hand) {
            if (c == null) {
                c = newCard;
                return;
            }
        }
        // No empty space found
        throw new IllegalStateException("Hand already full");
    }

    /**
     * Remove a card from the hand
     *
     * @param oldCard: the card that is to be removed
     * @throws NullPointerException: the parameter is null
     * @throws NoSuchElementException: the oldCard is not in the player's hand
     */
    public void removeFromHand(PlayableCard oldCard) throws NullPointerException, NoSuchElementException {
        // If the card is null
        if (oldCard == null) throw new NullPointerException("The Card is null");
        // Search card
        for (PlayableCard c: this.hand) {
            if (c == oldCard) {
                c = null;
                return;
            }
        }
        throw new NoSuchElementException("Card not found");
    }

    public Field getField() {
        return this.field;
    }

    public boolean getIsWinner() {
        return this.isWinner;
    }

    public void setIsWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public ArrayList<PlayableCard> getHand() {
        return this.hand;
    }

    public ObjectiveCard getObjCard() {
        return this.objCard;
    }

    public int getScore() {
        return this.score;
    }

    public String getUsername() {
        return this.username;
    }

    public Marker getMarker() {
        return this.marker;
    }
}