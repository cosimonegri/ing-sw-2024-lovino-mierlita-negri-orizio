package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.MainController;

public class JoinMessage implements Message {
    private final String username;
    private final int gameId;

    public JoinMessage(String username, int gameId){
        this.username = username;
        this.gameId = gameId;
    }

    public String getUsername() {
        return username;
    }

    public int getGameId() {
        return gameId;
    }

    //TODO catch exception correctly
    @Override
    public void execute() {
        try {
            MainController.getInstance().joinGame(this.getUsername(), this.getGameId());
        } catch (Exception ignored) {}
    }
}