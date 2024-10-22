package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.PlacedCard;
import it.polimi.ingsw.modelView.FieldView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


import java.util.List;

import static it.polimi.ingsw.view.gui.GuiController.*;

public class OtherPlayerGuiController {
    @FXML
    private GridPane otherGridPanePlayer;
    @FXML
    private AnchorPane otherFieldAnchor;
    @FXML
    private ImageView otherCardInHand1;
    @FXML
    private ImageView otherCardInHand2;
    @FXML
    private ImageView otherCardInHand3;
    @FXML
    private ImageView otherPrivateObjective;

    /**
     * The gridPane containing the player's field in it
     */
    private GridPane otherGridFieldPane;
    /**
     * The height of the gridPane, it's two units longer to get some more margin near the edges, these are never populated
     */
    private final int numColumns = 83;
    /**
     * The length of the gridPane, it's two units longer to get some more margin near the edges, these are never populated
     */
    private final int numRows = 83;

    /**
     * Initializes the grid containing the field ande the colors, positions and effects of the other elements
     */
    @FXML
    public void initialize() {
        otherFieldAnchor.setBackground(new Background(new BackgroundFill(Color.WHEAT, null, null)));
        otherGridPanePlayer.setBackground(new Background(new BackgroundFill(Color.WHEAT, null, null)));
        otherGridFieldPane = new GridPane();
        //added more rows and columns for margins

        for (int i = 0; i < numColumns; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(121);
            otherGridFieldPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(64);
            otherGridFieldPane.getRowConstraints().add(rowConst);
        }
        otherFieldAnchor.getChildren().add(otherGridFieldPane);
        AnchorPane.setTopAnchor(otherGridFieldPane, 0.0);
        AnchorPane.setRightAnchor(otherGridFieldPane, 0.0);
        AnchorPane.setBottomAnchor(otherGridFieldPane, 0.0);
        AnchorPane.setLeftAnchor(otherGridFieldPane, 0.0);

        initializeEffects();
    }

    /**
     * Updates the player's hand and draw the last card placed on his field
     * @param fieldView contains the updated fieldView
     * @param hand contains the updated hand of the playerView
     */
    protected void updateOtherPlayer(FieldView fieldView, List<PlayableCard> hand) {
        //update field
        int placementIndex = fieldView.getCardsCount() - 1;
        Coordinates coords = fieldView.getCoords(placementIndex);
        PlacedCard placedCard = fieldView.getPlacedCard(coords);

        ImageView imageView = new ImageView();
        if (placementIndex == 0) {
            setCardImage(imageView, translateToPath(placedCard.card().getId(), !placedCard.flipped()));
        } else {
            setCardImage(imageView, translateToPath(placedCard.card().getId(), placedCard.flipped()));
        }
        otherGridFieldPane.add(imageView, coords.x() + 1, numRows - 2 - coords.y());
        //update hand
        setHand(hand);
    }

    /**
     * Sets the player's hand with the cards in the list
     * @param hand list containing the cards to be put, max 3
     */
    protected void setHand(List<PlayableCard> hand){
        setCardImage(otherCardInHand1, translateToPath(hand.getFirst().getId(), true));
        setCardImage(otherCardInHand2, translateToPath(hand.get(1).getId(), true));
        if(hand.size() == 3) {
            setCardImage(otherCardInHand3, translateToPath(hand.get(2).getId(), true));
        } else {
            otherCardInHand3.setImage(null);
        }
    }

    /**
     * Sets the starter card on the player's field
     * @param starterId the id of the starter card
     * @param starterFlipped the flipped attribute of the card
     */
    protected void setStarter(int starterId, boolean starterFlipped) {
        String path = "images/" + ((!starterFlipped) ? "card_backs/" : "card_fronts/");
        ImageView starterCard = new ImageView();
        setCardImage(starterCard, path + starterId + ".jpg");
        otherGridFieldPane.add(starterCard,41,41);
    }

    protected void setMarkerOnField(Marker playersMarker) {
        Circle circle = new Circle(7.0,
                GuiController.getMarkerColor(playersMarker));
        otherFieldAnchor.getChildren().add(circle);
        circle.setLayoutX(5013.0);
        circle.setLayoutY(2625.0);
    }

    /**
     * Initializes the effects of the cards in hand
     */
    private void initializeEffects() {
        otherCardInHand1.setEffect(new DropShadow());
        otherCardInHand2.setEffect(new DropShadow());
        otherCardInHand3.setEffect(new DropShadow());
        otherPrivateObjective.setEffect(new DropShadow());
    }

    /**
     * @param col of the requested card
     * @param row of the requested card
     * @return the node ad the given position or null if there is no card
     */
    private Node getNodeFromGridPane(int col, int row) {
        for (Node node : otherGridFieldPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
