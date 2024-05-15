package it.polimi.ingsw.network.message.clienttoserver.maincontroller;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.exceptions.UsernameNotValidException;
import it.polimi.ingsw.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.servertoclient.PingRequest;
import it.polimi.ingsw.network.message.servertoclient.UsernameNotValidMessage;
import it.polimi.ingsw.network.message.servertoclient.UsernameAckMessage;

import java.rmi.RemoteException;

public class ConnectMessage extends MainControllerMessage {
    private final ClientInterface client;

    public ConnectMessage(String username, ClientInterface client) {
        super(username);
        this.client = client;
    }

    public ClientInterface getClient() {
        return this.client;
    }

    //TODO check RemoteException handling
    @Override
    public void execute(MainController controller) {
        try {
            controller.connect(this.getUsername(), (message) -> {
                try {
                    this.client.messageFromServer(message);
                } catch (RemoteException e) {
                    System.err.println("Cannot send message to the client");
                }
            });
            controller.notifyListener(this.getUsername(), new UsernameAckMessage());
            // ping thread
//            new Thread(() -> {
//                while (true) {
//                    // ping request to the client
//                    controller.notifyListener(this.getUsername(), new PingRequest(this.getUsername()));
//                    // wait for response
//                }
//            }).start();

        } catch (UsernameNotValidException | UsernameAlreadyTakenException error) {
            try {
                this.client.messageFromServer(new UsernameNotValidMessage(error.getMessage()));
            } catch (RemoteException e) {
                System.err.println("Cannot send message to the client");
            }
        }
    }
}
