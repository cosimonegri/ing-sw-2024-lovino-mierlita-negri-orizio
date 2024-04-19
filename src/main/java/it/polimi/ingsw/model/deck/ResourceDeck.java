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

public class ResourceDeck extends Deck<PlayableCard> {
    /**
     * Constructor of the resource Deck with Jackson Library from a File,
     * firstly mapping the objects on a class called ParsedCard and then remapping on the specific Card type
     * @throws IOException if the file containing the cards is not opened correctly
     */
    public ResourceDeck() throws IOException {
        //constructor of deck
        super();
        ObjectMapper objectMapper = new ObjectMapper();
        //The Json file is parsed in a list of ParsedCards
        List<ParsedCard> parsedResourceCards = objectMapper.readValue(new File("src/main/resources/cards/ResourceCards.json"), new TypeReference<>() {
        });
        for(ParsedCard s : parsedResourceCards){
            //initialize and construct the corners of the card
            List<Corner> corners = new ArrayList<>();
            if(s.getTopLeft() == null)
                corners.add(new Corner(CornerType.HIDDEN,null));
            else
                corners.add(new Corner(CornerType.VISIBLE,stringToResource.get(s.getTopLeft())));
            if(s.getTopRight() == null)
                corners.add(new Corner(CornerType.HIDDEN,null));
            else
                corners.add(new Corner(CornerType.VISIBLE,stringToResource.get(s.getTopRight())));
            if(s.getBottomLeft() == null)
                corners.add(new Corner(CornerType.HIDDEN,null));
            else
                corners.add(new Corner(CornerType.VISIBLE,stringToResource.get(s.getBottomLeft())));
            if(s.getBottomRight() == null)
                corners.add(new Corner(CornerType.HIDDEN,null));
            else
                corners.add(new Corner(CornerType.VISIBLE,stringToResource.get(s.getBottomRight())));
            //mapping backCorners of the card
            for(int i = 0; i<=3; i++) {
                corners.add(new Corner(CornerType.VISIBLE, null));
            }
            //Mapping back Resources
            List<Resource> backResources = new ArrayList<>();
            backResources.add(stringToResource.get(s.getColor()));
            //The new card is created and added to the list
            PlayableCard card = new PlayableCard(s.getPoints(), s.getId(), "frontImage", "backImage",
                    corners,backResources);
            cards.add(card);
        }
        Collections.shuffle(cards);
}}
