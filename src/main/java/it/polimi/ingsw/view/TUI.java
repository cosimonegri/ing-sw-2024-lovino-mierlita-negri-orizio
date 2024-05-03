package it.polimi.ingsw.view;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.message.CreateGameMessage;
import it.polimi.ingsw.network.message.JoinMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.utilities.Config;

import java.util.Scanner;

public class TUI extends View {
    private Scanner scanner;
    private String username;


    public TUI(){
        super();
        this.scanner = new Scanner(System.in);
    }

    public void run(){
        chooseUsername();
        chooseCreateOrJoin();
    }

    public void update(Message message){

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
            super.notifyAllListeners(new CreateGameMessage(this.username, playersChoice));
        } else {
            System.out.println("Type the ID of an existing game:");
            int gameId = getGameID();
            super.notifyAllListeners(new JoinMessage(this.username, gameId));
        }
    }

    // TODO: change gameID to String and check length
    private int getGameID() {
        int gameId;
        gameId = scanner.nextInt();
        return gameId;
    }

    private int getIntChoice(int min, int max) {
        int choice;
        do {
            choice = scanner.nextInt();
        } while (choice < min || choice > max);
        return choice;
    }
}