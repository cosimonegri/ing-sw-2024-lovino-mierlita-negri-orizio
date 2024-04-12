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
    public ResourceDeck() throws IOException {
        super();
        ObjectMapper objectMapper = new ObjectMapper();

        List<ParsedCard> parsedResourceCards = objectMapper.readValue(new File("src/main/resources/cards/ResourceCards.json"), new TypeReference<List<ParsedCard>>() {});
        for(ParsedCard s : parsedResourceCards){
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
            PlayableCard card = new PlayableCard(s.getPoints(), s.getId(), "frontImage", "backImage",
                    corners,backResources);
            cards.add(card);
        }
        Collections.shuffle(cards);
}}
