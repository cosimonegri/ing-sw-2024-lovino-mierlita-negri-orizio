package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.server.ServerInterface;
import it.polimi.ingsw.view.View;

import java.rmi.RemoteException;

public class Client implements ClientInterface {
    private ServerInterface server;
    private View view;

    public Client(ServerInterface server, View view) {
        this.server = server;
        this.view = view;
        view.addListener((message) -> {
            try {
                server.messageFromClient(message);
            } catch (RemoteException e) {

            }
        });
        view.run();
    }

    @Override
    public void messageFromServer(Message message) throws RemoteException {

    }
}
