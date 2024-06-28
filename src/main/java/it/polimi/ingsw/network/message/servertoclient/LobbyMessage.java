package it.polimi.ingsw.network.message.servertoclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Message sent to the client when the action of joining a game has been successful,
 * or when someone joins or leaves the lobby during the waiting phase.
 */
public class LobbyMessage implements ServerToClientMessage {
    /**
     * Maximum size of the lobby.
     */
    private final int size;
    /**
     * Username of the players in the lobby.
     */
    private final List<String> usernames;
    /**
     * Message that describes what happened.
     */
    private final String message;

    public LobbyMessage(int size, List<String> usernames, String message) {
        this.size = size;
        this.usernames = new ArrayList<>(usernames);
        this.message = message;
    }

    public int getSize() {
        return this.size;
    }

    public List<String> getUsernames() {
        return Collections.unmodifiableList(this.usernames);
    }

    public String getMessage() {
        return this.message;
    }
}
