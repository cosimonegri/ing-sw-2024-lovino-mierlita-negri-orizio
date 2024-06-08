package it.polimi.ingsw.utilities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is a POJO class used to parse a single move on the field, it contains the id of the played Card, its coordinates(x,y)
 * and its orientation
 */
public class Move {


    int id;
    int x;
    int y;
    boolean flipped;

    @JsonCreator
    public Move(@JsonProperty("id") int id,
                @JsonProperty ("x")int  x,
                @JsonProperty("y") int y,
                @JsonProperty("flipped") boolean flipped){
        this.id = id;
        this.x = x;
        this.y = y;
        this.flipped = flipped;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isFlipped() {
        return flipped;
    }

}