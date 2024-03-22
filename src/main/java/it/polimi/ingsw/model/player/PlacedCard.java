package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;

/**
 * PlacedCard class that represent a card placed on the filed with its relevant data
 *
 * @param card the card placed on the field
 * @param flipped true if the card is placed on its back, false if it is placed on its front
 * @param placementIndex the card with a higher index covers the card with a lower one
 */
public record PlacedCard(PlayableCard card, boolean flipped, int placementIndex) {}
