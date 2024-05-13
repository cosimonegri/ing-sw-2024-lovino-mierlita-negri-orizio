package it.polimi.ingsw.network.message.clienttoserver.maincontroller;

import it.polimi.ingsw.controller.MainController;

public class LeaveMessage extends MainControllerMessage {
    private final int gameId;

    public LeaveMessage(String username, int gameId){
        super(username);
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    @Override
    public void execute(MainController controller) {

    }
}
