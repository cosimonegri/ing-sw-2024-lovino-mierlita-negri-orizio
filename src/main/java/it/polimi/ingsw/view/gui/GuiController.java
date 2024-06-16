package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.modelView.BoardView;
import it.polimi.ingsw.modelView.FieldView;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.List;

public class GuiController{
    @FXML
    public Label playersTurn;
    @FXML
    public Label playerMessages;
    @FXML
    public AnchorPane board;
    @FXML
    public TabPane tabFieldsPane;
    @FXML
    public ScrollPane boardScrollpane;
    @FXML
    public GridPane gridPane;
    @FXML
    public AnchorPane fieldAnchor;
    @FXML
    public Tab myField;
    @FXML
    public ScrollPane playerFieldScrollpane;
    @FXML
    public GridPane gridPanePlayer;
    @FXML
    public Pane topLeftText;
    @FXML
    public Label lobbyID;
    @FXML
    public Pane topRightText;
    @FXML
    public Label turnNum;
    @FXML
    private ImageView cardInHand1;
    @FXML
    private ImageView cardInHand2;
    @FXML
    private ImageView cardInHand3;
    @FXML
    private ImageView privateObjective;
    @FXML
    private ImageView goldDeck;
    @FXML
    private ImageView resourceDeck;
    @FXML
    private ImageView visibleGold1;
    @FXML
    private ImageView visibleGold2;
    @FXML
    private ImageView visibleResource1;
    @FXML
    private ImageView visibleResource2;
    @FXML
    private ImageView publicObjective1;
    @FXML
    private ImageView publicObjective2;
    @FXML
    public ImageView publicObjective11;
    @FXML
    public ImageView publicObjective21;
    @FXML
    public ImageView scoreBoard;
    @FXML
    public Label myUserName;

    private Image selectedCard;
    private String selectedCardPath;
    private GridPane gridFieldPane;
    private final int numColumns = 83;
    private final int numRows = 83;

    public AnchorPane getBoard() {
        return board;
    }

    @FXML
    public void initialize(){
        //todo messages text: for player messages red text for errors
        topLeftText.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, null, null)));
        topRightText.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, null, null)));
        playerMessages.setBackground(new Background(new BackgroundFill(Color.valueOf("#A0A0A0"), null, null)));
        playerMessages.setTextFill(Color.BLUE);
        fieldAnchor.setBackground(new Background(new BackgroundFill(Color.WHEAT, null, null)));
        board.setBackground(new Background(new BackgroundFill(Color.WHEAT, null, null)));
        selectedCard = null;
        loadScoreBoard();

        gridFieldPane = new GridPane();
        //added more rows and columns for margins

        for (int i = 0; i < numColumns; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(121);
            gridFieldPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(64);
            gridFieldPane.getRowConstraints().add(rowConst);
        }
        fieldAnchor.getChildren().add(gridFieldPane);
        AnchorPane.setTopAnchor(gridFieldPane, 0.0);
        AnchorPane.setRightAnchor(gridFieldPane, 0.0);
        AnchorPane.setBottomAnchor(gridFieldPane, 0.0);
        AnchorPane.setLeftAnchor(gridFieldPane, 0.0);

        //initialize effects
        cardInHand1.setEffect(new DropShadow());
        cardInHand2.setEffect(new DropShadow());
        cardInHand3.setEffect(new DropShadow());
        privateObjective.setEffect(new DropShadow());

        publicObjective1.setEffect(new DropShadow());
        publicObjective2.setEffect(new DropShadow());
        goldDeck.setEffect(new DropShadow());
        resourceDeck.setEffect(new DropShadow());
        visibleGold1.setEffect(new DropShadow());
        visibleGold2.setEffect(new DropShadow());
        visibleResource1.setEffect(new DropShadow());
        visibleResource2.setEffect(new DropShadow());
    }

    public void loadScoreBoard(){
        Image i = new Image("file:src/main/resources/images/PLATEAU-SCORE-IMP/plateau.jpg");
        scoreBoard.setImage(i);
        scoreBoard.setFitHeight(431);
        scoreBoard.setFitWidth(265);
        Rectangle2D cut = new Rectangle2D(40.0, 40.0, 480.0, 945.0);
        scoreBoard.setViewport(cut);
        scoreBoard.visibleProperty().setValue(false);
    }

    private void setMarkersOnPlateau(/*playerView list*/){
        //coords of top right in circle of points
        int dx = 50, xOf0 = 133, xOf6 = 108, xOf20 = 183, xOf26 = 229;
        int dy = 46, yOf0 = 402, yOf20 = 151, yOf25 = 37, yOf26 = 45, yOf29 = 92;
        //coords modifiers inside of circle
        int ddx = 20, ddy = 20;
        //todo foreach with player views to determine marker position via points and markers
    }

    private static String translateToPath(int id, boolean flipped){
        if(!flipped) {
            return "file:src/main/resources/images/card_fronts/" + id + ".jpg";
        } else {
            return "file:src/main/resources/images/card_backs/" + id + ".jpg";
        }
    }

    private int translateToID(String url){
        return Integer.parseInt(url.replaceAll("[^0-9]",""));
    }

    public void newPlayablePositionsFromCard(FieldView fieldView){
        for(int y = fieldView.getBottomRightBound().y(); y <= fieldView.getTopLeftBound().y(); y++) {
            for(int x = fieldView.getTopLeftBound().x(); x <= fieldView.getBottomRightBound().x(); x++) {
                Coordinates cell = new Coordinates(x, y);
                if(fieldView.getPlacedCard(cell) == null) {
                    if(fieldView.getAllValidCoords().contains(cell)) {
                        Node node = getNodeFromGridPane(x + 1,numRows - 1 - y - 1);
                        if(node == null) {
                            if(x != 0 && x != 80 && y != 0 && y != 80){
                                if(y % 2 == 1){
                                    if(x % 2 == 1) {
                                       gridFieldPane.add(playableCell(), x + 1, numRows - 1 - y - 1);
                                    }
                                } else {
                                    if(x % 2 == 0) {
                                        gridFieldPane.add(playableCell(), x + 1, numRows - 1 - y - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Rectangle playableCell() {
        Rectangle playableCell = new Rectangle(0,0,159,110);
        playableCell.setFill(Color.AQUA);
        playableCell.setOpacity(0.2);
        return playableCell;
    }

    private Node getNodeFromGridPane(int col, int row) {
        for (Node node : gridFieldPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public void setPersonalObjective(int id) {
        setCardImage(privateObjective, translateToPath(id, false));
    }

    public void setBoard(BoardView bv) {
        setCardImage(goldDeck, translateToPath(bv.getGoldTopCard().getId(), true));
        setCardImage(resourceDeck, translateToPath(bv.getResourceTopCard().getId(), true));
        setCardImage(visibleGold1, translateToPath(bv.getVisibleCards()[0].getId(), false));
        setCardImage(visibleGold2, translateToPath(bv.getVisibleCards()[1].getId(), false));
        setCardImage(visibleResource1, translateToPath(bv.getVisibleCards()[2].getId(), false));
        setCardImage(visibleResource2, translateToPath(bv.getVisibleCards()[3].getId(), false));
    }

    public void setPublicObjective(List<ObjectiveCard> oc) {
        setCardImage(publicObjective1, translateToPath(oc.get(0).getId(), false));
        setCardImage(publicObjective2, translateToPath(oc.get(1).getId(), false));
        setCardImage(publicObjective11, "file:src/main/resources/images/card_backs/100.jpg");
        setCardImage(publicObjective21, "file:src/main/resources/images/card_backs/100.jpg");
    }

    public void setHand(int card1, int card2, int card3, int objective){
        setCardImage(cardInHand1, translateToPath(card1, false));
        setCardImage(cardInHand2, translateToPath(card2, false));
        setCardImage(cardInHand3, translateToPath(card3, false));
        setCardImage(privateObjective, translateToPath(objective, false));
    }

    public static void setCardImage(ImageView imageView, String s) {
        Image image = new Image(s);
        imageView.setImage(image);
        imageView.setFitHeight(110);
        imageView.setFitWidth(159);
        Rectangle2D cut = new Rectangle2D(75.0, 75.0, 875.0, 605.0);
        imageView.setViewport(cut);
    }

    public void changeImage(MouseEvent e) {
        if( selectedCard != null && e.getSource() instanceof ImageView imageView) {
            imageView.setImage(selectedCard);
            imageView.setImage(selectedCard);
            selectedCard = null;
        }
    }

    public void selectCard(MouseEvent e) {
        if(e.getSource() instanceof ImageView imageView) {
            selectedCard = imageView.getImage();
            selectedCardPath = imageView.getImage().getUrl();
        }
    }

    public void lightUp(MouseEvent e) {
        if(e.getSource() instanceof ImageView imageView) {
            Glow glow = new Glow();
            DropShadow blueDropShadow = new DropShadow(10, Color.BLUE);
            glow.setInput(blueDropShadow);
            imageView.setEffect(glow);
        }
    }
    public void lightOff(MouseEvent e) {
        if(e.getSource() instanceof ImageView imageView) {
            Glow glow = new Glow(0);
            DropShadow dropShadow = new DropShadow();
            glow.setInput(dropShadow);
            imageView.setEffect(glow);
        }
    }

    public void setStarter(int starterId, boolean starterFlipped) {
        String path = "file:src/main/resources/images/" + ((!starterFlipped) ? "card_backs/" : "card_fronts/");
        ImageView starterCard = new ImageView();
        setCardImage(starterCard, path + starterId + ".jpg");
        gridFieldPane.add(starterCard,41,41);
    }
}
