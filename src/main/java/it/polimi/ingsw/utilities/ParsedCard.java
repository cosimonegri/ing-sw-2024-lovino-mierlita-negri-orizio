package it.polimi.ingsw.utilities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true) //ignores unknown fields while parsing if not present in the java class
public class ParsedCard {
    private int id;
    private String color;

    private List<String> colors;
    private int points;

    private String backTopLeft;
    private String backTopRight;
    private String backBottomLeft;
    private String backBottomRight;
    private String topLeft;
    private String topRight;
    private String bottomLeft;
    private String bottomRight;
    private Map<String, Integer> resourcesNeeded;
    private String type;
    private String item;
    private Map<String, Integer> symbols;
    private String mainColor;
    private String thirdCardColor;
    private String thirdCardPos;
    private Boolean mainDiagonal;

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

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(String topLeft) {
        this.topLeft = topLeft;
    }

    public String getTopRight() {
        return topRight;
    }

    public void setTopRight(String topRight) {
        this.topRight = topRight;
    }

    public String getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(String bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public String getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(String bottomRight) {
        this.bottomRight = bottomRight;
    }

    public Map<String, Integer> getResourcesNeeded() {
        return resourcesNeeded;
    }

    public void setResourcesNeeded(Map<String, Integer> resourcesNeeded) {
        this.resourcesNeeded = resourcesNeeded;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Boolean getMainDiagonal() {
        return mainDiagonal;
    }

    public void setMainDiagonal(Boolean mainDiagonal) {
        this.mainDiagonal = mainDiagonal;
    }

    public Map<String, Integer> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, Integer> symbols) {
        this.symbols = symbols;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getThirdCardColor() {
        return thirdCardColor;
    }

    public void setThirdCardColor(String thirdCardColor) {
        this.thirdCardColor = thirdCardColor;
    }

    public String getThirdCardPos() {
        return thirdCardPos;
    }

    public void setThirdCardPos(String thirdCardPos) {
        this.thirdCardPos = thirdCardPos;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getBackTopLeft() {
        return backTopLeft;
    }

    public void setBackTopLeft(String backTopLeft) {
        this.backTopLeft = backTopLeft;
    }

    public String getBackTopRight() {
        return backTopRight;
    }

    public void setBackTopRight(String backTopRight) {
        this.backTopRight = backTopRight;
    }

    public String getBackBottomLeft() {
        return backBottomLeft;
    }

    public void setBackBottomLeft(String backBottomLeft) {
        this.backBottomLeft = backBottomLeft;
    }

    public String getBackBottomRight() {
        return backBottomRight;
    }

    public void setBackBottomRight(String backBottomRight) {
        this.backBottomRight = backBottomRight;
    }
}