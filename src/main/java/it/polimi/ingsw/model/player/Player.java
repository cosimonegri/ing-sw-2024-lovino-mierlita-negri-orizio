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
     * Objective cards to choose from
     */
    private List<ObjectiveCard> objOptions;
    /**
     * Starter card of the player
     */
    private PlayableCard starterCard;
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
     */
    public Player(String username, Marker marker) {
        this.username = username;   // TODO: check if username is valid
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
     */
    public void setObjCard(ObjectiveCard objCard) {
        this.objCard = objCard;
    }

    /**
     * Change the player score. It should only increase
     *
     * @param newPoints the points to add (>=0)
     * @throws IllegalArgumentException the points can only increase
     */
    public void increaseScore(int newPoints) {
        // Check if >= 0
        if (newPoints < 0) throw new IllegalArgumentException("NewPoints must be a positive value");
        this.score += newPoints;
    }


    /**
     * Take a new card. Maximum three cards
     *
     * @param newCard the new card to add to the player's hand
     * @throws IllegalStateException the player already has three card
     */
    public void addToHand(PlayableCard newCard) throws IllegalStateException {
        if (newCard == null) return;
        if (hand.size() >= Player.MAX_HAND_SIZE) {
            throw new IllegalStateException("Hand already full");
        }
        this.hand.add(newCard);
    }

    /**
     * Remove a card from the hand
     *
     * @param card the card that is to be removed
     * @throws NoSuchElementException the card is not in the player's hand
     */
    public void removeFromHand(PlayableCard card) throws NoSuchElementException {
        if (card == null) return;
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

    public void setStarterCard(PlayableCard starterCard){ this.starterCard = starterCard; }

    public PlayableCard getStarterCard(){ return this.starterCard; }

    public ObjectiveCard getObjCard() {
        return this.objCard;
    }

    public void setObjOptions(List<ObjectiveCard> objOptions){ this.objOptions = objOptions; }

    public List<ObjectiveCard> getObjOptions(){ return Collections.unmodifiableList(this. objOptions); }

    public int getScore() {
        return this.score;
    }

    public String getUsername() {
        return this.username;
    }

    public Marker getMarker() {
        return this.marker;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player other)) {
            return false;
        }
        return this.username.equals(other.username);
    }
}