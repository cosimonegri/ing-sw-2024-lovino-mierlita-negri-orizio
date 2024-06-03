package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
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

import java.awt.*;
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
    private Image selectedCard;
    private String selectedCardPath;

    @FXML
    public void initialize(){
        //todo messages text: for player messages gray background and blue text
        // red text for errors
        // background field and board beige
        topLeftText.setBackground(new Background(new BackgroundFill(Color.valueOf("#00ff00"), null, null)));
        topRightText.setBackground(new Background(new BackgroundFill(Color.valueOf("#00ff00"), null, null)));
        playerMessages.setBackground(new Background(new BackgroundFill(Color.valueOf("#7FFFD4"), null, null)));

        setCardImage(starterCard, "file:src/main/resources/images/card_backs/85.jpg");
        selectedCard = null;
        newPlayablePositions(starterCard);
        cardInHand1.setEffect(new DropShadow());
        cardInHand2.setEffect(new DropShadow());
        cardInHand3.setEffect(new DropShadow());
        goldDeck.setEffect(new DropShadow());
        resourceDeck.setEffect(new DropShadow());
        visibleGold1.setEffect(new DropShadow());
        visibleGold2.setEffect(new DropShadow());
        visibleResource1.setEffect(new DropShadow());
        visibleResource2.setEffect(new DropShadow());
        privateObjective.setEffect(new DropShadow());
    }

    private String translateToPath(int id, boolean flipped){
        if(!flipped) {
            return "file:src/main/resources/images/card_fronts/" + id + ".jpg";
        } else {
            return "file:src/main/resources/images/card_backs/" + id + ".jpg";
        }
    }

    private void newPlayablePositions(/*cardView, */ImageView imageView){
        //TopLeft
        if(true){
            ImageView i = new ImageView();
            setCardImage(i, "file:src/main/resources/images/card_fronts/9.jpg");
            fieldPane.getChildren().add(i);
            i.setOpacity(0.4);
            i.setLayoutX(imageView.getLayoutX() - 122);
            i.setLayoutY(imageView.getLayoutY() - 65);
        }
        //TopRight
        if(true){
            ImageView i = new ImageView();
            setCardImage(i, "file:src/main/resources/images/card_fronts/9.jpg");
            fieldPane.getChildren().add(i);
            i.setOpacity(0.4);
            i.setLayoutX(imageView.getLayoutX() + 122);
            i.setLayoutY(imageView.getLayoutY() - 65);
        }
        //BottomLeft
        if(true){
            ImageView i = new ImageView();
            setCardImage(i, "file:src/main/resources/images/card_fronts/9.jpg");
            fieldPane.getChildren().add(i);
            i.setOpacity(0.4);
            i.setLayoutX(imageView.getLayoutX() - 122);
            i.setLayoutY(imageView.getLayoutY() + 65);
        }
        //BottomRight
        if(true){
            ImageView i = new ImageView();
            setCardImage(i, "file:src/main/resources/images/card_fronts/9.jpg");
            fieldPane.getChildren().add(i);
            i.setOpacity(0.4);
            i.setLayoutX(imageView.getLayoutX() + 122);
            i.setLayoutY(imageView.getLayoutY() + 65);
        }
    }

    public void setBoard(int goldDeckPath, int resourceDeckPath,
                         int visibleGold1Path, int visibleGold2Path,
                         int visibleResource1Path, int visibleResource2Path,
                         int objective1, int objective2) {
        setCardImage(goldDeck, translateToPath(goldDeckPath, true));
        setCardImage(resourceDeck, translateToPath(resourceDeckPath, true));
        setCardImage(visibleGold1, translateToPath(visibleGold1Path, false));
        setCardImage(visibleGold2, translateToPath(visibleGold2Path, false));
        setCardImage(visibleResource1, translateToPath(visibleResource1Path, false));
        setCardImage(visibleResource2, translateToPath(visibleResource2Path, false));
        setCardImage(publicObjective1, translateToPath(objective1, false));
        setCardImage(publicObjective2, translateToPath(objective2, false));
    }

    public void setHand(int card1, int card2, int card3, int objective){
        setCardImage(cardInHand1, translateToPath(card1, false));
        setCardImage(cardInHand2, translateToPath(card2, false));
        setCardImage(cardInHand3, translateToPath(card3, false));
        setCardImage(privateObjective, translateToPath(objective, false));
    }

    private void setCardImage(ImageView imageView, String s) {
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
}
