package it.polimi.ingsw;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.View;

public class AppClient {
    private static final String IPV4_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";

    private void parseArgs(String[] args) {
        // parse connection



    }
    public static void main(String[] args) {
        String connection = "SOCKET";
        View view = new GUI();
        String ip = "127.0.0.1";

        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                // connection type, default socket
                case "-n" -> {
                    if(args[i + 1].equalsIgnoreCase("RMI")) {
                        connection = "RMI";
                    } else if(args[i + 1].equalsIgnoreCase("SOCKET")) {
                        connection = "SOCKET";
                    } else {
                        System.err.println("Invalid connection parameter.");
                        System.exit(1);
                    }
                }
                // user interface, default gui
                case "-i" -> {
                    if(args[i + 1].equalsIgnoreCase("TUI")) {
                        view = new TUI();
                    } else if(args[i + 1].equalsIgnoreCase("GUI")) {
                        view = new GUI();
                    } else {
                        System.err.println("Invalid user interface parameter.");
                        System.exit(1);
                    }
                }
                // ip, default localhost
                case "-h" -> {
                    if (args[i + 1].matches(IPV4_PATTERN)) {
                        ip = args[i + 1];
                    } else {
                        System.err.println("Invalid ip parameter.");
                        System.exit(1);
                    }
                }
            }
        }

        Client client = new Client(view, ConnectionType.valueOf(connection), ip);
        client.run();
    }
}
