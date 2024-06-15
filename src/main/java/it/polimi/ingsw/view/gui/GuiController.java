package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.CornerType;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.player.PlacedCard;
import it.polimi.ingsw.modelView.BoardView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public Pane fieldPane;
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
    private ImageView starterCard;
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
    private Image selectedCard;
    private String selectedCardPath;

    public AnchorPane getBoard() {
        return board;
    }

    @FXML
    public void initialize(){
        //todo messages text: for player messages gray background and blue text
        // red text for errors
        // background field and board beige
        topLeftText.setBackground(new Background(new BackgroundFill(Color.valueOf("#00ff00"), null, null)));
        topRightText.setBackground(new Background(new BackgroundFill(Color.valueOf("#00ff00"), null, null)));
        playerMessages.setBackground(new Background(new BackgroundFill(Color.valueOf("#7FFFD4"), null, null)));
        selectedCard = null;
        loadScoreBoard();
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

    private boolean assurePlayable(){
        boolean playable = false;

        return playable;
    }

    public void newPlayablePositionsFromCard(PlacedCard card){
        ImageView imageView = null;
        for(Node n : fieldPane.getChildren()){
            if( n instanceof ImageView i) {
                if(i.getImage().getUrl().equals(translateToPath(card.card().getId(), card.flipped()))){
                    imageView = i;
                }
            }
        }
        if(imageView == null) throw new RuntimeException("Card not found in field");//todo manage this better

        boolean flipped = card.flipped();
        if(card.card().getId() > 80 && card.card().getId() < 87)
            flipped = !card.flipped();

        //TopLeft
        if(card.card().getCorner(Position.TOPLEFT, flipped).type().equals(CornerType.VISIBLE)){
            System.out.println("TL");
            ImageView i = new ImageView();
            setCardImage(i, "file:src/main/resources/images/card_fronts/9.jpg");
            fieldPane.getChildren().add(i);
            i.setOpacity(0.4);
            i.setLayoutX(imageView.getLayoutX() - 122);
            i.setLayoutY(imageView.getLayoutY() - 65);
        }
        //TopRight
        if(card.card().getCorner(Position.TOPRIGHT, flipped).type().equals(CornerType.VISIBLE)){
            System.out.println("TR");
            ImageView i = new ImageView();
            setCardImage(i, "file:src/main/resources/images/card_fronts/9.jpg");
            fieldPane.getChildren().add(i);
            i.setOpacity(0.4);
            i.setLayoutX(imageView.getLayoutX() + 122);
            i.setLayoutY(imageView.getLayoutY() - 65);
        }
        //BottomLeft
        if(card.card().getCorner(Position.BOTTOMLEFT, flipped).type().equals(CornerType.VISIBLE)){
            System.out.println("BL");
            ImageView i = new ImageView();
            setCardImage(i, "file:src/main/resources/images/card_fronts/9.jpg");
            fieldPane.getChildren().add(i);
            i.setOpacity(0.4);
            i.setLayoutX(imageView.getLayoutX() - 122);
            i.setLayoutY(imageView.getLayoutY() + 65);
        }
        //BottomRight
        if(card.card().getCorner(Position.BOTTOMRIGHT, flipped).type().equals(CornerType.VISIBLE)){
            System.out.println("BR");
            ImageView i = new ImageView();
            setCardImage(i, "file:src/main/resources/images/card_fronts/9.jpg");
            fieldPane.getChildren().add(i);
            i.setOpacity(0.4);
            i.setLayoutX(imageView.getLayoutX() + 122);
            i.setLayoutY(imageView.getLayoutY() + 65);
        }

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
        String path = "file:src/main/resources/images/" + ((starterFlipped) ? "card_backs/" : "card_fronts/");
        setCardImage(starterCard, path + starterId + ".jpg");
    }
}
