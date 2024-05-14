package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.ChooseMarkerMessage;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.PlayStarterMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.JoinGameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.CreateGameMessage;
import it.polimi.ingsw.network.message.servertoclient.*;
import it.polimi.ingsw.utilities.Printer;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TUI extends View {
    private final Scanner scanner;

    public TUI() {
        super();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        printTitle();
        connectUsername();
        createOrJoinGame();
        setupGame();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception ignored) {}
    }

    private void printTitle() {
        System.out.println();
        System.out.println("  _____          _              _   _       _                   _ _ ");
        System.out.println(" / ____|        | |            | \\ | |     | |                 | (_)");
        System.out.println("| |     ___   __| | _____  __  |  \\| | __ _| |_ _   _ _ __ __ _| |_ ___");
        System.out.println("| |    / _ \\ / _` |/ _ \\ \\/ /  | . ` |/ _` | __| | | | '__/ _` | | / __|");
        System.out.println("| |___| (_) | (_| |  __/>  <   | |\\  | (_| | |_| |_| | | | (_| | | \\__ \\");
        System.out.println(" \\_____\\___/ \\__,_|\\___/_/\\_\\  |_| \\_|\\__,_|\\__|\\__,_|_|  \\__,_|_|_|___/");
        System.out.println();
    }

    private void connectUsername() {
        String username = readUsername();

        while (true) {
            ServerToClientMessage response = waitForMessage();

            if (response instanceof UsernameAckMessage) {
                this.username = username;
                break;
            }
            else if (response instanceof UsernameNotValidMessage m) {
                Printer.printError(m.getMessage());
                username = readUsername();
            }
            else {
                addMessage(response);
            }
        }
    }

    private String readUsername() {
        System.out.println("Choose a username:");
        String username = scanner.nextLine();
        notifyAllListeners(new UsernameMessage(username));
        return username;
    }

    private void createOrJoinGame() {
        chooseCreateOrJoin();

        while (true) {
            ServerToClientMessage response = waitForMessage();

            if (response instanceof CreateGameAckMessage r) {
                System.out.println("Created game with ID " + r.getGameId() + ". Waiting for players to join...");
            }
            else if (response instanceof CreateGameErrorMessage) {
                // cannot read json files in the server
                Printer.printError("Cannot create a game. Try again later...");
                System.exit(1);
            }
            else if (response instanceof LobbyMessage r) {
                printLobbyUsernames(r.getUsernames());
            }
            else if (response instanceof LobbyNotValidMessage r) {
                Printer.printError(r.getMessage());
                chooseCreateOrJoin();
            }
            else if (response instanceof GameSetupStartedMessage) {
                System.out.println("Lobby full. The game is starting...");
                break;
            }
            else {
                addMessage(response);
            }
        }
    }

    private void chooseCreateOrJoin() {
        System.out.println("What do you want to do?");
        printOptions("Create a new game", "Join a game");
        int choice = readInt(1, 2);
        switch (choice) {
            case 1:
                System.out.println("How many players do you want in the lobby?");
                int playersCount = readInt(2, 4);
                notifyAllListeners(new CreateGameMessage(this.username, playersCount));
                break;
            case 2:
                System.out.println("Type the ID of the game you want to join:");
                int gameId = this.scanner.nextInt();
                notifyAllListeners(new JoinGameMessage(this.username, gameId));
                break;
        }
    }

    private void printLobbyUsernames(List<String> usernames) {
        System.out.print("Players connected: ");
        for (int i = 0; i < usernames.size(); i++) {
            System.out.print(usernames.get(i));
            if (usernames.get(i).equals(this.username)) {
                System.out.print(" (you)");
            }
            if (i != usernames.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    private void setupGame() {
        chooseMarker();

        while (true) {
            ServerToClientMessage response = waitForMessage();

            if (response instanceof ChooseMarkerAckMessage) {
                chooseStarter();
            }
            else if (response instanceof ChooseMarkerErrorMessage) {
                Printer.printError("This marker has already been taken");
                chooseMarker();
            }
            else {
                addMessage(response);
            }
        }
    }

    private void chooseMarker() {
        System.out.println("Which marker do you want?");
        printOptions("Blue", "Green", "Red", "Yellow");
        int choice = readInt(1, 4);
        Marker marker = switch (choice) {
            case 1 -> Marker.BLUE;
            case 2 -> Marker.GREEN;
            case 3 -> Marker.RED;
            default -> Marker.YELLOW;
        };
        notifyAllListeners(new ChooseMarkerMessage(this.username, marker));
    }

    private void chooseStarter() {
        System.out.println("How do you want to play the starter card?");
        printOptions("Front visible", "Back visible");
        int choice = readInt(1, 2);
        notifyAllListeners(new PlayStarterMessage(this.username, choice == 2));
    }

    private void printOptions(String... options) {
        for (int i = 0; i < options.length; i++) {
            int num = i + 1;
            System.out.println(num + ") " + options[i]);
        }
    }

    private int readInt(int min, int max) {
        int choice = max + 1;
        boolean firstIter = true;
        while (choice < min || choice > max) {
            if (!firstIter) {
                Printer.printError("Choose a number between " + min + " and " + max);
            }
            try {
                choice = this.scanner.nextInt();
            } catch (InputMismatchException e) {
                this.scanner.nextLine();
            }
            firstIter = false;
        }
        return choice;
    }
}