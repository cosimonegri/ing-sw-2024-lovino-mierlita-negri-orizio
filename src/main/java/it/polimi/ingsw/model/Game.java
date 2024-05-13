package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CannotCreateGameException;
import it.polimi.ingsw.exceptions.MarkerNotValidException;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;

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
     * List of players that joined the lobby, the order is that of entry
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
    /**
     * Set of available markers
     */
    private final Set<Marker> markers;

    /**
     * Constructor of the class
     *
     * @param playersCount the number of players wanted for the match
     * @throws IllegalArgumentException when playersCount has not a legal value
     */
    public Game(int playersCount) throws CannotCreateGameException {
        this.playersCount = playersCount;

        this.players = new ArrayList<>(playersCount);
        this.playerToListener = new HashMap<>(playersCount);
        this.currentPlayer = null;

        this.markers = new HashSet<>(List.of(Marker.values()));

        this.currentTurn = 0; // todo maybe remove
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
     * Initializes the board, which initializes all the cards and decks
     * gives to all players their starting hands and assigns the first turn and player.
     */
    public void start() {
        this.addObjectives();
        this.currentTurn += 1;
        this.currentPlayer = this.players.getFirst();

        for(Player p : this.players){
            p.addToHand(this.board.getResourceDeck().draw());
            p.addToHand(this.board.getResourceDeck().draw());
            p.addToHand(this.board.getGoldDeck().draw());

            p.setStarterCard(this.board.getStarterDeck().draw());

            List<ObjectiveCard> objectiveCardList = new ArrayList<>(2);
            objectiveCardList.add(this.board.getObjectiveDeck().draw());
            objectiveCardList.add(this.board.getObjectiveDeck().draw());

            p.setObjOptions(objectiveCardList);
        }
    }

    /**
     * Takes the game to the next turn changing the current player to the next in line
     */
    public void advanceTurn() {
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

    public int getCurrentTurn(){
        return this.currentTurn;
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
     * @param username username of a player
     * @param marker marker to be assigned to the player
     * @throws MarkerNotValidException when the marker has already been choosen
     */
    public void assignMarker(String username, Marker marker) throws MarkerNotValidException {
        if (!this.markers.contains(marker)) {
            throw new MarkerNotValidException();
        }
        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                p.setMarker(marker);
                markers.remove(marker);
            }
        }
    }

    /**
     * @return true if the lobby is full
     */
    public boolean isLobbyFull() {
        return this.playersCount == this.players.size();
    }

    /**
     * @param username username of a player
     * @return true if the player is the last of the round
     */
    public boolean isLastPlayer(String username) {
        return players.getLast().getUsername().equals(username);
    }

    /**
     * Draws the two public objectives and reveals them
     */
    private void addObjectives(){
        for(int i=0; i<2; i++){
            this.objectives.add(this.board.getObjectiveDeck().draw());
        }
    }
}
