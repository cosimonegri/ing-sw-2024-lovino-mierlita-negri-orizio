package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Item;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.model.player.Field;

import java.util.List;
import java.util.Map;

public class CornerGoldCard extends GoldCard {
    public CornerGoldCard(int points,
                        String frontImage,
                        String backImage,
                        List<Corner> corners,
                        List<Resource> backResources,
                        Map<Resource, Integer> resourcesNeeded){super(points, frontImage, backImage, corners, backResources, resourcesNeeded);}

    @Override
    public int getTotalPoints(Field field) {
        
    }
}
