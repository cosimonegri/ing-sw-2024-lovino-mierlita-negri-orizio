package it.polimi.ingsw.utilities;

public class Printer {
    public static final String RESET = "\u001B[0m";

    public static final String CONSOLE_ERROR = "\u001b[38;2;247;84;100m";
    public static final String CYAN = "\u001B[36m";

    public static final String RED = "\u001b[38;2;170;15;0m";
    public static final String GREEN = "\u001b[38;2;14;125;0m";
    public static final String BLUE = "\u001b[38;2;0;76;120m";
    public static final String PURPLE = "\u001b[38;2;108;0;130m";
    public static final String YELLOW = "\033[33m";
    public static final String BLACK = "\u001B[30m";
    public static final String BEIGE = "\u001b[38;2;168;144;128m";

    public static final String RED_BACKGROUND = "\u001b[48;2;195;60;50m";
    public static final String GREEN_BACKGROUND = "\u001b[48;2;66;189;53m";
    public static final String BLUE_BACKGROUND = "\u001b[48;2;78;147;186m";
    public static final String PURPLE_BACKGROUND = "\u001b[48;2;173;80;191m";
    public static final String YELLOW_BACKGROUND = "\033[0;103m";
    public static final String WHITE_BACKGROUND = "\u001b[48;2;190;190;190m";
    public static final String GRAY_BACKGROUND = "\u001b[48;2;120;120;120m";

    public static void printError(String string) {
        System.out.println(CONSOLE_ERROR + string + RESET);
    }

    public static void printInfo(String string) {
        System.out.println(CYAN + string + RESET);
    }
}
