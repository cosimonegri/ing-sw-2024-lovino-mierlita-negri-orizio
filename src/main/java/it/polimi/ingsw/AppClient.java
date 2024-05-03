package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.server.ServerInterface;
import it.polimi.ingsw.network.server.SocketServerStub;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.utilities.Printer;
import it.polimi.ingsw.view.GUI;
import it.polimi.ingsw.view.TUI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppClient {
    private static final String PARAM_RMI = "rmi";
    private static final String PARAM_SOCKET = "socket";
    private static final String PARAM_TUI = "tui";
    private static final String PARAM_GUI = "gui";

    public static void main(String[] args) {
        if (args.length != 2) {
            Printer.printError("Command line argument for the network protocol not found");
            return;
        }

        if(args[1].equalsIgnoreCase(PARAM_TUI)){
            TUI tui = new TUI();
            if (args[0].equalsIgnoreCase(PARAM_RMI)) {
                try {
                    ServerInterface server = setupRmiConnection();
                    ClientInterface client = new Client(server, tui);
                } catch (RemoteException | NotBoundException e) {
                    Printer.printError("Cannot connect to the RMI Server", e);
                }
            }
            else if (args[0].equalsIgnoreCase(PARAM_SOCKET)) {
                ServerInterface server = setupSocketConnection();
                ClientInterface client = new Client(server, tui);
            }
        }
        else if(args[1].equalsIgnoreCase(PARAM_GUI)){
            GUI gui = new GUI();
            if (args[0].equalsIgnoreCase(PARAM_RMI)) {
                try {
                    ServerInterface server = setupRmiConnection();
                    ClientInterface client = new Client(server, gui);
                } catch (RemoteException | NotBoundException e) {
                    Printer.printError("Cannot connect to the RMI Server", e);
                }
            }
            else if (args[0].equalsIgnoreCase(PARAM_SOCKET)) {
                ServerInterface server = setupSocketConnection();
                ClientInterface client = new Client(server, gui);
            }
        }
        else {
            Printer.printError("The chosen network protocol doesn't exists");
        }
    }

    private static ServerInterface setupRmiConnection() throws RemoteException, NotBoundException {
        ServerInterface server;
        System.out.println("Connecting to the RMI server...");
        Registry registry = LocateRegistry.getRegistry(Config.HOSTNAME, Config.RMI_PORT);
        server = (ServerInterface) registry.lookup(Config.RMI_NAME);
        System.out.println("Connection established successfully...");
        return server;
    }

    //TODO sistemare eccezioni
    private static ServerInterface setupSocketConnection() {
        System.out.println("Connecting to the Socket server...");
        ServerInterface server = new SocketServerStub();
        System.out.println("Connection established successfully...");
        return server;
    }
}
