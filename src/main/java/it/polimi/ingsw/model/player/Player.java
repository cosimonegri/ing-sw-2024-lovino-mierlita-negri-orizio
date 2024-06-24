package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.modelView.PlayerView;

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
    private Marker marker;
    /**
     * The score of the player
     */
    private int score;
    /**
     * The score obtained from objectives cards
     */
    private int objectiveScore;
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
     */
    public Player(String username) {
        this.username = username;
        this.marker = null;
        this.score = 0;
        this.objectiveScore = 0;
        this.objCard = null;
        this.objOptions = new ArrayList<>();
        this.starterCard = null;
        this.hand = new ArrayList<>();
        this.field = new Field();
        this.isWinner = false;
    }

    public int getScore() {
        return this.score;
    }

    /**
     * Change the player score. It should only increase
     *
     * @param newPoints the points to add
     * @throws IllegalArgumentException when newPoints is <= 0
     */
    public void increaseScore(int newPoints) {
        if (newPoints < 0) throw new IllegalArgumentException("NewPoints must be a positive value");
        this.score += newPoints;
    }

    public int getObjectiveScore() {
        return this.objectiveScore;
    }

    /**
     * Set the score obtained from objectives cards
     *
     * @param newPoints the points to add
     * @throws IllegalArgumentException when newPoints is <= 0
     */
    public void setObjectiveScore(int newPoints) {
        if (newPoints < 0) throw new IllegalArgumentException("NewPoints must be a positive value");
        this.objectiveScore = newPoints;
    }

    //TODO handle this exception
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
     * Remove a card from the hand if present, otherwise do nothing
     *
     * @param card the card that is to be removed
     */
    public void removeFromHand(PlayableCard card) {
        if (card == null || !hand.contains(card)) {
            return;
        }
        this.hand.remove(card);
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

    public void setStarterCard(PlayableCard starterCard) {
        this.starterCard = starterCard;
    }

    public PlayableCard getStarterCard() {
        return this.starterCard;
    }

    /**
     * Set the new private objective for the player
     *
     * @param objCard the player's personal objective card
     */
    public void setObjCard(ObjectiveCard objCard) {
        this.objCard = objCard;
    }

    public ObjectiveCard getObjCard() {
        return this.objCard;
    }

    public void addObjOption(ObjectiveCard objective) {
        this.objOptions.add(objective);
    }

    public List<ObjectiveCard> getObjOptions() {
        return Collections.unmodifiableList(this.objOptions);
    }

    public String getUsername() {
        return this.username;
    }

    public void setMarker(Marker m) {
        this.marker = m;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public PlayerView getView(){
        return new PlayerView(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player other)) {
            return false;
        }
        return this.username.equals(other.username);
    }

}