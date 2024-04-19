package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.deck.card.objectivecard.DiagonalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.SymbolsObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.VerticalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Symbol;
import it.polimi.ingsw.utilities.ParsedCard;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectiveDeck extends Deck<ObjectiveCard> {
    /**
     * Constructor of the objective Deck with Jackson Library from a File,
     * firstly mapping the objects on a class called ParsedCard and then remapping on the specific Card type
     * @throws IOException if the file containing the cards is not opened correctly
     */
    public ObjectiveDeck() throws IOException {
        super();
        ObjectMapper objectMapper = new ObjectMapper();
        //construction of the symbols objective cards
        List<ParsedCard> ObjectiveCards = objectMapper.readValue(new File("src/main/resources/cards/ObjectiveCards.json"), new TypeReference<>() {
        });
        for(ParsedCard s : ObjectiveCards) {
            //Construction differs on the type of objective card intended to build
            switch (s.getType()) {
                case "symbols" -> {
                    Map<Symbol, Integer> symbols = new HashMap<>();
                    //filling the symbols map of the card from json
                    for (String k : s.getSymbols().keySet()) {
                        if (stringToItem.get(k) == null)
                            symbols.put(stringToResource.get(k), s.getSymbols().get(k));
                        else
                            symbols.put(stringToItem.get(k), s.getSymbols().get(k));
                    }
                    SymbolsObjectiveCard card = new SymbolsObjectiveCard(s.getPoints(), s.getId(), "frontImage", "backImage", symbols);
                    cards.add(card);
                }
                case "diagonal" -> {
                    DiagonalPatternObjectiveCard card = new DiagonalPatternObjectiveCard(s.getPoints(), s.getId(), "frontImage", "backImage",
                            stringToResource.get((s.getColor())), s.getMainDiagonal());
                    cards.add(card);
                }
                case "vertical" -> {
                    VerticalPatternObjectiveCard card = new VerticalPatternObjectiveCard(s.getPoints(), s.getId(), "frontImage", "backImage",
                            stringToResource.get(s.getMainColor()), stringToResource.get(s.getThirdCardColor()),
                            stringToPosition.get(s.getThirdCardPos()));
                    cards.add(card);
                }
            }
        }
        Collections.shuffle(cards);

    }
}
