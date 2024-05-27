package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;

import java.io.Serializable;

/**
 * PlacedCard class that represent a card placed on the filed with its relevant data
 *
 * @param card the card placed on the field
 * @param flipped true if the card's back is visible, false if the card's front is visible
 * @param placementIndex the card with a higher index covers the card with a lower one
 */
public record PlacedCard(PlayableCard card, boolean flipped, int placementIndex) implements Serializable { }
