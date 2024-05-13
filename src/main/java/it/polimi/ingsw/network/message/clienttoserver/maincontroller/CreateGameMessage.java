package it.polimi.ingsw.network.message.clienttoserver.maincontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.exceptions.CannotCreateGameException;
import it.polimi.ingsw.exceptions.UsernameNotPlayingException;
import it.polimi.ingsw.network.message.servertoclient.CannotCreateGameMessage;
import it.polimi.ingsw.network.message.servertoclient.CreateGameAckMessage;

public class CreateGameMessage extends MainControllerMessage {
    private final int playersCount;

    public CreateGameMessage(String username, int playersCount) {
        super(username);
        this.playersCount = playersCount;
    }

    public int getPlayersCount() { return this.playersCount; }

    @Override
    public void execute(MainController controller) {
        try {
            controller.createGame(this.getUsername(), this.playersCount);
            GameController game = controller.getGameOfPlayer(this.getUsername());
            game.notifyAllListeners(new CreateGameAckMessage(game.getId()));
        } catch (CannotCreateGameException e) {
            controller.notifyListener(this.getUsername(), new CannotCreateGameMessage());
        } catch (UsernameNotPlayingException ignored) {
        }
    }
}
