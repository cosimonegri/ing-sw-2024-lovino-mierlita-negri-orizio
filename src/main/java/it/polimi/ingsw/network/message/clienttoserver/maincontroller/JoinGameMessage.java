package it.polimi.ingsw.network.message.clienttoserver.maincontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.exceptions.LobbyFullException;
import it.polimi.ingsw.exceptions.LobbyNotValidException;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.message.servertoclient.ViewUpdateMessage;
import it.polimi.ingsw.network.message.servertoclient.LobbyMessage;
import it.polimi.ingsw.network.message.servertoclient.LobbyNotValidMessage;

public class JoinGameMessage extends MainControllerMessage {
    private final int gameId;

    public JoinGameMessage(String username, int gameId){
        super(username);
        this.gameId = gameId;
    }

    @Override
    public void execute(MainController controller) {
        try {
            GameController game = controller.joinGame(this.getUsername(), this.gameId);
            game.notifyAllListeners(new LobbyMessage(
                    game.getPlayersCount(),
                    game.getPlayers().stream().map(Player::getUsername).toList(),
                    this.getUsername() + " has joined."
            ));
            if (game.getPhase() == GamePhase.STARTER) {
                game.notifyAllListeners(new ViewUpdateMessage(game.getModelView(), "Lobby full. The game has started."));
            }
        } catch (LobbyNotValidException | LobbyFullException e) {
            controller.notifyListener(this.getUsername(), new LobbyNotValidMessage(e.getMessage()));
        }
    }
}