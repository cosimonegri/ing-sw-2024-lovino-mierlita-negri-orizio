package it.polimi.ingsw.modelView.cardView;

import it.polimi.ingsw.model.player.PlacedCard;

public record PlacedCardView(PlayableCardView card, boolean flipped, int placementIndex) {
}
