package it.polimi.ingsw.model;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.CannotCreateGameException;
import it.polimi.ingsw.exceptions.LobbyFullException;
import it.polimi.ingsw.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GameTest {
    private Game game;

    @BeforeEach
    public void gameTest() throws CannotCreateGameException { game = new Game(4); }

//    @Test
//    @DisplayName("Constructor Test")
//    public void constructorTest() {
//        int playerCountMax = 5;
//        for(int playerCount = -1; playerCount <= playerCountMax; playerCount++){
//           int finalI = playerCount;
//           if(playerCount < 2 || playerCount > 4){
//               assertThrows(IllegalArgumentException.class, () -> new Game(finalI));
//           }
//           else { assertDoesNotThrow(() -> new Game(finalI)); }
//        }
//    }


    @Test
    @DisplayName("New Player")
    public void addPlayerTest() {
//        String u1 = "the-player*]0";
//        assertThrows(IllegalArgumentException.class, () -> game.addPlayer(u1, null));
//        String u2 = "   ";
//        assertThrows(IllegalArgumentException.class, () -> game.addPlayer(u2, null));
//        String u3 = "";
//        assertThrows(IllegalArgumentException.class, () -> game.addPlayer(u3, null));

        assertDoesNotThrow(() -> game.addPlayer("one1_", null));
//        assertThrows(UsernameAlreadyTakenException.class, () -> game.addPlayer("one1_", null));
        assertEquals(1, game.getPlayers().size());
        assertDoesNotThrow(() -> game.addPlayer("two2", null));
        assertDoesNotThrow(() -> game.addPlayer("three3", null));
        assertDoesNotThrow(() -> game.addPlayer("four4", null));
//        assertThrows(LobbyFullException.class, () -> game.addPlayer("five5", null));
        assertEquals(4, game.getPlayers().size());

//        Set<Marker> markerSet = new HashSet<>();
//        List<Marker> markerList = new ArrayList<>();
//        for(Player player : game.getPlayers()) {
//            markerSet.add(player.getMarker());
//            markerList.add(player.getMarker());
//        }
//        assertEquals(markerSet.size(), markerList.size());

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
        assertDoesNotThrow(() -> game.addPlayer("one_1", null));
        assertDoesNotThrow(() -> game.addPlayer("two_2", null));
        assertDoesNotThrow(() -> game.addPlayer("three_3", null));
        String u1 = "the-player*]0";
        game.removePlayer(u1);
        String u2 = "   ";
        game.removePlayer(u2);
        String u3 = "";
        game.removePlayer(u3);
        String u4 = "None0";
        game.removePlayer(u4);
        assertEquals(3, game.getPlayers().size());

        game.removePlayer("one_1");
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
        assertDoesNotThrow(() -> game.addPlayer("one", null));
        assertDoesNotThrow(() -> game.addPlayer("two", null));
        assertDoesNotThrow(() -> game.addPlayer("three", null));
        assertDoesNotThrow(() -> game.addPlayer("four", null));

        assertDoesNotThrow(() -> game.start());

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
                assertInstanceOf(PlayableCard.class, player.getStarterCard());
                assertNotNull(player.getStarterCard());
                assertEquals(2, player.getObjOptions().size());
                assertInstanceOf(ObjectiveCard.class, player.getObjOptions().get(0));
                assertInstanceOf(ObjectiveCard.class, player.getObjOptions().get(1));
            }
            assertEquals(1, goldAmount);
            assertEquals(2, resourceAmount);

        }

    }

    @Test
    @DisplayName("Advance turn test")
    public void advanceTurnTest(){
//        assertThrows(GameNotStartedYetException.class, () -> game.advanceTurn());
        assertDoesNotThrow(() -> game.addPlayer("one_1", null));
        assertDoesNotThrow(() -> game.addPlayer("two_2", null));
        assertDoesNotThrow(() -> game.addPlayer("three_3", null));
        assertDoesNotThrow(() -> game.addPlayer("four_4", null));
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
