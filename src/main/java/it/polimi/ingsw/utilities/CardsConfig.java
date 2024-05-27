package it.polimi.ingsw.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.deck.card.objectivecard.DiagonalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.SymbolsObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.VerticalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.*;
import it.polimi.ingsw.model.deck.card.playablecard.corner.*;
import static it.polimi.ingsw.model.deck.card.playablecard.corner.Resource.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class to read the cards from JSON files
 */
public class CardsConfig {
    private static final int FIRST_CARD_ID = 1;
    private static final int FIRST_GOLD_ID = 41;
    private static final int FIRST_STARTER_ID = 81;
    private static final int FIRST_OBJECTIVE_ID = 87;
    private static final int LAST_CARD_ID = 102;

    private static final String GOLD_PATH = "src/main/resources/cards/GoldCards.json";
    private static final String RESOURCE_PATH = "src/main/resources/cards/ResourceCards.json";
    private static final String OBJECTIVE_PATH = "src/main/resources/cards/ObjectiveCards.json";
    private static final String STARTER_PATH = "src/main/resources/cards/StarterCards.json";

    private static CardsConfig instance;
    private final List<GoldCard> goldCards;
    private final List<PlayableCard> resourceCards;
    private final List<ObjectiveCard> objectiveCards;
    private final List<PlayableCard> starterCards;

    /**
     * Map string attribute of the JSON into a Resource
     */
    private final Map<String, Resource> stringToResource = new HashMap<>();
    /**
     * Map string attribute of the JSON into an Item
     */
    private final Map<String, Item> stringToItem = new HashMap<>();
    /**
     * Map string attribute of the JSON into a Position
     */
    private final Map<String, Position> stringToPosition = new HashMap<>();

    /**
     * Constructor of the class
     * @throws IOException when the file cannot be opened
     */
    private CardsConfig() throws IOException {
        //TODO maybe move string maps to the relative enumeration
        goldCards = new ArrayList<>();
        resourceCards = new ArrayList<>();
        objectiveCards = new ArrayList<>();
        starterCards = new ArrayList<>();
        stringToResource.put("fungi", FUNGI);
        stringToResource.put("animal", ANIMAL);
        stringToResource.put("plant", PLANT);
        stringToResource.put("insect", INSECT);
        stringToItem.put("inkwell", Item.INKWELL);
        stringToItem.put("manuscript", Item.MANUSCRIPT);
        stringToItem.put("quill", Item.QUILL);
        stringToPosition.put("top-left", Position.TOPLEFT);
        stringToPosition.put("top-right", Position.TOPRIGHT);
        stringToPosition.put("bottom-left", Position.BOTTOMLEFT);
        stringToPosition.put("bottom-right", Position.BOTTOMRIGHT);
        parseGoldCards();
        parseResourceCards();
        parseObjectiveCards();
        parseStarterCards();
    }

    /**
     * @return the instance of the singleton class
     * @throws IOException when the file cannot be opened
     */
    public static CardsConfig getInstance() throws IOException {
        if (instance == null) {
            instance = new CardsConfig();
        }
        return instance;
    }

    /**
     * @return ordered list of all the gold cards
     */
    public List<GoldCard> getGoldCards() {
        return new ArrayList<>(this.goldCards);
    }

    /**
     * @return ordered list of all the resource cards
     */
    public List<PlayableCard> getResourceCards() {
        return new ArrayList<>(this.resourceCards);
    }

    /**
     * @return ordered list of all the objective cards
     */
    public List<ObjectiveCard> getObjectiveCards() {
        return new ArrayList<>(this.objectiveCards);
    }

    /**
     * @return ordered list of all the starter cards
     */
    public List<PlayableCard> getStarterCards() {
        return new ArrayList<>(this.starterCards);
    }

    /**
     * @param id id of the card
     * @return the card whose id is requested
     */
    public Card getCard(int id) {
        if (id < FIRST_CARD_ID || id > LAST_CARD_ID) {
            throw new IllegalArgumentException("The card id should be included between 1 and 102");
        }
        if (id >= FIRST_OBJECTIVE_ID) {
            return this.objectiveCards.get(id - FIRST_OBJECTIVE_ID);
        }
        if (id >= FIRST_STARTER_ID) {
            return this.starterCards.get(id - FIRST_STARTER_ID);
        }
        if (id >= FIRST_GOLD_ID) {
            return this.goldCards.get(id - FIRST_GOLD_ID);
        }
        return this.resourceCards.get(id - FIRST_CARD_ID);
    }

    /**
     * Read JSON file and build list of all the gold cards
     *
     * @throws IOException when the file fails to open
     */
    private void parseGoldCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //The Json file is parsed in a list of ParsedCards
        List<ParsedCard> parsedGoldCards = objectMapper.readValue(new File(GOLD_PATH), new TypeReference<>() {});
        for (ParsedCard parsedCard : parsedGoldCards ) {
            //Mapping back resources
            List<Resource> backResources = new ArrayList<>();
            backResources.add(stringToResource.get(parsedCard.getColor()));

            //Mapping resources needed
            Map<Resource, Integer> resourcesNeeded = new HashMap<>();
            for (String k : parsedCard.getResourcesNeeded().keySet()) {
                resourcesNeeded.put(stringToResource.get(k), parsedCard.getResourcesNeeded().get(k));
            }

            //Construction differs on the type of gold card intended to build (CornerGold, SimpleGold, ItemGold)
            switch (parsedCard.getType()) {
                case "corner" -> {
                    CornerGoldCard card = new CornerGoldCard(parsedCard.getPoints(), parsedCard.getId(), null, null,
                            buildCorners(parsedCard), backResources, resourcesNeeded
                    );
                    goldCards.add(card);
                }
                case "simple" -> {
                    SimpleGoldCard card = new SimpleGoldCard(parsedCard.getPoints(), parsedCard.getId(), null, null,
                            buildCorners(parsedCard), backResources, resourcesNeeded
                    );
                    goldCards.add(card);
                }
                case "item" -> {
                    ItemGoldCard card = new ItemGoldCard(parsedCard.getPoints(), parsedCard.getId(), null, null,
                            buildCorners(parsedCard), backResources, resourcesNeeded, stringToItem.get(parsedCard.getItem())
                    );
                    goldCards.add(card);
                }
            }
        }
    }

    /**
     * Read JSON file and build list of all the resource cards
     *
     * @throws IOException when the file fails to open
     */
    private void parseResourceCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //The Json file is parsed in a list of ParsedCards
        List<ParsedCard> parsedResourceCards = objectMapper.readValue(new File(RESOURCE_PATH), new TypeReference<>() {});
        for (ParsedCard parsedCard : parsedResourceCards){
            //Mapping back resources
            List<Resource> backResources = new ArrayList<>();
            backResources.add(stringToResource.get(parsedCard.getColor()));

            //The resource card is created and added to the list
            PlayableCard card = new PlayableCard(parsedCard.getPoints(), parsedCard.getId(), null, null,
                    buildCorners(parsedCard), backResources
            );
            resourceCards.add(card);
        }
    }

    /**
     * Read JSON file and build list of all the objective cards
     *
     * @throws IOException when the file fails to open
     */
    private void parseObjectiveCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //The Json file is parsed in a list of ParsedCards
        List<ParsedCard> ObjectiveCards = objectMapper.readValue(new File(OBJECTIVE_PATH), new TypeReference<>() {});
        for (ParsedCard parsedCard : ObjectiveCards) {

            //Construction differs on the type of objective card intended to build
            switch (parsedCard.getType()) {
                case "symbols" -> {
                    //filling the symbols map of the card
                    Map<Symbol, Integer> symbols = new HashMap<>();
                    for (String k : parsedCard.getSymbols().keySet()) {
                        if (stringToResource.containsKey(k))
                            symbols.put(stringToResource.get(k), parsedCard.getSymbols().get(k));
                        else
                            symbols.put(stringToItem.get(k), parsedCard.getSymbols().get(k));
                    }
                    SymbolsObjectiveCard card = new SymbolsObjectiveCard(parsedCard.getPoints(), parsedCard.getId(),
                            null, null, symbols
                    );
                    objectiveCards.add(card);
                }
                case "diagonal" -> {
                    DiagonalPatternObjectiveCard card = new DiagonalPatternObjectiveCard(parsedCard.getPoints(), parsedCard.getId(),
                            null, null, stringToResource.get((parsedCard.getColor())), parsedCard.getMainDiagonal()
                    );
                    objectiveCards.add(card);
                }
                case "vertical" -> {
                    VerticalPatternObjectiveCard card = new VerticalPatternObjectiveCard(parsedCard.getPoints(), parsedCard.getId(),
                            null, null, stringToResource.get(parsedCard.getMainColor()),
                            stringToResource.get(parsedCard.getThirdCardColor()), stringToPosition.get(parsedCard.getThirdCardPos())
                    );
                    objectiveCards.add(card);
                }
            }
        }
    }

    /**
     * Read JSON file and build list of all the starter cards
     *
     * @throws IOException when the file fails to open
     */
    private void parseStarterCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //The Json file is parsed in a list of ParsedCards
        List<ParsedCard> parsedStarterCards = objectMapper.readValue(new File(STARTER_PATH), new TypeReference<>() {});
        for(ParsedCard parsedCard : parsedStarterCards) {
            //mapping frontCorners
            List<Corner> corners = buildFrontCorners(parsedCard);

            //mapping backCorners (only starter cards have different backCorners)
            corners.add(buildCorner(parsedCard.getBackTopLeft()));
            corners.add(buildCorner(parsedCard.getBackTopRight()));
            corners.add(buildCorner(parsedCard.getBackBottomLeft()));
            corners.add(buildCorner(parsedCard.getBackBottomRight()));

            //Mapping back Resources
            List<Resource> backResources = new ArrayList<>();
            for(String color : parsedCard.getColors()){
                backResources.add(stringToResource.get(color));
            }

            //The new card is created and added to the list
            PlayableCard card = new PlayableCard(parsedCard.getPoints(), parsedCard.getId(), null, null,
                    corners, backResources
            );
            starterCards.add(card);
        }
    }

    /**
     * @param card a parsed card
     * @return list of front corners of the parsed card
     */
    private List<Corner> buildFrontCorners(ParsedCard card) {
        List<Corner> corners = new ArrayList<>();
        corners.add(buildCorner(card.getTopLeft()));
        corners.add(buildCorner(card.getTopRight()));
        corners.add(buildCorner(card.getBottomLeft()));
        corners.add(buildCorner(card.getBottomRight()));
        return corners;
    }

    /**
     * Build all the corners for the resource and gold cards
     *
     * @param card a parsed card
     * @return list of all corners of the parsed card
     */
    private List<Corner> buildCorners(ParsedCard card) {
        List<Corner> corners = buildFrontCorners(card);
        // the back corners of resource and gold cards are always visible and empty
        for (int i = 0; i <= 3; i++) {
            corners.add(new Corner(CornerType.VISIBLE, null));
        }
        return corners;
    }

    /**
     * @param symbolString textual representation of a symbol
     * @return corner containing the given symbol
     */
    private Corner buildCorner(String symbolString) {
        if (symbolString == null) {
            return new Corner(CornerType.HIDDEN, null);
        }
        Symbol symbol = stringToResource.containsKey(symbolString)
                ? stringToResource.get(symbolString)
                : stringToItem.get(symbolString);
        return new Corner(CornerType.VISIBLE, symbol);
    }
}

