package it.polimi.ingsw.network.message.clienttoserver.maincontroller;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.exceptions.LobbyFullException;
import it.polimi.ingsw.exceptions.LobbyNotValidException;
import it.polimi.ingsw.network.message.servertoclient.LobbyNotValidMessage;

public class JoinMessage extends MainControllerMessage {
    private final int gameId;

    public JoinMessage(String username, int gameId){
        super(username);
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    @Override
    public void execute(MainController controller) {
        try {
            controller.joinGame(this.getUsername(), this.gameId);
        } catch (LobbyNotValidException | LobbyFullException e) {
            controller.notifyListener(this.getUsername(), new LobbyNotValidMessage(e.getMessage()));
        }
    }
}