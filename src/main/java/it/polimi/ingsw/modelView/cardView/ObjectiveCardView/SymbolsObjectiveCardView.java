package it.polimi.ingsw.modelView.cardView.ObjectiveCardView;

import it.polimi.ingsw.model.deck.card.objectivecard.SymbolsObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Symbol;

import java.util.Map;

public class SymbolsObjectiveCardView extends ObjectiveCardView{
    private Map<Symbol, Integer> symbols;
    public SymbolsObjectiveCardView (SymbolsObjectiveCard card){
        super(card);
        this.symbols.putAll(card.getSymbols());
    }
}
