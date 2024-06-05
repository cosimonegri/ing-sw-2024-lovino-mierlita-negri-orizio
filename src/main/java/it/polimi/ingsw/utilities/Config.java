package it.polimi.ingsw.utilities;

public class Config {
    public static final String DEFAULT_IP = "127.0.0.1";
    public static final int SOCKET_PORT = 6666;
    public static final int RMI_PORT = 1099;
    public static final String RMI_NAME = "CodexNaturalisServer-PSP33";


    public static final int PING_TIME_MS = 5000;

    public static final int MIN_GAME_ID = 0;
    public static final int MAX_GAME_ID = 999999;

    public static final int SCORE_FOR_FINAL_PHASE = 20;

    public static boolean isUsernameValid(String username) {
        return !username.isEmpty() && !username.isBlank() && username.matches("[a-zA-Z_0-9]*");
    }

    public static String pluralize(int count, String word) {
        if (count == 1) {
            return count + " " + word;
        }
        return count + " " + word + "s";
    }
}
