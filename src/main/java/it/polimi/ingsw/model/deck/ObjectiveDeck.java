package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.utilities.ParsedCard;
import it.polimi.ingsw.model.deck.card.objectivecard.DiagonalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.SymbolsObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.VerticalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Symbol;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectiveDeck extends Deck<ObjectiveCard> {
    public ObjectiveDeck() throws IOException {
        super();
        ObjectMapper objectMapper = new ObjectMapper();

        List<ParsedCard> symbolsObjectiveCards = objectMapper.readValue(new File("src/main/resources/cards/SymbolsObjectiveCard.json"), new TypeReference<List<ParsedCard>>() {});
        for(ParsedCard s : symbolsObjectiveCards){
        Map<Symbol, Integer> symbols = new HashMap<>();
        //filling the symbols map of the card from json
        for(String k : s.getSymbols().keySet()) {
            if(stringToItem.get(k) == null)
                symbols.put(stringToResource.get(k), s.getSymbols().get(k));
            else
                symbols.put(stringToItem.get(k), s.getSymbols().get(k));
        }
        SymbolsObjectiveCard card = new SymbolsObjectiveCard(s.getPoints(), s.getId(), "frontImage", "backImage", symbols);
        cards.add(card);
        }
        List<ParsedCard>  DPOCCards = objectMapper.readValue(new File("src/main/resources/cards/DPOC.json"), new TypeReference<List<ParsedCard>>() {});
        for(ParsedCard s : DPOCCards){

        DiagonalPatternObjectiveCard card = new DiagonalPatternObjectiveCard(s.getPoints(), s.getId(), "frontImage", "backImage",
                stringToResource.get((s.getColor())), s.getMainDiagonal());
        cards.add(card);
        }
        List<ParsedCard>  VPOCCards = objectMapper.readValue(new File("src/main/resources/cards/VPOC.json"), new TypeReference<List<ParsedCard>>() {});

        for(ParsedCard s : VPOCCards){
            VerticalPatternObjectiveCard card = new VerticalPatternObjectiveCard(s.getPoints(), s.getId(),"frontImage","backImage",
                    stringToResource.get(s.getMainColor()), stringToResource.get(s.getThirdCardColor()),
                    stringToPosition.get(s.getThirdCardPos()));
            cards.add(card);
        }

        Collections.shuffle(cards);

    }
}
