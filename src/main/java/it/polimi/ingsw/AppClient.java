package it.polimi.ingsw;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.server.ServerInterface;
import it.polimi.ingsw.network.server.SocketServerStub;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.utilities.Printer;
import it.polimi.ingsw.view.TUI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
        } catch (RemoteException e) {
            System.err.println("Cannot export the client as a remote object");
        }
    }
}
