package it.polimi.ingsw.network.message.servertoclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LobbyMessage implements ServerToClientMessage {
    private final List<String> usernames;
    private final String message;

    public LobbyMessage(List<String> usernames, String message) {
        this.usernames = new ArrayList<>(usernames);
        this.message = message;
    }

    public List<String> getUsernames() {
        return Collections.unmodifiableList(this.usernames);
    }

    public String getMessage() {
        return this.message;
    }
}
