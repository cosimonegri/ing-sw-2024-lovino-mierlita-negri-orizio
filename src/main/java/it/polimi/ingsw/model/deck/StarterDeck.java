package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.utilities.ParsedCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.CornerType;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StarterDeck extends Deck<PlayableCard>{
    /**
     * Constructor of the starter Deck with Jackson Library from a File,
     * firstly mapping the objects on a class called ParsedCard and then remapping on the specific Card type
     * @throws IOException if the file containing the cards is not opened correctly
     */
    public StarterDeck() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<ParsedCard> parsedStarterCards = objectMapper.readValue(new File("src/main/resources/cards/StarterCards.json"), new TypeReference<>() {});
        for(ParsedCard s : parsedStarterCards) {
            List<Corner> corners = new ArrayList<>();
            //mapping front corners of the starter card
            if (s.getStartTopLeft() == null)
                corners.add(new Corner(CornerType.HIDDEN, null));
            else
                corners.add(new Corner(CornerType.VISIBLE, stringToResource.get(s.getStartTopLeft())));
            if (s.getStartTopRight() == null)
                corners.add(new Corner(CornerType.HIDDEN, null));
            else
                corners.add(new Corner(CornerType.VISIBLE, stringToResource.get(s.getStartTopRight())));
            if (s.getStartBottomLeft() == null)
                corners.add(new Corner(CornerType.HIDDEN, null));
            else
                corners.add(new Corner(CornerType.VISIBLE, stringToResource.get(s.getStartBottomLeft())));
            if (s.getStartBottomRight() == null)
                corners.add(new Corner(CornerType.HIDDEN, null));
            else
                corners.add(new Corner(CornerType.VISIBLE, stringToResource.get(s.getStartBottomRight())));
           //mapping back corners
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

            //Mapping back Resources
            List<Resource> backResources = new ArrayList<>();
            for(String color : s.getColors()){
                backResources.add(stringToResource.get(color));
            }
            //The new card is created and added to the list
            PlayableCard card = new PlayableCard(s.getPoints(), s.getId(), "frontImage", "backImage",
                    corners, backResources);
            cards.add(card);
        }
        Collections.shuffle(cards);
    }
}
