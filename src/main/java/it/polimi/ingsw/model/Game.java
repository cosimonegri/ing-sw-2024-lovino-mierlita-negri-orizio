package it.polimi.ingsw.model;

import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to represent a game of 2 to 4 players.
 */

public class Game {
    /**
     * Number of players in lobby, min of 0 and max of 4
     */
    private final int playerCount;
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
     * Constructor of the class
     *
     * @param playerCount the number of players wanted for the match
     * @throws IllegalArgumentException when playerCount has not a legal value
     */
    public Game(int playerCount){
        if(playerCount <= 0 || playerCount > 4) {
            throw new IllegalArgumentException("Amount of players cannot be zero, negative nor greater than 4");}
        this.playerCount = playerCount;
        this.players = new ArrayList<>();
        this.currentPlayer = null;
        this.board = null;
        this.currentTurn = 0;
        this.objectives = new ArrayList<>();
    }

    /**
     * Add a player to the game
     *
     * @param username is the username of a new player
     * @param marker is the color marker of a new player
     * @throws UsernameAlreadyTakenException when a new player tries to choose an already taken username
     * @throws LobbyFullException when a new player tries to enter a game of already "playerCount" players
     * @throws MarkerAlreadyTakenException when a new player tries to choose an already taken marker
     */
    public void addPlayer(String username, Marker marker) throws UsernameAlreadyTakenException, LobbyFullException, MarkerAlreadyTakenException {
        if(username.isEmpty() || username.isBlank() || !(username.matches("[a-zA-Z0-9]"))){
            throw new IllegalArgumentException("Username must be alphanumeric and not null");
        }
        for(Player p : this.players) {
            if(p.getUsername().equals(username)){ throw new UsernameAlreadyTakenException(); }
        }
        for(Player p : this.players){
            if(p.getMarker().equals(marker)){ throw new MarkerAlreadyTakenException(); }
        }

        if(this.players.size() < this.playerCount){ this.players.add(new Player(username, marker)); }
        else{ throw new LobbyFullException(); }
    }

    /**
     * Removes a player from the game
     *
     * @param username of the player to be removed
     * @throws NonExistentUsernameException when the lobby is empty or if the username does not match any player
     * @throws IllegalArgumentException when the username is null or blank
     */
    public void removePlayer(String username) throws NonExistentUsernameException {
        if(username.isEmpty() || username.isBlank()){
            throw new IllegalArgumentException("Username must be alphanumeric and not null");
        }
        if(this.players.isEmpty()) {
            throw new NonExistentUsernameException();
        }

        for(Player p : this.players) {
            if(p.getUsername().equals(username)){
                this.players.remove(p);
                return;
            }
        }
        throw new NonExistentUsernameException();
    }

    /**
     * Initializes the board, which initializes all the cards and decks
     * gives to all players their starting hands and assigns the first turn and player.
     *
     * @throws GameAlreadyStartedException when the game has already started for the current lobby
     */
    public void start() throws GameAlreadyStartedException {
        if(this.board != null){ throw new GameAlreadyStartedException(); }
        try {
            this.board = new Board();
        }
        catch (IOException ex) {
            throw new RuntimeException("Board created unsuccessfully");
        }

        addObjectives();
        this.currentTurn += 1;
        this.currentPlayer = this.players.getFirst();
        for(Player p : this.players){
            p.addToHand(this.board.getStarterDeck().draw());
            p.addToHand(this.board.getResourceDeck().draw());
            p.addToHand(this.board.getResourceDeck().draw());
            p.addToHand(this.board.getGoldDeck().draw());
            addObjectives();
        }
    }

    /**
     * Takes the game to the next turn changing the current player to the next in line
     */
    public void advanceTurn() {
        this.currentTurn += 1;
        if(!this.currentPlayer.equals(this.players.getLast())){ this.currentPlayer = this.players.listIterator().next(); }
        else{ this.currentPlayer = this.players.getFirst(); }
    }

    public int getPlayerCount(){
        return this.playerCount;
    }

    public List<Player> getPlayers(){
        return Collections.unmodifiableList(this.players);
    }

    public Board getBoard(){
        return this.board;
    }

    public int getCurrentTurn(){
        return this.currentTurn;
    }

    public List<ObjectiveCard> getObjectives(){
        return Collections.unmodifiableList(this.objectives);
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
