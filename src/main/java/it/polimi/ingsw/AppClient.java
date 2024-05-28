package it.polimi.ingsw;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.View;

public class AppClient {
    private static final String IPV4_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    private View view;
    private String connection;
    private String ip;

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
                case "-n" -> connection = args[i + 1].equalsIgnoreCase("RMI") ? "RMI" : null;
                // user interface, default gui
                case "-i" -> view = args[i + 1].equalsIgnoreCase("TUI") ? new TUI() : null;
                // ip, default localhost
                case "-h" -> ip = args[i + 1].matches(IPV4_PATTERN) ? args[i + 1] : null;
            }
        }
        if (connection == null || view == null || ip == null) {
            System.err.println("Invalid parameters.");
            System.exit(1);
        }
//        System.out.println("Connection: " + connection);
//        System.out.println("View: " + view.getClass());
//        System.out.println("Ip: " + ip);
        Client client = new Client(view, ConnectionType.valueOf(connection), ip);
        client.run();

    }
}
