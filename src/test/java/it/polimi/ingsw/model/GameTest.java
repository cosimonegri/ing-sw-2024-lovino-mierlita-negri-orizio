package it.polimi.ingsw.model;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class GameTest {
    private Game game;

    @BeforeEach
    public void gameTest() { game = new Game(4); }

    @Test
    @DisplayName("Constructor Test")
    public void constructorTest() {
       int playerCountMax = 5;
       for(int playerCount = -1; playerCount <= playerCountMax; playerCount++){
           int finalI = playerCount;
           if(playerCount < 2 || playerCount > 4){
               assertThrows(IllegalArgumentException.class, () -> new Game(finalI));
           }
           else { assertDoesNotThrow(() -> new Game(finalI)); }
       }
    }


    @Test
    @DisplayName("New Player")
    public void addPlayerTest() {
        String u1 = "the-player*]0";
        assertThrows(IllegalArgumentException.class, () -> game.addPlayer(u1, Marker.GREEN));
        String u2 = "   ";
        assertThrows(IllegalArgumentException.class, () -> game.addPlayer(u2, Marker.GREEN));
        String u3 = "";
        assertThrows(IllegalArgumentException.class, () -> game.addPlayer(u3, Marker.GREEN));

        assertDoesNotThrow(() -> game.addPlayer("one1_", Marker.GREEN));
        assertThrows(UsernameAlreadyTakenException.class, () -> game.addPlayer("one1_", Marker.RED));
        assertThrows(MarkerAlreadyTakenException.class, () -> game.addPlayer("two2", Marker.GREEN));
        assertEquals(1, game.getPlayers().size());
        assertDoesNotThrow(() -> game.addPlayer("two2", Marker.RED));
        assertDoesNotThrow(() -> game.addPlayer("three3", Marker.BLUE));
        assertDoesNotThrow(() -> game.addPlayer("four4", Marker.YELLOW));
        assertThrows(LobbyFullException.class, () -> game.addPlayer("five5", Marker.GREEN));
        assertEquals(4, game.getPlayers().size());

        List<String> names = new ArrayList<>();
        names.add("one1_");
        names.add("two2");
        names.add("three3");
        names.add("four4");

        for(Player player : game.getPlayers()){
            assertTrue(names.contains(player.getUsername()));
        }
    }

    @Test
    @DisplayName("Remove Player")
    public void removePlayerTest() {
        assertThrows(NonExistentUsernameException.class, () -> game.removePlayer("void"));
        assertDoesNotThrow(() -> game.addPlayer("one_1", Marker.GREEN));
        assertDoesNotThrow(() -> game.addPlayer("two_2", Marker.RED));
        assertDoesNotThrow(() -> game.addPlayer("three_3", Marker.BLUE));
        String u1 = "the-player*]0";
        assertThrows(IllegalArgumentException.class, () -> game.removePlayer(u1));
        String u2 = "   ";
        assertThrows(IllegalArgumentException.class, () -> game.removePlayer(u2));
        String u3 = "";
        assertThrows(IllegalArgumentException.class, () -> game.removePlayer(u3));
        String u4 = "None0";
        assertThrows(NonExistentUsernameException.class, () -> game.removePlayer(u4));
        assertEquals(3, game.getPlayers().size());

        assertDoesNotThrow(() -> game.removePlayer("one_1"));
        assertEquals(2, game.getPlayers().size());

        List<String> names = new ArrayList<>();
        names.add("two_2");
        names.add("three_3");

        for(Player player : game.getPlayers()) {
            assertTrue(names.contains(player.getUsername()));
            assertNotEquals("one_1", player.getUsername());
        }
    }

    @Test
    @DisplayName("Start Test")
    public void StartTest(){
        assertDoesNotThrow(() -> game.addPlayer("one", Marker.GREEN));
        assertThrows(StillWaitingPlayersException.class, () -> game.start());

        assertDoesNotThrow(() -> game.addPlayer("two", Marker.RED));
        assertDoesNotThrow(() -> game.addPlayer("three", Marker.BLUE));
        assertDoesNotThrow(() -> game.addPlayer("four", Marker.YELLOW));

        assertDoesNotThrow(() -> game.start());

        assertThrows(GameAlreadyStartedException.class, () -> game.start());
        assertEquals(2, game.getObjectives().size());

        for(ObjectiveCard objectiveCard : game.getObjectives())
        {
            assertInstanceOf(ObjectiveCard.class, objectiveCard);
        }
        assertEquals(1, game.getCurrentTurn());
        for(Player player : game.getPlayers()) {
            int resourceAmount = 0, goldAmount = 0;
            for (PlayableCard cardInHand : player.getHand()) {
                if(cardInHand instanceof GoldCard) { goldAmount++; }
                else{ resourceAmount++; }
            }
            assertEquals(1, goldAmount);
            assertEquals(2, resourceAmount);
        }

    }

    @Test
    @DisplayName("Advance turn test")
    public void advanceTurnTest(){
        assertThrows(GameNotStartedYetException.class, () -> game.advanceTurn());
        assertDoesNotThrow(() -> game.addPlayer("one_1", Marker.GREEN));
        assertDoesNotThrow(() -> game.addPlayer("two_2", Marker.RED));
        assertDoesNotThrow(() -> game.addPlayer("three_3", Marker.BLUE));
        assertDoesNotThrow(() -> game.addPlayer("four_4", Marker.YELLOW));
        assertDoesNotThrow(() -> game.start());

        assertEquals(game.getPlayers().getFirst(), game.getCurrentPlayer());

        assertDoesNotThrow(() -> game.advanceTurn()); // 1st to 2nd
        assertEquals(game.getPlayers().get(1), game.getCurrentPlayer());

        assertDoesNotThrow(() -> game.advanceTurn());
        assertDoesNotThrow(() -> game.advanceTurn());
        assertDoesNotThrow(() -> game.advanceTurn());// 4th to 1st
        assertEquals(game.getPlayers().getFirst(), game.getCurrentPlayer());
    }
}
