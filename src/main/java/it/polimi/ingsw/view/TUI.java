package it.polimi.ingsw.view;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.JoinMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.CreateGameMessage;
import it.polimi.ingsw.network.message.servertoclient.CreateGameAckMessage;
import it.polimi.ingsw.network.message.servertoclient.UsernameNotValidMessage;
import it.polimi.ingsw.network.message.servertoclient.UsernameAckMessage;
import it.polimi.ingsw.utilities.Printer;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TUI extends View {
    private final Scanner scanner;


    public TUI(Client client){
        super(client);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        super.run();
        printTitle();
        this.username = readUsername();
        chooseCreateOrJoin();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception ignored) {}
    }

    private void printTitle() {
        System.out.println("  _____          _              _   _       _                   _ _ ");
        System.out.println(" / ____|        | |            | \\ | |     | |                 | (_)");
        System.out.println("| |     ___   __| | _____  __  |  \\| | __ _| |_ _   _ _ __ __ _| |_ ___");
        System.out.println("| |    / _ \\ / _` |/ _ \\ \\/ /  | . ` |/ _` | __| | | | '__/ _` | | / __|");
        System.out.println("| |___| (_) | (_| |  __/>  <   | |\\  | (_| | |_| |_| | | | (_| | | \\__ \\");
        System.out.println(" \\_____\\___/ \\__,_|\\___/_/\\_\\  |_| \\_|\\__,_|\\__|\\__,_|_|  \\__,_|_|_|___/");
    }

    private String readUsername() {
        while (true) {
            System.out.println("Choose a username:");
            String username = scanner.nextLine();
            notifyAllListeners(new UsernameMessage(username));
            Message response = getClient().waitForMessage();

            if (response instanceof UsernameAckMessage) {
                return username;
            }
            else if (response instanceof UsernameNotValidMessage m) {
                Printer.printError(m.getMessage());
            }
        }
    }

    private void chooseCreateOrJoin() {
        while (true) {
            System.out.println("What do you want to do?");
            System.out.println("1) Create a new game");
            System.out.println("2) Join a game");
            int choice = readInt(1, 2);
            if (choice == 1) {
                System.out.println("How many players do you want in the lobby? (between 2 and 4)");
                int playersChoice = readInt(2, 4);
                notifyAllListeners(new CreateGameMessage(this.username, playersChoice));
                Message response = getClient().waitForMessage();
                if (response instanceof CreateGameAckMessage r) {
                    System.out.println("Game created successfully with id " + r.getGameId() + ". Waiting for players to join...");
                    break;
                }
            } else {
                System.out.println("Type the ID of an existing game:");
                int gameId = getGameID();
                notifyAllListeners(new JoinMessage(this.username, gameId));
            }
        }
    }

    private int readInt(int min, int max) {
        int choice;
        do {
            choice = scanner.nextInt();
        } while (choice < min || choice > max);
        return choice;
    }

    // TODO: change gameID to String and check length
    private int getGameID() {
        return scanner.nextInt();
    }
}