package it.polimi.ingsw.utilities;

public class Config {
    public static final String HOSTNAME = "127.0.0.1";
    public static final int SOCKET_PORT = 6666;
    public static final int RMI_PORT = 1099;
    public static final String RMI_NAME = "CodexNaturalisServer-PSP33";

    public static final int MIN_GAME_ID = 0;
    public static final int MAX_GAME_ID = 999999;

    public static boolean isUsernameValid(String username) {
        return !username.isEmpty() && !username.isBlank() && username.matches("[a-zA-Z_0-9]*");
    }
}
