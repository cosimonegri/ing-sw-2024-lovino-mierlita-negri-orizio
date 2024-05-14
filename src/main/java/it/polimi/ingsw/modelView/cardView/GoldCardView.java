package it.polimi.ingsw.modelView.cardView;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.ItemGoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.SimpleGoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Item;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;

import java.util.Map;

public class GoldCardView extends PlayableCardView {

    private final GoldType type;
    private final Map<Resource, Integer> resourcesNeeded;
    private Item item;
    public GoldCardView(GoldCard card){
        super(card);
        this.resourcesNeeded = card.getResourcesNeeded();
        this.item = null;
        if(card instanceof ItemGoldCard){
            this.type = GoldType.ITEM;
            this.item = ((ItemGoldCard) card).getItem();
        }
        else if(card instanceof SimpleGoldCard){
            this.type = GoldType.SIMPLE;
        }
        else
            this.type = GoldType.CORNER;
    }
    public GoldType getType() {
        return type;
    }
    public Map<Resource, Integer> getResourcesNeeded() {
        return resourcesNeeded;
    }
    public Item getItem() {
        return item;
    }
}
