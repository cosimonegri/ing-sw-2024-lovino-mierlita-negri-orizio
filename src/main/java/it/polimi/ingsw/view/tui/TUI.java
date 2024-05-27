package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.DrawType;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.modelView.PlayerView;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.DrawCardMessage;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.ChooseMarkerMessage;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.ChooseObjectiveMessage;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.PlayCardMessage;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.PlayStarterMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.JoinGameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.CreateGameMessage;
import it.polimi.ingsw.network.message.servertoclient.*;
import it.polimi.ingsw.utilities.Printer;
import it.polimi.ingsw.view.View;

import java.util.*;

public class TUI extends View {
    private final Scanner scanner;
    private Map<Integer, Coordinates> numToCoordinates;

    public TUI() {
        super();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        printTitle();
        connectUsername();
        createOrJoinGame();
        setupGame();

        Thread mainThread = new Thread(() -> {
            while (true) {
                chooseAction();
            }
        });
        mainThread.start();

        Thread updateThread = new Thread(() -> {
            while (true) {
                ServerToClientMessage message = waitForMessage();

                if (message instanceof ViewUpdateMessage m) {
                    this.gameView = m.getGameView();
                    Printer.printInfo(m.getMessage());
                    if (this.gameView.isCurrentPlayer(this.username)) {
                        Printer.printInfo("It's your turn");
                    }
                }
                else if (message instanceof LobbyMessage m) {
                    printLobbyUsernames(m.getUsernames());
                }
                else {
                    addMessage(message);
                }
            }
        });
        updateThread.start();
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
        System.out.print("Choose a username: ");
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
            else if (response instanceof ViewUpdateMessage r) {
                this.gameView = r.getGameView();
                System.out.println("Lobby full. The game is starting...");
                break;
            }
            else {
                addMessage(response);
            }
        }
    }

    private void chooseCreateOrJoin() {
        System.out.println();
        System.out.println("What do you want to do?");
        int choice = chooseOption(true, "Create a new game", "Join a game");
        switch (choice) {
            case 1:
                int playersCount = chooseInRange("How many players do you want in the lobby", 2, 4);
                notifyAllListeners(new CreateGameMessage(this.username, playersCount));
                break;
            case 2:
                System.out.print("Type the ID of the game you want to join: ");
                int gameId = readInt(true);
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
                chooseObjective();
            }
            else if (response instanceof ChooseMarkerErrorMessage) {
                Printer.printError("This marker has already been taken");
                chooseMarker();
            }
            else if (response instanceof ChooseObjectiveAckMessage r) {
                System.out.println();
                System.out.println("Waiting for the other players to finish their setup...");
            }
            else if (response instanceof ChooseObjectiveErrorMessage r) {
                //todo this error should never happen
                Printer.printError("You cannot choose this objective");
                chooseObjective();
            }
            else if (response instanceof ViewUpdateMessage) {
                addMessage(response);
                break;
            }
            else {
                addMessage(response);
            }
        }
    }

    private void chooseMarker() {
        System.out.println();
        System.out.println("Which marker do you want?");
        int choice = chooseOption(true, "Blue", "Green", "Red", "Yellow");
        Marker marker = switch (choice) {
            case 1 -> Marker.BLUE;
            case 2 -> Marker.GREEN;
            case 3 -> Marker.RED;
            default -> Marker.YELLOW;
        };
        notifyAllListeners(new ChooseMarkerMessage(this.username, marker));
    }

    private void chooseStarter() {
        System.out.println();
        PlayableCard starter = this.gameView.getPlayer(this.username).getStarterCard();
        System.out.println("Front");
        GamePrinter.printCard(starter, false);
        System.out.println("Back");
        GamePrinter.printCard(starter, true);
        System.out.println("How do you want to play your starter card?");
        int choice = chooseOption(true, "Front visible", "Back visible");
        notifyAllListeners(new PlayStarterMessage(this.username, choice == 2));
    }

    private void chooseObjective() {
        System.out.println();
        System.out.println("Choose one of the following personal personal objectives:");
        List<ObjectiveCard> objectives = this.gameView.getPlayer(this.username).getObjectiveOptions();
        int choice = chooseOption(true, objectives.stream().map(GamePrinter::getObjectiveDescription).toList());
        notifyAllListeners(new ChooseObjectiveMessage(this.username, objectives.get(choice - 1)));
    }

    private void playTurn() {
        playCard();

        while (true) {
            ServerToClientMessage response = waitForMessage();

            if (response instanceof PlayCardAckMessage) {
                printBoard();
                drawCard();
            }
            else if (response instanceof PlayCardErrorMessage r) {
                Printer.printError(r.getMessage() + " Choose another card");
                playCard();
            }
            else if (response instanceof DrawCardAckMessage) {
                break;
            }
            else if (response instanceof DrawCardErrorMessage r) {
                Printer.printError(r.getMessage() + " Choose another card");
                drawCard();
            }
            else {
                addMessage(response);
            }
        }

    }

    private void playCard() {
        List<PlayableCard> hand = this.gameView.getCurrentPlayer().getHand();
        System.out.println();
        int cardChoice = chooseInRange("Which card do you want to play", 1, hand.size());
        int flippedChoice = chooseOption(true, "Front visible", "Back visible");
        int coordsChoice = chooseInRange("Where do you want to place it", 1, this.numToCoordinates.size());
        notifyAllListeners(new PlayCardMessage(
                this.username, hand.get(cardChoice - 1), flippedChoice == 2, this.numToCoordinates.get(coordsChoice))
        );
    }

    private void drawCard() {
        System.out.println();
        System.out.println("Which card do you want to draw?");
        int choice = chooseOption(
                true,
                "Visible top-left",
                "Visible top-right",
                "Visible bottom-left",
                "Visible bottom-right",
                "Gold from deck",
                "Resource from deck"
        );
        DrawType type = switch (choice) {
            case 5 -> DrawType.GOLD;
            case 6 -> DrawType.RESOURCE;
            default -> DrawType.VISIBLE;
        };
        PlayableCard card = switch (choice) {
            case 1 -> this.gameView.getBoard().getVisibleCards()[0];
            case 2 -> this.gameView.getBoard().getVisibleCards()[1];
            case 3 -> this.gameView.getBoard().getVisibleCards()[2];
            case 4 -> this.gameView.getBoard().getVisibleCards()[3];
            default -> null;
        };
        notifyAllListeners(new DrawCardMessage(this.username, type, card));
    }

    private void chooseAction() {
        List<String> options = new ArrayList<>(Arrays.asList(
                "Play turn", "Print board", "Print leaderboard", "Print my field", "Print my hand"
        ));
        Map<Integer, String> choiceToUsername = new HashMap<>();
        int playerChoiceIndex  = options.size() + 1;
        for (PlayerView player : this.gameView.getPlayers()) {
            if (player.getUsername().equals(this.username)) {
                continue;
            }
            choiceToUsername.put(playerChoiceIndex, player.getUsername());
            options.add("Print " + player.getUsername() + "'s field and hand");
            playerChoiceIndex++;
        }
        System.out.println();
        System.out.println("What do you want to do?");
        int choice = chooseOption(false, options);
        switch (choice) {
            case 1 -> {
                if (!this.gameView.isCurrentPlayer(this.username)) {
                    Printer.printError("Wait for your turn");
                    break;
                }
                System.out.println();
                System.out.println("BOARD:");
                printBoard();
                System.out.println();
                System.out.println("YOUR FIELD:");
                System.out.println();
                this.numToCoordinates = GamePrinter.printField(this.gameView.getPlayer(this.username).getField());
                System.out.println();
                System.out.println("YOUR HAND:");
                printHand();
                playTurn();
            }
            case 2 -> printBoard();
            case 3 -> {
                for (PlayerView player : this.gameView.getSortedPlayers()) {
                    System.out.println(player.getUsername() + ": " + player.getScore() + " points");
                }
            }
            case 4 -> {
                System.out.println();
                GamePrinter.printField(this.gameView.getPlayer(this.username).getField());
            }
            case 5 -> printHand();
            default -> printOpponent(this.gameView.getPlayer(choiceToUsername.get(choice)));
        }
    }

    private void printBoard() {
        System.out.println();
        GamePrinter.printBoard(this.gameView.getBoard());
        System.out.println();
        for (ObjectiveCard obj : this.gameView.getObjectives()) {
            System.out.println("Common objective: " + GamePrinter.getObjectiveDescription(obj));
        }
    }

    private void printHand() {
        System.out.println();
        GamePrinter.printHand(this.gameView.getPlayer(this.username).getHand(), false);
        System.out.println();
        System.out.println("Private objective: " + GamePrinter.getObjectiveDescription(this.gameView.getPlayer(this.username).getObjective()));
    }

    private void printOpponent(PlayerView player) {
        System.out.println();
        GamePrinter.printField(player.getField());
        System.out.println();
        GamePrinter.printHand(player.getHand(), true);
    }

    private int chooseOption(boolean hasAngle, String... options) {
        return chooseOption(hasAngle, Arrays.asList(options));
    }

    private int chooseOption(boolean hasAngle, List<String> options) {
        for (int i = 0; i < options.size(); i++) {
            int num = i + 1;
            System.out.println(num + ") " + options.get(i));
        }
        if (hasAngle) {
            System.out.print("> ");
        }
        return readInt(1, options.size(), hasAngle);
    }

    private int chooseInRange(String prompt, int min, int max) {
        System.out.print(prompt + " [" + min + "-" + max + "]: ");
        return readInt(min, max, true);
    }

    private int readInt(boolean hasAngleOnError) {
        while (true) {
            try {
                int choice = this.scanner.nextInt();
                // flush
                this.scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                this.scanner.nextLine();
                Printer.printError("You should type a number");
                if (hasAngleOnError) {
                    System.out.print("> ");
                }
            }
        }
    }

    private int readInt(int min, int max, boolean hasAngleOnError) {
        int choice = max + 1;
        boolean firstIter = true;
        while (choice < min || choice > max) {
            if (!firstIter) {
                Printer.printError("You should type a number between " + min + " and " + max);
                if (hasAngleOnError) {
                    System.out.print("> ");
                }
            }
            choice = readInt(hasAngleOnError);
            firstIter = false;
        }
        return choice;
    }
}