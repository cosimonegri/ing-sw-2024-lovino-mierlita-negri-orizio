package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.utilities.ParsedCard;
import it.polimi.ingsw.model.deck.card.playablecard.CornerGoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.ItemGoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.SimpleGoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GoldDeck extends Deck<GoldCard>{

    /**
     * Constructor of the gold Deck with Jackson Library from a File,
     * firstly mapping the objects on a class called ParsedCard and then remapping on the specific Card type
     * @throws IOException if the file containing the cards is not opened correctly
     */
    public GoldDeck() throws IOException {
        //constructor of deck
        super();
        ObjectMapper objectMapper = new ObjectMapper();
        //The Json file is parsed in a list of ParsedCards
        List<ParsedCard> parsedGoldCards = objectMapper.readValue(new File("src/main/resources/cards/GoldCards.json"), new TypeReference<>() {});
        for(ParsedCard s : parsedGoldCards ) {
            List<Corner> corners = new ArrayList<>();
            Map<Resource, Integer> resourcesNeededMap = new HashMap<>();
            //initialize and construct the corners of the card
            if (s.getTopLeft() == null)
                corners.add(new Corner(CornerType.HIDDEN, null));
            else
                corners.add(new Corner(CornerType.VISIBLE, stringToResource.get(s.getTopLeft())));
            if (s.getTopRight() == null)
                corners.add(new Corner(CornerType.HIDDEN, null));
            else
                corners.add(new Corner(CornerType.VISIBLE, stringToResource.get(s.getTopRight())));
            if (s.getBottomLeft() == null)
                corners.add(new Corner(CornerType.HIDDEN, null));
            else
                corners.add(new Corner(CornerType.VISIBLE, stringToResource.get(s.getBottomLeft())));
            if (s.getBottomRight() == null)
                corners.add(new Corner(CornerType.HIDDEN, null));
            else
                corners.add(new Corner(CornerType.VISIBLE, stringToResource.get(s.getBottomRight())));
            //mapping backCorners of the card
            for (int i = 0; i <= 3; i++) {
                corners.add(new Corner(CornerType.VISIBLE, null));
            }
            //Mapping back Resources
            List<Resource> backResources = new ArrayList<>();
            backResources.add(stringToResource.get(s.getColor()));
            //Mapping ResourcesNeeded
            for (String k : s.getResourcesNeeded().keySet()) {
                resourcesNeededMap.put(stringToResource.get(k), s.getResourcesNeeded().get(k));
            }
            //Construction differs on the type of gold card intended to build (CornerGold, SimpleGold, ItemGold)
            switch (s.getType()) {
                case "corner" -> {
                    CornerGoldCard card = new CornerGoldCard(s.getPoints(), s.getId(), "frontImage", "backImage",
                            corners, backResources, resourcesNeededMap);
                    cards.add(card);
                }
                case "simple" -> {
                    SimpleGoldCard card = new SimpleGoldCard(s.getPoints(), s.getId(), "frontImage", "backImage",
                            corners, backResources, resourcesNeededMap);
                    cards.add(card);
                }
                case "item" -> {
                    ItemGoldCard card = new ItemGoldCard(s.getPoints(), s.getId(), "frontImage", "backImage",
                            corners, backResources, resourcesNeededMap, stringToItem.get(s.getItem()));
                    cards.add(card);
                }

            }
        }
        Collections.shuffle(cards);

    }
}
