package it.polimi.ingsw.network.message.servertoclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LobbyMessage implements ServerToClientMessage {
    private final List<String> usernames;

    public LobbyMessage(List<String> usernames) {
        this.usernames = new ArrayList<>(usernames);
    }

    public List<String> getUsernames() {
        return Collections.unmodifiableList(this.usernames);
    }
}
