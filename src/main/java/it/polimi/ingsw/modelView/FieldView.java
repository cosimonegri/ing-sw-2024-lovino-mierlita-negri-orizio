package it.polimi.ingsw.modelView;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Item;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Symbol;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.model.player.Field;
import it.polimi.ingsw.model.player.PlacedCard;
import it.polimi.ingsw.modelView.cardView.PlacedCardView;
import it.polimi.ingsw.modelView.cardView.PlayableCardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldView implements Serializable {

    private final PlacedCardView[][] placedCards;
    private final List<Coordinates> allValidCords = new ArrayList<>();
    private final Map<Symbol, Integer> symbolCount = new HashMap<>();
    public FieldView(Field field){
        this.placedCards = new PlacedCardView[field.size()][field.size()];
        for(int i = 0; i < field.size(); i++) {
            for (int j = 0; j < field.size(); j++) {
                Coordinates coords = new Coordinates(i,j);
                PlacedCard placedCard = field.getPlacedCard(coords);
                this.placedCards[i][j] = (placedCard== null) ? null : getView(placedCard);
            }

        }
        this.allValidCords.addAll(field.getAllValidCoords());
        for(Symbol symbol: Resource.values())
            this.symbolCount.put(symbol, field.getSymbolCount(symbol));
        for(Symbol symbol: Item.values())
            this.symbolCount.put(symbol, field.getSymbolCount(symbol));
    }

    public List<Coordinates> getAllValidCords(){
        return this.allValidCords;
    }
    public PlacedCardView getPlacedCard(Coordinates coords){
        return this.placedCards[coords.x()][coords.y()];
    }
    public Map<Symbol, Integer> getSymbolCount(){
        return this.symbolCount;
    }

    private PlacedCardView getView(PlacedCard card){
        return new PlacedCardView(card.card().getView(), card.flipped(), card.placementIndex());}

}
