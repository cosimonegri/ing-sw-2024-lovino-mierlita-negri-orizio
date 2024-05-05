package it.polimi.ingsw.view;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.*;
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
        this.username = readUsername();
        chooseCreateOrJoin();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception ignored) {}
    }

    private String readUsername() {
        while (true) {
            System.out.println("Choose a username:");
            String username = scanner.nextLine();
            notifyAllListeners(new UsernameMessage(username));
            Message response = getClient().waitForMessage();

            if (response instanceof UsernameValidMessage) {
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
                if (response instanceof CreateGameAckMessage) {
                    System.out.println("Game created successfully. Waiting for players to join...");
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