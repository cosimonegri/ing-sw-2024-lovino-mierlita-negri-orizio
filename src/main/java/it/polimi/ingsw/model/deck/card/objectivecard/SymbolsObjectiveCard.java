package it.polimi.ingsw.model.deck.card.objectivecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Symbol;
import it.polimi.ingsw.model.player.Field;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents the objective cards that need some symbols to be completed
 */
public class SymbolsObjectiveCard extends ObjectiveCard {
    /**
     * Symbols needed to complete the objective
     */
    private final Map<Symbol, Integer> symbols;

    /**
     * Constructor of the class
     *
     * @param points the number of points obtained by completing the objective once
     * @param id the card identifier
     * @param frontImage path to front image
     * @param backImage path to back image
     * @param symbols a map of the symbols needed to complete the objective
     * @throws IllegalArgumentException if points is a negative number
     */
    public SymbolsObjectiveCard(
            int points,
            int id,
            String frontImage,
            String backImage,
            Map<Symbol, Integer> symbols
    ) {
        super(points, id, frontImage, backImage);
        this.symbols = new HashMap<>(symbols);
    }

    @Override
    public int getTotalPoints(Field field) {
        // initial value of -1 to handle the first iteration of the loop
        int timesCompleted = -1;
        for (Symbol symbol : this.symbols.keySet()) {
            // the number of times the card would have been completed if it only required that symbol
            int timesSymbolCompleted = field.getSymbolCount(symbol) / this.symbols.get(symbol);
            // update the count at the first iteration or if we find a symbol that has been completed fewer times
            if (timesCompleted == -1 || timesSymbolCompleted < timesCompleted) {
                timesCompleted = timesSymbolCompleted;
            }
        }
        if (timesCompleted <= 0) {
            return 0;
        }
        return timesCompleted * this.getPoints();
    }

    public Map<Symbol, Integer> getSymbols() { return Collections.unmodifiableMap(this.symbols); }
}
