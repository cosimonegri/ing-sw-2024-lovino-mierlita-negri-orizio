package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CannotCreateGameException;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.modelView.GameView;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.utilities.Config;

import java.io.IOException;
import java.util.*;

/**
 * Class to represent a game of 2 to 4 players.
 */
public class Game {
    /**
     * Number of players for the lobby (not actual players in game), min of 2 and max of 4
     */
    private final int playersCount;
    /**
     * List of players that joined the lobby
     */
    private final List<Player> players;
    /**
     * Listeners of the game
     */
    private final Map<Player, GameListener> playerToListener;
    /**
     * Reference to the player whose turn it is.
     */
    private Player currentPlayer;
    /**
     * Reference to the current game's board
     */
    private final Board board;
    /**
     * Incremental number representing the current turn
     */
    private int currentTurn;
    /**
     * Number representing the last turn of the game. Null if it is not already defined
     */
    private Optional<Integer> lastTurn;
    /**
     * List of the two public objectives
     */
    private final List<ObjectiveCard> objectives;
    /**
     * Current game phase of the game
     */
    private GamePhase gamePhase;
    /**
     * Current turn phase of the round
     */
    private TurnPhase turnPhase;

    //todo handle illlegal players count in controller
    /**
     * Constructor of the class
     *
     * @param playersCount the number of players wanted for the match
     */
    public Game(int playersCount) throws CannotCreateGameException {
        this.playersCount = playersCount;

        this.players = new ArrayList<>(playersCount);
        this.playerToListener = new HashMap<>(playersCount);
        this.currentPlayer = null;

        this.currentTurn = 0;
        this.lastTurn = Optional.empty();
        this.gamePhase = GamePhase.WAITING;

        this.objectives = new ArrayList<>(2);

        try {
            this.board = new Board();
        } catch (IOException e) {
            throw new CannotCreateGameException();
        }
    }

    /**
     * Add a player to the game
     *
     * @param username username of the player
     * @param listener game listener of the player
     */
    public void addPlayer(String username, GameListener listener) {
        Player player = new Player(username);
        this.players.add(player);
        this.playerToListener.put(player, listener);
    }

    /**
     * @param username username of a player
     * @return the player with that username if it exists, otherwise null
     */
    public Player getPlayer(String username) {
        for (Player p : this.players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Removes a player and the correspondent listener from the game
     *
     * @param username of the player to be removed
     */
    public void removePlayer(String username) {
        for (Player p : this.players) {
            if (p.getUsername().equals(username)){
                this.players.remove(p);
                this.playerToListener.remove(p);
                return;
            }
        }
    }

    public void notifyListener(String username, ServerToClientMessage message) {
        for (Player p : this.players) {
            if (p.getUsername().equals(username)) {
                this.playerToListener.get(p).updateFromModel(message);
            }
        }
    }

    public void notifyAllListenersExcept(String username, ServerToClientMessage message) {
        for (Player p : this.players) {
            if (p.getUsername().equals(username)) {
                continue;
            }
            this.playerToListener.get(p).updateFromModel(message);
        }
    }

    public void notifyAllListeners(ServerToClientMessage message) {
        for (GameListener listener : this.playerToListener.values()) {
            listener.updateFromModel(message);
        }
    }

    /**
     * Give a starter card to each player
     */
    public void giveStarterCards() {
        for (Player p : this.players) {
            p.setStarterCard(this.board.getStarterDeck().draw());
        }
    }

    /**
     * Give 2 resource cards, 1 gold card and 2 objective card options to each player
     */
    public void fillPlayerHands() {
        for (Player p : this.players) {
            p.addToHand(this.board.getResourceDeck().draw());
            p.addToHand(this.board.getResourceDeck().draw());
            p.addToHand(this.board.getGoldDeck().draw());
            p.addObjOption(this.board.getObjectiveDeck().draw());
            p.addObjOption(this.board.getObjectiveDeck().draw());
        }
    }

    /**
     * Set the 2 common objective cards
     */
    public void drawCommonObjectives() {
        this.objectives.add(this.board.getObjectiveDeck().draw());
        this.objectives.add(this.board.getObjectiveDeck().draw());
    }

    /**
     * Assign the first turn and player
     */
    public void start() {
        Collections.shuffle(this.players);
        this.currentPlayer = this.players.getFirst();
        this.currentTurn = 1;
    }

    /**
     * Calculate the objective points for each player and add them to their score
     */
    public void calculateObjectivePoints() {
        for (Player player : this.players) {
            int objectiveScore = 0;
            for (ObjectiveCard objective : this.objectives) {
                objectiveScore += objective.getTotalPoints(player.getField());
            }
            objectiveScore += player.getObjCard().getTotalPoints(player.getField());
            player.setObjectiveScore(objectiveScore);
        }
    }

    //todo check again if we will add disconnections
    /**
     * Takes the game to the next turn, setting the last turn if necessary
     * and changing the current player to the next one
     */
    public void advanceTurn() {
        // set the last turn only if the final pahse has not already started
        if (this.lastTurn.isEmpty()) {
            this.lastTurn = getLastTurn();
        }
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % playersCount);
        this.currentTurn += 1;
    }

    public int getPlayersCount(){
        return this.playersCount;
    }

    public List<Player> getPlayers(){
        return Collections.unmodifiableList(this.players);
    }

    public Player getCurrentPlayer(){ return this.currentPlayer; }

    public Board getBoard(){
        return this.board;
    }

    public int getCurrentTurn() {
        return this.currentTurn;
    }

    public Optional<Integer> getRemainingTurns() {
        return this.lastTurn.map(lastTurn -> Math.max(0, lastTurn - this.currentTurn + 1));
    }

    public List<ObjectiveCard> getObjectives(){
        return Collections.unmodifiableList(this.objectives);
    }

    public GamePhase getGamePhase(){
        return this.gamePhase;
    }

    public TurnPhase getTurnPhase() {
        return this.turnPhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    /**
     * @return true if the lobby is full
     */
    public boolean isLobbyFull() {
        return this.playersCount == this.players.size();
    }

    public GameView getView() {
        return new GameView(this);
    }

    /**
     * @return the number of the last turn if the final phase has been reached, otherwise null
     */
    private Optional<Integer> getLastTurn() {
        Integer lastTurn = null;
        boolean hasReachedScore = false;
        for (Player player : this.players) {
            if (player.getScore() >= Config.SCORE_FOR_FINAL_PHASE) {
                hasReachedScore = true;
                break;
            }
        }
        // if a player has reached 20 points or if both decks are empty
        if (hasReachedScore || (board.getGoldDeck().isEmpty()) && board.getResourceDeck().isEmpty()) {
            // set a last round for everyone
            lastTurn = this.currentTurn + this.playersCount;
            // adjust it so that everyone is going to make the same amoutn of moves
            if (lastTurn % this.playersCount != 0) {
                lastTurn += this.playersCount - (lastTurn % this.playersCount);
            }
        }
        return Optional.ofNullable(lastTurn);
    }
}
