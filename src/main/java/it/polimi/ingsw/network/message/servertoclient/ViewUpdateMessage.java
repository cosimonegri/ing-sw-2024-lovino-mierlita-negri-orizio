package it.polimi.ingsw.network.message.servertoclient;

import it.polimi.ingsw.modelView.GameView;

public class ViewUpdateMessage implements ServerToClientMessage {
    private final GameView gameView;

    public ViewUpdateMessage(GameView gameView) {
        this.gameView = gameView;
    }

    public GameView getGameView() {
        return this.gameView;
    }
}
