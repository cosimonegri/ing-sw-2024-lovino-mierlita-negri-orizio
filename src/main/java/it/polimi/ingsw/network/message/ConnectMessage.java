package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.exceptions.UsernameNotValidException;
import it.polimi.ingsw.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.utilities.Printer;

import java.rmi.RemoteException;

public class ConnectMessage implements Message {
    private final String username;
    private final ClientInterface client;

    public ConnectMessage(String username, ClientInterface client) {
        this.username = username;
        this.client = client;
    }

    public String getUsername() {
        return this.username;
    }

    public ClientInterface getClient() {
        return this.client;
    }

    public void execute(MainController controller) {
        try {
            controller.connect(this.username, (message) -> {
                try {
                    this.client.messageFromServer(message);
                } catch (RemoteException e) {
                    Printer.printError("Remote Exception", e);
                }
            });
            try {
                this.client.messageFromServer(new UsernameValidMessage());
            } catch (RemoteException e2) {
                Printer.printError("Remote Exception", e2);
            }
        } catch (UsernameNotValidException | UsernameAlreadyTakenException e) {
            try {
                this.client.messageFromServer(new UsernameNotValidMessage(e.getMessage()));
            } catch (RemoteException e2) {
                Printer.printError("Remote Exception", e2);
            }
        }
    }
}
