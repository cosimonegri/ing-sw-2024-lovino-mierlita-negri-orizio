package it.polimi.ingsw.network.message.clienttoserver.maincontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.exceptions.CannotCreateGameException;
import it.polimi.ingsw.exceptions.PlayersCountNotValidException;
import it.polimi.ingsw.network.message.servertoclient.CreateGameErrorMessage;
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
            GameController game = controller.createGame(this.getUsername(), this.playersCount);
            game.notifyAllListeners(new CreateGameAckMessage(game.getId()));
        } catch (PlayersCountNotValidException e) {
            controller.notifyListener(this.getUsername(), new CreateGameErrorMessage(e.getMessage()));
        } catch (CannotCreateGameException e) {
            controller.notifyListener(this.getUsername(), new CreateGameErrorMessage("Cannot create a game. Try again later."));
        }
    }
}
