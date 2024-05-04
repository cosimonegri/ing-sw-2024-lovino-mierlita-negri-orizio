package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.MainController;

public class UsernameMessage implements Message {
    private final String username;

    public UsernameMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void execute(MainController controller) { }
}
