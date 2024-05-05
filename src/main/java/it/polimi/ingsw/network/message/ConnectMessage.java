package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.exceptions.UsernameNotValidException;
import it.polimi.ingsw.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.network.client.ClientInterface;

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

    //TODO check error handling
    public void execute() {
        try {
            MainController.getInstance().connect(this.username, (message) -> {
                try {
                    this.client.messageFromServer(message);
                } catch (RemoteException e) {
                    System.err.println("Cannot send message to the client");
                }
            });
            try {
                this.client.messageFromServer(new UsernameValidMessage());
            } catch (RemoteException e) {
                System.err.println("Cannot send message to the client");
            }
        } catch (UsernameNotValidException | UsernameAlreadyTakenException e) {
            try {
                this.client.messageFromServer(new UsernameNotValidMessage(e.getMessage()));
            } catch (RemoteException e2) {
                System.err.println("Cannot send message to the client");
            }
        }
    }
}
