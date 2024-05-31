package it.polimi.ingsw.modelView;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Item;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Symbol;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.model.player.Field;
import it.polimi.ingsw.model.player.PlacedCard;

import java.io.Serializable;
import java.util.*;

public class FieldView implements Serializable {
    private final PlacedCard[][] placedCards;
    private final Coordinates topLeftBound;
    private final Coordinates bottomRightBound;
    private final List<Coordinates> allValidCoords = new ArrayList<>();
    private final Map<Symbol, Integer> symbolCount = new HashMap<>();

    public FieldView(Field field){
        this.placedCards = new PlacedCard[field.size()][field.size()];
        for (int x = 0; x < field.size(); x++) {
            for (int y = 0; y < field.size(); y++) {
                Coordinates coords = new Coordinates(x, y);
                this.placedCards[x][y] = field.getPlacedCard(coords);
            }

        }
        this.topLeftBound = field.getTopLeftBound();
        this.bottomRightBound = field.getBottomRightBound();
        this.allValidCoords.addAll(field.getAllValidCoords());
        for(Symbol symbol: Resource.values())
            this.symbolCount.put(symbol, field.getSymbolCount(symbol));
        for(Symbol symbol: Item.values())
            this.symbolCount.put(symbol, field.getSymbolCount(symbol));
    }

    public int size() {
        return placedCards.length;
    }

    public List<Coordinates> getAllValidCoords(){
        return Collections.unmodifiableList(this.allValidCoords);
    }

    /**
     * @param coords coordinates
     * @return whether the coordinates are out of bound
     */
    public boolean areCoordsOutOfBound(Coordinates coords) {
        return coords.x() < 0 || coords.x() >= size() || coords.y() < 0 || coords.y() >= size();
    }

    public PlacedCard getPlacedCard(Coordinates coords) {
        return this.placedCards[coords.x()][coords.y()];
    }

    public int getSymbolCount(Symbol symbol){
        return this.symbolCount.get(symbol);
    }

    public Coordinates getTopLeftBound() {
        return this.topLeftBound;
    }

    public Coordinates getBottomRightBound() {
        return this.bottomRightBound;
    }
}
