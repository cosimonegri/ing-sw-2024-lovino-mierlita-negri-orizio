package it.polimi.ingsw.utilities;

import it.polimi.ingsw.model.deck.card.objectivecard.DiagonalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.CornerGoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.CornerType;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.deck.card.playablecard.corner.Resource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardsConfigTest {
    private static CardsConfig cardsConfig;

    @BeforeAll
    public static void setupCardsConfig() throws IOException {
        cardsConfig = CardsConfig.getInstance();
    }


    @Test
    public void CardsConfigTestSize() {
        assertEquals(40, cardsConfig.getGoldCards().size());
        assertEquals(40, cardsConfig.getResourceCards().size());
        assertEquals(16, cardsConfig.getObjectiveCards().size());
        assertEquals(6, cardsConfig.getStarterCards().size());

    }

    @Test
    public void correctParsingOfGoldCards() {

        //manually creating GoldCard Id = 66

        List<Corner> cornersTestCard = new ArrayList<>();
        cornersTestCard.add(new Corner(CornerType.HIDDEN, null));

        for(int i = 0; i < 3; i++){
            cornersTestCard.add(new Corner(CornerType.VISIBLE, null));
        }
        for(int i = 0; i < 4; i++){
            cornersTestCard.add(new Corner(CornerType.VISIBLE, null));
        }

        List<Resource> resourcesTest = new ArrayList<>();
        resourcesTest.add(Resource.ANIMAL);

        Map<Resource, Integer> resourcesNeededTest = new HashMap<>();
        resourcesNeededTest.put(FUNGI, 0);
        resourcesNeededTest.put(Resource.PLANT, 1);
        resourcesNeededTest.put(Resource.ANIMAL, 3);
        resourcesNeededTest.put(Resource.INSECT, 0);

        CornerGoldCard testCard = new CornerGoldCard(2, 66, cornersTestCard, resourcesTest, resourcesNeededTest);

        CornerGoldCard card = (CornerGoldCard) cardsConfig.getCard(66);

        assertEquals(testCard.getId(), card.getId());
        assertEquals(testCard.getPoints(), card.getPoints());
        assertEquals(card.getCorners(), testCard.getCorners());
        assertEquals(card.getColor(),testCard.getColor());
        assertEquals(card.getResourcesNeeded(),testCard.getResourcesNeeded());
    }

    @Test
    public void correctParsingOfPlayableCard() {

        //manually creating PlayableCard id = 9
        List<Corner> cornersTestCard = new ArrayList<>();
        cornersTestCard.add(new Corner(CornerType.VISIBLE, FUNGI));
        cornersTestCard.add(new Corner(CornerType.HIDDEN, null));

        for(int i = 0; i < 6; i++){
            cornersTestCard.add(new Corner(CornerType.VISIBLE, null));
        }

        List<Resource> resourcesTest = new ArrayList<>();
        resourcesTest.add(Resource.FUNGI);

        PlayableCard testCard = new PlayableCard(1,9, cornersTestCard, resourcesTest);

        PlayableCard card = (PlayableCard) cardsConfig.getCard(9);
        assertEquals(testCard.getId(), card.getId());
        assertEquals(testCard.getPoints(), card.getPoints());
        assertEquals(card.getCorners(), testCard.getCorners());
        assertEquals(card.getColor(),testCard.getColor());
    }

    @Test
    public void correctParsingOfStarterCards() {

        //manually creating Starter card with id = 84;
        List<Corner> cornersTestCard = new ArrayList<>();
        cornersTestCard.add(new Corner(CornerType.VISIBLE, PLANT));
        cornersTestCard.add(new Corner(CornerType.VISIBLE, INSECT));
        cornersTestCard.add(new Corner(CornerType.VISIBLE, ANIMAL));
        cornersTestCard.add(new Corner(CornerType.VISIBLE, FUNGI));
        for(int i = 0; i < 4; i++){
            cornersTestCard.add(new Corner(CornerType.VISIBLE, null));
        }
        List<Resource> resourcesTest = new ArrayList<>();
        resourcesTest.add(ANIMAL);
        resourcesTest.add(INSECT);

        PlayableCard testCard = new PlayableCard(0,84, cornersTestCard, resourcesTest);

        PlayableCard card = (PlayableCard) cardsConfig.getCard(84);

        assertEquals(testCard.getId(), card.getId());
        assertEquals(testCard.getPoints(), card.getPoints());
        assertEquals(card.getCorners(), testCard.getCorners());
        assertEquals(card.getColor(),testCard.getColor());
    }
    @Test
    public void correctParsingOfObjectiveCards() {

        //manually creating Objective card id = 90

        DiagonalPatternObjectiveCard testCard = new DiagonalPatternObjectiveCard(2, 90, INSECT, false);

        DiagonalPatternObjectiveCard card = (DiagonalPatternObjectiveCard) cardsConfig.getCard(90);

        assertEquals(testCard.getId(), card.getId());
        assertEquals(testCard.getPoints(), card.getPoints());
        assertEquals(testCard.getColor(), card.getColor());
        assertEquals(testCard.getMainDiagonal(), card.getMainDiagonal());
    }
}