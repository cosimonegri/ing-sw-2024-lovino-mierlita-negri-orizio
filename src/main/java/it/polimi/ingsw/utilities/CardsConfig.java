package it.polimi.ingsw.utilities;

import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;

import java.util.ArrayList;
import java.util.List;

public class CardsConfig {
    private static int FIRST_CARD = 1;
    private static int FIRST_RESOURCE = 41;
    private static int FIRST_OBJECTIVE = 81;
    private static int FIRST_STARTER = 97;
    private static int LAST_CARD = 102;

    private static CardsConfig instance;
    private List<GoldCard> goldCards;
    private List<PlayableCard> resourceCards;
    private List<ObjectiveCard> objectiveCards;
    private List<PlayableCard> starterCards;

    private CardsConfig() {
        goldCards = new ArrayList<>();
        resourceCards = new ArrayList<>();
        objectiveCards = new ArrayList<>();
        starterCards = new ArrayList<>();
        parseGoldCards();
        parseResourceCards();
        parseObjectiveCards();
        parseStarterCards();
    }

    public static CardsConfig getInstance() {
        if (instance == null) {
            instance = new CardsConfig();
        }
        return instance;
    }

    public List<PlayableCard> getResourceCards() {
        return new ArrayList<>(this.resourceCards);
    }

    public List<GoldCard> getGoldCards() {
        return new ArrayList<>(this.goldCards);
    }

    public List<ObjectiveCard> getObjectiveCards() {
        return new ArrayList<>(this.objectiveCards);
    }

    public List<PlayableCard> getStarterCards() {
        return new ArrayList<>(this.starterCards);
    }

    public Card getCard(int id) {
        if (id < FIRST_CARD || id > LAST_CARD) {
            throw new IllegalArgumentException("The card id should be included between 1 and 102");
        }
        if (id >= FIRST_STARTER) {
            return this.starterCards.get(id - FIRST_STARTER);
        }
        if (id >= FIRST_OBJECTIVE) {
            return this.objectiveCards.get(id - FIRST_OBJECTIVE);
        }
        if (id >= FIRST_RESOURCE) {
            return this.resourceCards.get(id - FIRST_RESOURCE);
        }
        return this.goldCards.get(id - FIRST_CARD);
    }

    private void parseGoldCards() {

    }

    private void parseResourceCards(){

    }

    private void parseObjectiveCards(){

    }

    private void parseStarterCards(){

    }
}
