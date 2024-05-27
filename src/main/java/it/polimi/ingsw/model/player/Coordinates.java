package it.polimi.ingsw.model.player;

import java.io.Serializable;

/**
 * Coordinates record used to locate cards in the field.
 *
 * @param x x coordinate
 * @param y y coordinate
 */
public record Coordinates(int x, int y) implements Serializable { }
