package it.polimi.ingsw;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.TUI;

public class AppClient {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Command line argument for the network protocol not found");
            return;
        }

        try {
            String connection = args[0].toUpperCase();
            Client client = new Client(ConnectionType.valueOf(connection));
            View view = new TUI(client);
            view.run();
        } catch (IllegalArgumentException e) {
            System.err.println("The chosen network protocol doesn't exists");
        }
    }
}
