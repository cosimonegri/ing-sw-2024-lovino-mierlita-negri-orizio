package it.polimi.ingsw.network.message.servertoclient;

public class CreateGameAckMessage implements ServerToClientMessage {
    private final int gameId;

    public CreateGameAckMessage(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return this.gameId;
    }
}
