package it.polimi.ingsw.modelView.cardView;

import it.polimi.ingsw.model.player.PlacedCard;

import java.io.Serializable;

public record PlacedCardView(PlayableCardView card, boolean flipped, int placementIndex) implements Serializable {
}
