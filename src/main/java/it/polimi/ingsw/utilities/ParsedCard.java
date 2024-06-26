package it.polimi.ingsw.utilities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ignores unknown fields while parsing if not present in the java class
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParsedCard {
    private final int id;
    private final String color;

    private final List<String> colors;
    private final int points;

    private final String backTopLeft;
    private final String backTopRight;
    private final String backBottomLeft;
    private final String backBottomRight;
    private final String topLeft;
    private final String topRight;
    private final String bottomLeft;
    private final String bottomRight;
    private final Map<String, Integer> resourcesNeeded;
    private final String type;
    private final String item;
    private final Map<String, Integer> symbols;
    private final String mainColor;
    private final String thirdCardColor;
    private final String thirdCardPos;
    private final Boolean mainDiagonal;

    /**
     * constructor of the class, for parameters not specified, see the attributes of {@link it.polimi.ingsw.model.deck.card.Card}
     * or of its subclasses
     * @param id unique identifier of the card
     * @param color main resource of the card
     * @param colors main resources of the starter cards
     * @param points points of the card
     * @param backTopLeft back top-left corner of starter card
     * @param backTopRight back top-right corner of starter card
     * @param backBottomLeft back bottom-left corner of starter card
     * @param backBottomRight back bottom-right corner of starter card
     * @param topLeft front top-left corner of the card
     * @param topRight front top-right corner of the card
     * @param bottomLeft front bottom-left corner of the card
     * @param bottomRight front bottom-right corner of the card
     * @param resourcesNeeded map of resources needed in order to place the card
     * @param type type of card to differentiate from different cards of the same class (Ex. simpleGold, cornerGold, ItemGold)
     * @param item item in {@link it.polimi.ingsw.model.deck.card.playablecard.ItemGoldCard}
     * @param symbols symbols in {@link it.polimi.ingsw.model.deck.card.objectivecard.SymbolsObjectiveCard}
     * @param mainDiagonal mainDiagonal in {@link it.polimi.ingsw.model.deck.card.objectivecard.DiagonalPatternObjectiveCard}
     * @param mainColor mainColor in {@link it.polimi.ingsw.model.deck.card.objectivecard.VerticalPatternObjectiveCard}
     * @param thirdCardColor thirdCardColor in {@link it.polimi.ingsw.model.deck.card.objectivecard.VerticalPatternObjectiveCard}
     * @param thirdCardPos thirdCardPos in {@link it.polimi.ingsw.model.deck.card.objectivecard.VerticalPatternObjectiveCard}
     */
    @JsonCreator
    public ParsedCard(
            @JsonProperty("id") int id,
            @JsonProperty("color") String color,
            @JsonProperty("colors") List<String> colors,
            @JsonProperty("points") int points,
            @JsonProperty("back-top-left") String backTopLeft,
            @JsonProperty("back-top-right") String backTopRight,
            @JsonProperty("back-bottom-left") String backBottomLeft,
            @JsonProperty("back-bottom-right") String backBottomRight,
            @JsonProperty("top-left") String topLeft,
            @JsonProperty("top-right") String topRight,
            @JsonProperty("bottom-left") String bottomLeft,
            @JsonProperty("bottom-right") String bottomRight,
            @JsonProperty("resourcesNeeded") Map<String, Integer> resourcesNeeded,
            @JsonProperty("type") String type,
            @JsonProperty("item") String item,
            @JsonProperty("symbols") Map<String, Integer> symbols,
            @JsonProperty("main-color") String mainColor,
            @JsonProperty("thirdCard-color") String thirdCardColor,
            @JsonProperty("thirdCard-position") String thirdCardPos,
            @JsonProperty("main-diagonal") Boolean mainDiagonal
    ) {
        this.id = id;
        this.color = color;
        this.colors = colors;
        this.points = points;
        this.backTopLeft = backTopLeft;
        this.backTopRight = backTopRight;
        this.backBottomLeft = backBottomLeft;
        this.backBottomRight = backBottomRight;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.resourcesNeeded = resourcesNeeded;
        this.type = type;
        this.item = item;
        this.mainColor = mainColor;
        this.thirdCardColor  = thirdCardColor;
        this.thirdCardPos = thirdCardPos;
        this.mainDiagonal = mainDiagonal;
        this.symbols = symbols;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    public String getTopLeft() {
        return topLeft;
    }

    public String getTopRight() {
        return topRight;
    }

    public String getBottomLeft() {
        return bottomLeft;
    }

    public String getBottomRight() {
        return bottomRight;
    }

    public Map<String, Integer> getResourcesNeeded() {
        return new HashMap<>(resourcesNeeded);
    }

    public String getType() {
        return type;
    }

    public String getItem() {
        return item;
    }

    public Boolean getMainDiagonal() {
        return mainDiagonal;
    }

    public Map<String, Integer> getSymbols() {
        return new HashMap<>(symbols);
    }

    public String getMainColor() {
        return mainColor;
    }

    public String getThirdCardColor() {
        return thirdCardColor;
    }

    public String getThirdCardPos() {
        return thirdCardPos;
    }

    public List<String> getColors() {
        return new ArrayList<>(colors);
    }

    public String getBackTopLeft() {
        return backTopLeft;
    }

    public String getBackTopRight() {
        return backTopRight;
    }

    public String getBackBottomLeft() {
        return backBottomLeft;
    }

    public String getBackBottomRight() {
        return backBottomRight;
    }
}