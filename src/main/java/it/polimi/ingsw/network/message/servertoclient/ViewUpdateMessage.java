package it.polimi.ingsw.network.message.servertoclient;

import it.polimi.ingsw.modelView.GameView;

/**
 * Message sent to the client when someone does an action during the game.
 */
public class ViewUpdateMessage implements ServerToClientMessage {
    /**
     * Object that contains all the information about the game.
     */
    private final GameView gameView;
    /**
     * Message that describes what happened.
     */
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
