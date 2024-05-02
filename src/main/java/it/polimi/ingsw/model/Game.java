package it.polimi.ingsw.model;

import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class to represent a game of 2 to 4 players.
 */

public class Game {
    /**
     * ID number to identify the different games
     */
    private int id;
    /**
     * Number of players for the lobby (not actual players in game), min of 2 and max of 4
     */
    private final int playersCount;
    /**
     * List of players that joined the lobby, the order is that of entry
     */
    private final List<Player> players;
    /**
     * Reference to the player whose turn it is.
     */
    private Player currentPlayer;
    /**
     * Reference to the current game's board
     */
    private Board board;
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
     * Constructor of the class
     *
     * @param playersCount the number of players wanted for the match
     * @throws IllegalArgumentException when playerCount has not a legal value
     */
    public Game(int id, int playersCount){
        if(id < 0) {throw new IllegalArgumentException("ID of game cannot be negative");}
        if(playersCount < 2 || playersCount > 4) {
            throw new IllegalArgumentException("Amount of players cannot be zero, negative nor smaller than 2 or greater than 4");}
        this.playersCount = playersCount;
        this.players = new ArrayList<>();
        this.currentPlayer = null;
        this.currentTurn = 0;
        this.objectives = new ArrayList<>();
        try {
            this.board = new Board();
        }
        //todo implement try-catch to controller
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a player to the game assigning it a unique marker
     *
     * @param username is the username of a new player
     * @throws UsernameAlreadyTakenException when a new player tries to choose an already taken username
     * @throws LobbyFullException when a new player tries to enter a game of already "playerCount" players
     */
    public void addPlayer(String username) throws UsernameAlreadyTakenException, LobbyFullException {

        if(this.players.size() == this.playersCount) throw new LobbyFullException();
        if(username.isEmpty() || username.isBlank() || !(username.matches("[a-zA-Z_0-9]*"))){
            throw new IllegalArgumentException("Username must be alphanumeric and not null");
        }
        for(Player p : this.players) {
            if(p.getUsername().equals(username)){ throw new UsernameAlreadyTakenException();}
        }

        List<Marker> markers = new ArrayList<>();
        markers.add(Marker.GREEN);
        markers.add(Marker.RED);
        markers.add(Marker.BLUE);
        markers.add(Marker.YELLOW);
        int maxBound = 4;
        Random rand = new Random();
        boolean markerTaken = false;

        Marker randMarker = markers.get(rand.nextInt(maxBound));
        if(!this.players.isEmpty()) {
            do{
                markerTaken = false;
                for(Player player : this.players){
                    if(player.getMarker().equals(randMarker)){
                        markerTaken = true;
                        randMarker = markers.get(rand.nextInt(maxBound));
                    }
                }
            }while(markerTaken);
        }
        this.players.add(new Player(username, randMarker));
    }

    /**
     * Removes a player from the game
     *
     * @param username of the player to be removed
     * @throws IllegalArgumentException when the username is null or blank
     */
    public void removePlayer(String username){
        if(this.players.isEmpty() || username.isEmpty() || username.isBlank() || !(username.matches("[a-zA-Z_0-9]*"))){
            return;}

        for(Player p : this.players) {
            if(p.getUsername().equals(username)){
                this.players.remove(p);
                return;
            }
        }
    }

    /**
     * Initializes the board, which initializes all the cards and decks
     * gives to all players their starting hands and assigns the first turn and player.
     */
    public void start() throws StillWaitingPlayersException{
        if(this.currentTurn != 0){ return; }
        if(this.playersCount > this.players.size()){ throw new StillWaitingPlayersException(); }

        addObjectives();
        this.currentTurn += 1;
        this.currentPlayer = this.players.getFirst();
        for(Player p : this.players){
            p.addToHand(this.board.getResourceDeck().draw());
            p.addToHand(this.board.getResourceDeck().draw());
            p.addToHand(this.board.getGoldDeck().draw());

            p.setStarterCard(this.board.getStarterDeck().draw());

            List<ObjectiveCard> objectiveCardList = new ArrayList<>();
            objectiveCardList.add(this.board.getObjectiveDeck().draw());
            objectiveCardList.add(this.board.getObjectiveDeck().draw());
            p.setObjOptions(objectiveCardList);
        }
    }

    /**
     * Takes the game to the next turn changing the current player to the next in line
     */
    public void advanceTurn() throws GameNotStartedYetException{
        if(this.currentTurn == 0) {throw new GameNotStartedYetException();}
        this.currentTurn += 1;
        int currentPlayerIndex = this.players.indexOf(this.currentPlayer);
        if(!this.currentPlayer.equals(this.players.getLast())){
            this.currentPlayer = this.players.get(currentPlayerIndex + 1); }
        else{ this.currentPlayer = this.players.getFirst(); }
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

    public int getId() {
        return this.id;
    }

    public GamePhase getGamePhase(){
        return this.gamePhase;
    }

    public TurnPhase getTurnPhase() {
        return this.turnPhase;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
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
