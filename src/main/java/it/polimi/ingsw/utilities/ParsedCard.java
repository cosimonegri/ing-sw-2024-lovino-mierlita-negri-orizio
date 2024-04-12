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

        private String startTopLeft;
        private String startTopRight;
        private String startBottomLeft;
        private String startBottomRight;
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

        @JsonCreator
        public ParsedCard(
                @JsonProperty("id") int id,
                @JsonProperty("color") String color,
                @JsonProperty("colors") List<String> colors,
                @JsonProperty("points") int points,
                @JsonProperty("start-top-left") String startTopLeft,
                @JsonProperty("start-top-right") String startTopRight,
                @JsonProperty("start-bottom-left") String startBottomLeft,
                @JsonProperty("start-bottom-right") String startBottomRight,
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
                @JsonProperty("main-diagonal") Boolean mainDiagonal) {
            this.id = id;
            this.color = color;
            this.colors = colors;
            this.points = points;
            this.startTopLeft = startTopLeft;
            this.startTopRight = startTopRight;
            this.startBottomLeft = startBottomLeft;
            this.startBottomRight = startBottomRight;
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

    public String getStartTopLeft() {
        return startTopLeft;
    }

    public void setStartTopLeft(String startTopLeft) {
        this.startTopLeft = startTopLeft;
    }

    public String getStartTopRight() {
        return startTopRight;
    }

    public void setStartTopRight(String startTopRight) {
        this.startTopRight = startTopRight;
    }

    public String getStartBottomLeft() {
        return startBottomLeft;
    }

    public void setStartBottomLeft(String startBottomLeft) {
        this.startBottomLeft = startBottomLeft;
    }

    public String getStartBottomRight() {
        return startBottomRight;
    }

    public void setStartBottomRight(String startBottomRight) {
        this.startBottomRight = startBottomRight;
    }
}