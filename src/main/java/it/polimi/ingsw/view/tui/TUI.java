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
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.utilities.Pair;
import it.polimi.ingsw.utilities.Printer;
import it.polimi.ingsw.view.View;
import javafx.stage.Stage;

import java.util.*;

public class TUI extends View {
    private final Scanner scanner;
    private Map<Integer, Coordinates> numToCoordinates;

    public TUI() {
        super();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.exit(1);
    }

    public void run() {
        printTitle();
        connectUsername();
        createOrJoinGame();

        Thread mainThread = new Thread(() -> {
            runStarterPhase();
            runObjectivePhase();
            while (true) {
                chooseAction();
            }
        });
        mainThread.start();

        Thread updateThread = new Thread(() -> {
            while (true) {
                ServerToClientMessage message = waitForMessage();

                switch (message) {
                    case ViewUpdateMessage m -> {
                        this.gameView = m.getGameView();
                        Printer.printInfo(m.getMessage());
                        if (this.gameView.isEnded()) {
                            printLeaderboard(true);
                            System.exit(0);
                        }
                        if (this.gameView.isCurrentPlayer(this.username)) {
                            Printer.printInfo("It's your turn");
                        }
                    }
                    case LobbyMessage m -> {
                        Printer.printInfo(m.getMessage());
                        printLobbyUsernames(m.getSize(), m.getUsernames());
                    }
                    case null, default -> addMessage(message);
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
                Printer.printInfo(r.getMessage());
                printLobbyUsernames(r.getSize(), r.getUsernames());
            }
            else if (response instanceof LobbyNotValidMessage r) {
                Printer.printError(r.getMessage());
                chooseCreateOrJoin();
            }
            else if (response instanceof ViewUpdateMessage r) {
                this.gameView = r.getGameView();
                Printer.printInfo(r.getMessage());
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

    private void printLobbyUsernames(int size, List<String> usernames) {
        System.out.print("Players connected [" + usernames.size() + "/" + size + "]: ");
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

    private void runStarterPhase() {
        chooseMarker();

        while (true) {
            ServerToClientMessage response = waitForMessage();

            if (response instanceof ChooseMarkerAckMessage) {
                printBoard(true, false);
                chooseStarter();
                System.out.println();
                System.out.println("Waiting for the other players to choose their starter card...");
            }
            else if (response instanceof ChooseMarkerErrorMessage) {
                Printer.printError("This marker has already been taken");
                chooseMarker();
            }
            else if (response instanceof StarterPhaseEndedMessage) {
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

    private void runObjectivePhase() {
        printBoard(true, false);
        printField(true);
        printHand(true, false);
        chooseObjective();

        while (true) {
            ServerToClientMessage response = waitForMessage();

            if (response instanceof ChooseObjectiveAckMessage) {
                System.out.println();
                System.out.println("Waiting for the other players to choose their private objective...");
            }
            else if (response instanceof ChooseObjectiveErrorMessage) {
                //todo this error should never happen
                Printer.printError("You cannot choose this objective");
                chooseObjective();
            }
            else if (response instanceof ObjectivePhaseEndedMessage) {
                break;
            }
            else {
                addMessage(response);
            }
        }
    }

    private void chooseObjective() {
        System.out.println();
        System.out.println("Choose one of the following private objectives:");
        List<ObjectiveCard> objectives = this.gameView.getPlayer(this.username).getObjectiveOptions();
        List<Pair<ObjectiveCard, String>> objectivesWithPrompt = new ArrayList<>();
        for (int i = 0; i < objectives.size(); i++) {
            objectivesWithPrompt.add(new Pair<>(objectives.get(i), (i + 1) + ") "));
        }
        if (!objectivesWithPrompt.isEmpty()) {
            System.out.println();
            GamePrinter.printObjectives(objectivesWithPrompt);
        }
        System.out.print("> ");
        int choice = readInt(1, objectives.size(), true);
        notifyAllListeners(new ChooseObjectiveMessage(this.username, objectives.get(choice - 1)));
    }

    private void playTurn() {
        playCard();

        while (true) {
            ServerToClientMessage response = waitForMessage();

            if (response instanceof PlayCardAckMessage) {
                printBoard(true, true);
                if (gameView.isLastRound()) {
                    break;
                }
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
        List<PlayableCard> hand = this.gameView.getPlayer(this.username).getHand();
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
        int choice = chooseInRange("Which card do you want to draw", 1, 6);
        DrawType type = switch (choice) {
            case 1 -> DrawType.GOLD;
            case 4 -> DrawType.RESOURCE;
            default -> DrawType.VISIBLE;
        };
        PlayableCard card = switch (choice) {
            case 2 -> this.gameView.getBoard().getVisibleCards()[0];
            case 3 -> this.gameView.getBoard().getVisibleCards()[1];
            case 5 -> this.gameView.getBoard().getVisibleCards()[2];
            case 6 -> this.gameView.getBoard().getVisibleCards()[3];
            default -> null;
        };
        notifyAllListeners(new DrawCardMessage(this.username, type, card));
    }

    private void chooseAction() {
        List<String> options = new ArrayList<>(Arrays.asList(
                "Play turn", "Print my field and hand", "Print board"
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
                    if (gameView.getCurrentPlayer().isPresent()) {
                        Printer.printError("Wait for " + gameView.getCurrentPlayer().get().getUsername() + "'s turn");
                    } else {
                        Printer.printError("Wait for your turn");
                    }
                    break;
                }
                printLeaderboard(true);
                printBoard(true, false);
                printField(true);
                printHand(true, true);
                playTurn();
            }
            case 2 -> {
                printField(false);
                printHand(false, false);
            }
            case 3 -> {
                printLeaderboard(false);
                printBoard(false, false);
            }
            default -> printOpponent(this.gameView.getPlayer(choiceToUsername.get(choice)));
        }
    }

    private void printBoard(boolean hasTitle, boolean hasOptions) {
        System.out.println();
        if (hasTitle) {
            System.out.println("BOARD:");
        }
        if (hasOptions) {
            System.out.println(" 1              2              3");
        }
        GamePrinter.printBoard(this.gameView.getBoard());
        if (hasOptions) {
            System.out.println(" 4              5              6");
        }
        List<Pair<ObjectiveCard, String>> objectivesWithPrompt = new ArrayList<>();
        for (ObjectiveCard obj : this.gameView.getObjectives()) {
            objectivesWithPrompt.add(new Pair<>(obj, "Common objective: "));
        }
        if (!objectivesWithPrompt.isEmpty()) {
            System.out.println();
            GamePrinter.printObjectives(objectivesWithPrompt);
        }
    }

    private void printLeaderboard(boolean hasTitle) {
        System.out.println();
        if (hasTitle) {
            System.out.println("LEADERBOARD:");
        }
        for (PlayerView player : this.gameView.getSortedPlayers()) {
            String isYou = player.getUsername().equals(this.username) ? " (you)" : "";
            String coloredUsername = player.getMarker().isPresent()
                    ? player.getMarker().get().getConsoleColor() + player.getUsername() + isYou + Printer.RESET
                    : player.getUsername() + isYou;
            System.out.println(coloredUsername + ": " + Config.pluralize(player.getTotalScore(), "point")
                    + " (" + player.getObjectiveScore() + " from objectives)"
            );
        }
    }

    private void printField(boolean hasTitle) {
        System.out.println();
        if (hasTitle) {
            System.out.println("YOUR FIELD:");
        }
        this.numToCoordinates = GamePrinter.printField(this.gameView.getPlayer(this.username).getField());
    }

    private void printHand(boolean hasTitle, boolean hasOptions) {
        System.out.println();
        if (hasTitle) {
            System.out.println("YOUR HAND:");
        }
        if (hasOptions) {
            System.out.println(" 1              2              3");
        }
        GamePrinter.printHand(this.gameView.getPlayer(this.username).getHand(), false);
        if (this.gameView.getPlayer(this.username).getObjective().isPresent()) {
            System.out.println();
            GamePrinter.printObjective(
                    this.gameView.getPlayer(this.username).getObjective().get(),
                    "Private objective: "
            );
        }
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