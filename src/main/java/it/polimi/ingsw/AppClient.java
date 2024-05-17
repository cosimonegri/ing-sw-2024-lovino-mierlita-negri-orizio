package it.polimi.ingsw;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.GUI;
import it.polimi.ingsw.view.TUI;
import it.polimi.ingsw.view.View;

public class AppClient {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Command line argument for the network protocol not found");
            return;
        }

        try {
            String connection = args[0].toUpperCase();
            View view = new GUI();
            Client client = new Client(view, ConnectionType.valueOf(connection));
            client.run();
        } catch (IllegalArgumentException e) {
            System.err.println("The chosen network protocol doesn't exists");
        }
    }
}
