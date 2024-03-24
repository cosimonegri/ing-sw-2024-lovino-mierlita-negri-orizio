package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class contains all the player info
 */
public class Player {
    /**
     * Maximum number of cards that the player can hold in his hand
     */
    private static final int MAX_HAND_SIZE = 3;

    /**
     * The username chosen by the player
     */
    private final String username;
    /**
     * The marker color associated
     */
    private final Marker marker;
    /**
     * The score of the player
     */
    private int score;
    /**
     * The private objective of the player
     */
    private ObjectiveCard objCard;
    /**
     * The player's hand of cards
     */
    private final List<PlayableCard> hand;
    /**
     * The field of the player where the cards are placed
     */
    private final Field field;
    /**
     * True if the player won the match
     */
    private boolean isWinner;

    /**
     * Create a new player
     *
     * @param username the username chosen by the player
     * @param marker the token assigned/chosen by the player
     * @throws NullPointerException if the username or the marker are null
     */
    public Player(String username, Marker marker) {
        if (username == null) throw new NullPointerException("Cannot create player with null username");
        this.username = username;
        if (marker == null) throw new NullPointerException("The marker cannot be null");
        this.marker = marker;
        this.score = 0;
        this.objCard = null;
        this.hand = new ArrayList<>();
        this.field = new Field();
        this.isWinner = false;
    }

    /**
     * Set the new private objective for the player
     *
     * @param objCard the player's personal objective card
     * @throws NullPointerException if the parameter is null
     */
    public void setObjCard(ObjectiveCard objCard) {
        if (objCard == null) throw new NullPointerException("ObjectiveCard is null");
        this.objCard = objCard;
    }

    /**
     * Change the player score. It should only increase
     *
     * @param newPoints the points to add (>=0)
     * @throws IllegalArgumentException the value can only increase
     */
    public void increaseScore(int newPoints) {
        // Check if >= 0
        if (newPoints < 0) throw new IllegalArgumentException("Invalid newPoints value");
        this.score += newPoints;
    }


    /**
     * Take a new card. Maximum three cards
     *
     * @param newCard the new card to add to the player's hand
     * @throws NullPointerException the new card is a null pointer
     * @throws IllegalStateException the player already has three card
     */
    public void addToHand(PlayableCard newCard) throws IllegalStateException {
        if (newCard == null) throw new NullPointerException("The Card is null");
        if (hand.size() >= Player.MAX_HAND_SIZE) {
            throw new IllegalStateException("Hand already full");
        }
        this.hand.add(newCard);
    }

    /**
     * Remove a card from the hand
     *
     * @param card the card that is to be removed
     * @throws NullPointerException the parameter is null
     * @throws NoSuchElementException the card is not in the player's hand
     */
    public void removeFromHand(PlayableCard card) throws NoSuchElementException {
        if (card == null) throw new NullPointerException("The Card is null");
        if (hand.contains(card)) {
            this.hand.remove(card);
        } else {
            throw new NoSuchElementException("Card not found");
        }
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

    public List<PlayableCard> getHand() {
        return Collections.unmodifiableList(this.hand);
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