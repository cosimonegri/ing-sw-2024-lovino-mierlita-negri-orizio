package it.polimi.ingsw.modelView.cardView;

import it.polimi.ingsw.model.deck.card.Card;

import java.io.Serializable;

public abstract class CardView implements Serializable {
     private int id;
     public CardView (){}
     public int getId(){
         return this.getId();
     }
}
