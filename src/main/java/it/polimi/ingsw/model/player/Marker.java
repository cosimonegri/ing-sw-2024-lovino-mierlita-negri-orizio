package it.polimi.ingsw.model.player;

import it.polimi.ingsw.utilities.Printer;

/**
 *  Colored token for each player
 */
public enum Marker {
    BLUE,
    GREEN,
    RED,
    YELLOW;

    public String getConsoleColor() {
        switch (this) {
            case BLUE -> {
                return Printer.BLUE;
            }
            case GREEN -> {
                return Printer.GREEN;
            }
            case RED -> {
                return Printer.RED;
            }
            default -> {
                return Printer.YELLOW;
            }
        }
    }
}
