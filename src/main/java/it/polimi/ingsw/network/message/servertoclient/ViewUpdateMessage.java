package it.polimi.ingsw.network.message.servertoclient;

import it.polimi.ingsw.modelView.GameView;

public class ViewUpdateMessage implements ServerToClientMessage {
    private final GameView gameView;
    private final String message;

    public ViewUpdateMessage(GameView gameView, String message) {
        this.gameView = gameView;
        this.message = message;
    }

    public GameView getGameView() {
        return this.gameView;
    }

    public String getMessage() {
        return this.message;
    }
}
