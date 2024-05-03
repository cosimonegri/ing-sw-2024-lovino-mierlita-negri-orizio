package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.CreateGameMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.utilities.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class View {
    private final Scanner scanner;
    private final List<ViewListener> listeners;
    private String username;

    public View() {
        this.scanner = new Scanner(System.in);
        this.listeners = new ArrayList<>();
    }

    public void addListener(ViewListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ViewListener listener) {
        this.listeners.add(listener);
    }

    private void notifyAllListeners(Message message) {
        for (ViewListener listener : this.listeners) {
            listener.updateFromView(message);
        }
    }

    public void run() {
        chooseUsername();
        chooseCreateOrJoin();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {}
    }

    private void chooseUsername() {
        System.out.println("Choose a username: (it can only contain alphanumeric characters and _)");
        String username;
        do {
            username = scanner.nextLine();
        } while (!Config.isUsernameValid(username));
        this.username = username;
    }

    private void chooseCreateOrJoin() {
        System.out.println("What do you want to do?");
        System.out.println("1) Create a new game");
        System.out.println("2) Join a game");
        int typeChoice = getIntChoice(1, 2);
        if (typeChoice == 1) {
            System.out.println("How many players do you want in the lobby? (between 2 and 4)");
            int playersChoice = getIntChoice(2, 4);
            notifyAllListeners(new CreateGameMessage(this.username, playersChoice));
        } else {
            System.out.println("Type the ID of an existing game:");
            int gameChoice = scanner.nextInt();
        }
    }

    private int getIntChoice(int min, int max) {
        int choice;
        do {
            choice = scanner.nextInt();
        } while (choice < min || choice > max);
        return choice;
    }
}
