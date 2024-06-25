package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.modelView.GameView;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameController {
    private final int gameId;
    private final Game model;

    public GameController(int gameId, int playersCount) throws PlayersCountNotValidException, CannotCreateGameException {
        this.gameId = gameId;
        this.model = new Game(playersCount);
    }

    synchronized public int getId() {
        return this.gameId;
    }

    synchronized public void notifyListener(String username, ServerToClientMessage message) {
        model.notifyListener(username, message);
    }

    synchronized public void notifyAllListeners(ServerToClientMessage message) {
        model.notifyAllListeners(message);
    }

    synchronized public void addPlayer(String username, GameListener listener) throws LobbyFullException {
        if (model.isLobbyFull()) {
            throw new LobbyFullException();
        }
        model.addPlayer(username, listener);
        if (model.isLobbyFull()) {
            model.setGamePhase(GamePhase.STARTER);
            model.giveStarterCards();
        }
    }

    synchronized public void removePlayer(String username) {
        if (model.getGamePhase() == GamePhase.WAITING) {
            model.removePlayer(username);
        } else {
            model.setGamePhase(GamePhase.ENDED);
        }
    }

    synchronized public void chooseMarker(String username, Marker marker) throws MarkerNotValidException, ActionNotValidException {
        Optional<Player> player = model.getPlayer(username);
        if (model.getGamePhase() != GamePhase.STARTER || player.isEmpty() || hasChosenMarker(player.get())) {
            throw new ActionNotValidException();
        }
        for (Player p : model.getPlayers()) {
            if (p.getMarker() == marker) {
                throw new MarkerNotValidException();
            }
        }
        player.get().setMarker(marker);
        if (isStarterPhaseFinished()) {
            model.setGamePhase(GamePhase.OBJECTIVE);
            model.fillPlayerHands();
            model.drawCommonObjectives();
        }
    }

    synchronized public void playStarter(String username, boolean flipped) throws ActionNotValidException {
        Optional<Player> player = model.getPlayer(username);
        if (model.getGamePhase() != GamePhase.STARTER || player.isEmpty() || hasPlayedStarter(player.get())) {
            throw new ActionNotValidException();
        }
        try {
            player.get().getField().addCentralCard(player.get().getStarterCard(), flipped);
        } catch (CoordinatesNotValidException | NotEnoughResourcesException ignored) { }
        if (isStarterPhaseFinished()) {
            model.setGamePhase(GamePhase.OBJECTIVE);
            model.fillPlayerHands();
            model.drawCommonObjectives();
        }
    }

    synchronized public void chooseObjective(String username, ObjectiveCard objective) throws CardNotInHandException, ActionNotValidException {
        Optional<Player> player = model.getPlayer(username);
        if (model.getGamePhase() != GamePhase.OBJECTIVE || player.isEmpty() || hasChosenObjective(player.get())) {
            throw new ActionNotValidException();
        }
        if (!player.get().getObjOptions().contains(objective)) {
            throw new CardNotInHandException();
        }
        player.get().setObjCard(objective);
        if (isObjectivePhaseFinished()) {
            model.setGamePhase(GamePhase.MAIN);
            model.setTurnPhase(TurnPhase.PLAY);
            model.start();
        }
    }

    synchronized public void playCard(String username, PlayableCard card, boolean flipped, Coordinates coords)
            throws CardNotInHandException, CoordinatesNotValidException, NotEnoughResourcesException, ActionNotValidException
    {
        if (model.getGamePhase() != GamePhase.MAIN || model.getTurnPhase() != TurnPhase.PLAY || !isCurrentPlayer(username)) {
            throw new ActionNotValidException();
        }
        // check this even though removeFromHand would raise the same exception, so that the following 2 lines are atomic
        if (!model.getCurrentPlayer().getHand().contains(card)) {
            throw new CardNotInHandException();
        }
        model.getCurrentPlayer().getField().addCard(card, flipped, coords);
        model.getCurrentPlayer().removeFromHand(card);
        if (!flipped) {
            model.getCurrentPlayer().increaseScore(card instanceof GoldCard c
                    ? c.getTotalPoints(model.getCurrentPlayer().getField())
                    : card.getPoints()
            );
        }
        if (model.isLastRound()) {
            model.advanceTurn();
            if (model.getRemainingTurns().isPresent() && model.getRemainingTurns().get() == 0) {
                model.setGamePhase(GamePhase.ENDED);
                model.calculateObjectivePoints();
            }
        } else {
            model.setTurnPhase(TurnPhase.DRAW);
        }
    }

    synchronized public void drawCard(String username, DrawType type, PlayableCard card)
            throws EmptyDeckException, CardNotOnBoardException, ActionNotValidException
    {
        if (model.getGamePhase() != GamePhase.MAIN || model.getTurnPhase() != TurnPhase.DRAW || !isCurrentPlayer(username)) {
            throw new ActionNotValidException();
        }
        switch (type) {
            case RESOURCE:
                if (model.getBoard().getResourceDeck().isEmpty()) {
                    throw new EmptyDeckException();
                }
                model.getCurrentPlayer().addToHand(model.getBoard().getResourceDeck().draw());
                break;
            case GOLD:
                if (model.getBoard().getGoldDeck().isEmpty()) {
                    throw new EmptyDeckException();
                }
                model.getCurrentPlayer().addToHand(model.getBoard().getGoldDeck().draw());
                break;
            default:
                if (card == null || !Arrays.asList(model.getBoard().getVisibleCards()).contains(card)) {
                    throw new CardNotOnBoardException();
                }
                model.getCurrentPlayer().addToHand(card);
                model.getBoard().replaceVisibleCard(card);
                break;
        }
        model.advanceTurn();
        model.setTurnPhase(TurnPhase.PLAY);
    }

    synchronized public int getPlayersCount() {
        return model.getPlayersCount();
    }

    synchronized public List<Player> getPlayers() {
        return model.getPlayers();
    }

    synchronized public Optional<Integer> getRemainingTurns() {
        return model.getRemainingTurns();
    }

    synchronized public GamePhase getPhase() {
        return this.model.getGamePhase();
    }

    synchronized public boolean isCurrentPlayer(String username) {
        if (model.getCurrentPlayer() == null) {
            return false;
        }
        return model.getCurrentPlayer().getUsername().equals(username);
    }

    private boolean isStarterPhaseFinished() {
        for (Player player : model.getPlayers()) {
            if (!(hasChosenMarker(player) && hasPlayedStarter(player))) {
                return false;
            }
        }
        return true;
    }

    private boolean isObjectivePhaseFinished() {
        for (Player player : model.getPlayers()) {
            if (!hasChosenObjective(player)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasChosenMarker(Player player) {
        return player.getMarker() != null;
    }

    private boolean hasPlayedStarter(Player player) {
        return player.getField().getCardsCount() > 0;
    }

    private boolean hasChosenObjective(Player player) {
        return player.getObjCard() != null;
    }

    synchronized public GameView getModelView() {
        return model.getView();
    }
}
