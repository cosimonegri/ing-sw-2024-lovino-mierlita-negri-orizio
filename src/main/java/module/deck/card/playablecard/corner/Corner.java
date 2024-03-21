package module.deck.card.playablecard.corner;

/**
 *
 * @author Orizio Davide
 *
 * This class holds all the information about a single corner
 */
public class Corner {
    private final CornerType type;
    private final Symbol symbol;

    /**
     *
     * @param type: the type of angle: HIDDEN, VISIBLE
     * @param symbol: the symbol, if present, on the corner
     * @throws NullPointerException: the type cant be null
     */
    public Corner(CornerType type, Symbol symbol) throws NullPointerException {
        if (type == null) throw new NullPointerException("CornerType cannot be null");
        this.type = type;
        this.symbol = symbol;
    }

    public CornerType getType() {
        return type;
    }

    public Symbol getSymbol() {
        return symbol;
    }
}
