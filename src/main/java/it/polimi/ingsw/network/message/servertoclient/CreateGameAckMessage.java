package it.polimi.ingsw.network.message.servertoclient;

/**
 * Message sent to the client when the action of creating a game has been successful.
 */
public class CreateGameAckMessage implements ServerToClientMessage {
    /**
     * ID of the new game.
     */
    private final int gameId;

    public CreateGameAckMessage(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return this.gameId;
    }
}
