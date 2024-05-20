package it.polimi.ingsw;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.view.GUI;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.View;

public class AppClient {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Command line argument for the view type not found");
            System.exit(1);
        }
        else if (args.length == 1) {
            System.err.println("Command line argument for the network protocol not found");
            System.exit(1);
        }

        String viewString = args[0].toLowerCase();
        View view = Config.PARAM_TUI.equals(viewString)
                ? new TUI()
                : Config.PARAM_GUI.equals(viewString)
                ? new GUI()
                : null;
        if (view == null) {
            System.err.println("The chosen view type doesn't exist");
            System.exit(1);
        }

        String connectionString = args[1].toLowerCase();
        ConnectionType connection = Config.PARAM_RMI.equals(connectionString)
                ? ConnectionType.RMI
                : Config.PARAM_SOCKET.equals(connectionString)
                ? ConnectionType.SOCKET
                : null;
        if (connection == null) {
            System.err.println("The chosen network protocol doesn't exist");
            System.exit(1);
        }

        String ip = args.length >= 3 ? args[2] : Config.DEFAULT_IP;
        Client client = new Client(view, connection, ip);
        client.run();
    }
}
