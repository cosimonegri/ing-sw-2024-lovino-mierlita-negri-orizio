package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.DrawType;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.PlacedCard;
import it.polimi.ingsw.modelView.BoardView;
import it.polimi.ingsw.modelView.FieldView;
import it.polimi.ingsw.modelView.PlayerView;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.DrawCardMessage;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.PlayCardMessage;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
    private Label playersTurn;
    @FXML
    private Label playerMessages;
    @FXML
    private AnchorPane board;
    @FXML
    private TabPane tabFieldsPane;
    @FXML
    private AnchorPane fieldAnchor;
    @FXML
    private Tab myField;
    @FXML
    private GridPane gridPanePlayer;
    @FXML
    private Pane topLeftText;
    @FXML
    private Label lobbyID;
    @FXML
    private Pane topRightText;
    @FXML
    private Label turnNum;
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
    private ImageView goldDeckDepth;
    @FXML
    private ImageView resourceDeckDepth;
    @FXML
    private ImageView scoreBoard;
    @FXML
    private Label phaseLabel;
    @FXML
    private GridPane s0;
    @FXML
    private GridPane s1;
    @FXML
    private GridPane s2;
    @FXML
    private GridPane s3;
    @FXML
    private GridPane s4;
    @FXML
    private GridPane s5;
    @FXML
    private GridPane s6;
    @FXML
    private GridPane s7;
    @FXML
    private GridPane s8;
    @FXML
    private GridPane s9;
    @FXML
    private GridPane s10;
    @FXML
    private GridPane s11;
    @FXML
    private GridPane s12;
    @FXML
    private GridPane s13;
    @FXML
    private GridPane s14;
    @FXML
    private GridPane s15;
    @FXML
    private GridPane s16;
    @FXML
    private GridPane s17;
    @FXML
    private GridPane s18;
    @FXML
    private GridPane s19;
    @FXML
    private GridPane s20;
    @FXML
    private GridPane s21;
    @FXML
    private GridPane s22;
    @FXML
    private GridPane s23;
    @FXML
    private GridPane s24;
    @FXML
    private GridPane s25;
    @FXML
    private GridPane s26;
    @FXML
    private GridPane s27;
    @FXML
    private GridPane s28;
    @FXML
    private GridPane s29;

    /**
     * Array containing all the gridPanes used to place the point markers
     */
    private GridPane[] points;
    /**
     * Image of the selected card to be played
     */
    private Image selectedCardImage;
    /**
     * Path of the selected card to be played
     */
    private String selectedCardPath;
    /**
     * ID of the selected card to be played
     */
    private int selectedCardID;
    /**
     * Position in hand of the selected card to be played
     */
    private int cardSelectedInHandNum;
    /**
     * The actual field of the player
     */
    private GridPane gridFieldPane;
    /**
     * The length of the gridPane, it's two units longer to get some more margin near the edges, these are never populated
     */
    private final int numColumns = 83;
    /**
     * The height of the gridPane, it's two units longer to get some more margin near the edges, these are never populated
     */
    private final int numRows = 83;
    /**
     * The reference to the respective GUI
     */
    private GUI gui;
    /**
     * A flag to mark the turnPhase of the player's round
     */
    private boolean isPlayPhase;

    /**
     * Initializes the grid containing the field, the colors; positions and effects of the other elements; the text and textFill and backGround of labels;
     * the scoreboard; the event handlers of the board.
     */
    @FXML
    public void initialize() {
        //todo messages text: for player messages red text for errors
        topLeftText.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, null, null)));
        topRightText.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, null, null)));
        playerMessages.setBackground(new Background(new BackgroundFill(Color.valueOf("#A0A0A0"), null, null)));
        playerMessages.setTextFill(Color.BLUE);
        fieldAnchor.setBackground(new Background(new BackgroundFill(Color.WHEAT, null, null)));
        board.setBackground(new Background(new BackgroundFill(Color.WHEAT, null, null)));
        gridPanePlayer.setBackground(new Background(new BackgroundFill(Color.WHEAT, null, null)));
        selectedCardImage = null;
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

        initializeEffects();
        addDrawEventHandlers();
        //when any player's turn starts this must be true, can be set true now
        isPlayPhase = true;
    }


    /**
     * Initializes the scoreBoard if not already initialized
     */
    public void startScores() {
        if(this.gui != null) {
            initScoreBoard();
        } else {
            System.out.println("Score board already started!");
        }
    }

    /**
     * Updates the scoreBoard
     */
    private void updateScores() {
        for(Node n : board.getChildren()) {
            if(n instanceof GridPane gridPaneScore) {
                gridPaneScore.getChildren().clear();
            }
        }
        setScores();
    }

    /**
     * Puts the markers on the score number
     */
    private void setScores() {
        for(int i = 0; i < gui.getGameView().getPlayers().size(); i++) {
            PlayerView playerView = gui.getGameView().getPlayers().get(i);
            Circle circle;
            if (playerView.getMarker().isPresent()) {
                circle = new Circle(7.0, getMarkerColor(playerView.getMarker().get()));
                if(i == 0) {
                    points[playerView.getTotalScore()].add(circle, 0, 0);
                } else if(i == 1) {
                    points[playerView.getTotalScore()].add(circle, 1, 0);
                } else if(i == 2) {
                    points[playerView.getTotalScore()].add(circle, 0, 1);
                } else if(i == 3) {
                    points[playerView.getTotalScore()].add(circle, 1, 1);
                }
            } else {
                return;
            }
        }
    }

    /**
     * Initializes the points array and calls setScores
     */
    private void initScoreBoard() {
        points = new GridPane[]{s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15,
                                s16, s17, s18, s19, s20, s21, s22, s23, s24, s25, s26, s27, s28, s29};
        setScores();
    }

    /**
     * @param marker chosen by the player
     * @return the color corresponding to the given marker
     */
    private Color getMarkerColor(Marker marker) {
        if(marker.equals(Marker.BLUE)) {
            return Color.BLUE;
        } else if(marker.equals(Marker.RED)) {
            return Color.RED;
        } else if(marker.equals(Marker.GREEN)) {
            return Color.GREEN;
        } else if(marker.equals(Marker.YELLOW)) {
            return Color.YELLOW;
        } else {
            return null;
        }
    }

    /**
     * Creates and adds event handlers to the cards in board
     */
    private void addDrawEventHandlers() {
        EventHandler<Event> chooseSettingPosition = e -> {
            if(gui.isMyTurn()) {
                if(!isPlayPhase) {
                    if(e.getSource() instanceof ImageView imageView) {
                        DrawType type;
                        int visibleCard;
                        if(imageView.equals(goldDeck)) {
                            type = DrawType.GOLD;
                        } else if(imageView.equals(resourceDeck)) {
                            type = DrawType.RESOURCE;
                        } else {
                            type = DrawType.VISIBLE;
                        }

                        if(imageView.equals(visibleGold1)) {
                            visibleCard = 0;
                        } else if(imageView.equals(visibleGold2)) {
                            visibleCard = 1;
                        } else if(imageView.equals(visibleResource1)) {
                            visibleCard = 2;
                        } else if(imageView.equals(visibleResource2)) {
                            visibleCard = 3;
                        } else {
                            visibleCard = -1;
                        }

                        if(type.equals(DrawType.GOLD)) {
                            gui.notifyAllListeners(new DrawCardMessage(gui.getUsername(), type, gui.getGameView().getBoard().getGoldTopCard()));
                        } else if(type.equals(DrawType.RESOURCE)) {
                            gui.notifyAllListeners(new DrawCardMessage(gui.getUsername(), type, gui.getGameView().getBoard().getResourceTopCard()));
                        } else if(type.equals(DrawType.VISIBLE)){ //note: it's not always true, the compiler thinks so because of the variable's initialization
                            gui.notifyAllListeners(new DrawCardMessage(gui.getUsername(), type, gui.getGameView().getBoard().getVisibleCards()[visibleCard]));
                        }
                    }
                } else {
                    playerMessages.setTextFill(Color.RED);
                    playerMessages.setText("You cannot draw now!");
                }
            }
        };

        goldDeck.addEventHandler(MouseEvent.MOUSE_PRESSED, chooseSettingPosition);
        visibleGold1.addEventHandler(MouseEvent.MOUSE_PRESSED, chooseSettingPosition);
        visibleGold2.addEventHandler(MouseEvent.MOUSE_PRESSED, chooseSettingPosition);
        resourceDeck.addEventHandler(MouseEvent.MOUSE_PRESSED, chooseSettingPosition);
        visibleResource1.addEventHandler(MouseEvent.MOUSE_PRESSED, chooseSettingPosition);
        visibleResource2.addEventHandler(MouseEvent.MOUSE_PRESSED, chooseSettingPosition);
    }

    /**
     * Sets the effects of the cards in hand and board
     */
    private void initializeEffects() {
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

    public void setGui(GUI gui) { this.gui = gui; }

    public void loadScoreBoard(){
        Image i = new Image("file:src/main/resources/images/PLATEAU-SCORE-IMP/plateau.jpg");
        scoreBoard.setImage(i);
        scoreBoard.setFitHeight(431);
        scoreBoard.setFitWidth(265);
        Rectangle2D cut = new Rectangle2D(40.0, 40.0, 480.0, 945.0);
        scoreBoard.setViewport(cut);
        scoreBoard.visibleProperty().setValue(false);
    }

    public AnchorPane getBoard() {
        return board;
    }

    public void setBoard(BoardView bv) {
        if(bv.getGoldTopCard() != null) {
            setCardImage(goldDeck, translateToPath(bv.getGoldTopCard().getId(), true));
        } else {
            goldDeck.setImage(null);
            goldDeckDepth.setImage(null);
        }
        if(bv.getResourceTopCard() != null) {
            setCardImage(resourceDeck, translateToPath(bv.getResourceTopCard().getId(), true));
        } else {
            resourceDeck.setImage(null);
            resourceDeck.setImage(null);
        }

        if(bv.getVisibleCards()[0] != null) {
            setCardImage(visibleGold1, translateToPath(bv.getVisibleCards()[0].getId(), false));
        } else {
            visibleGold1.setImage(null);
        }
        if(bv.getVisibleCards()[1] != null) {
            setCardImage(visibleGold2, translateToPath(bv.getVisibleCards()[1].getId(), false));
        } else {
            visibleGold2.setImage(null);
        }
        if(bv.getVisibleCards()[2] != null) {
            setCardImage(visibleResource1, translateToPath(bv.getVisibleCards()[2].getId(), false));
        } else {
            visibleResource1.setImage(null);
        }
        if(bv.getVisibleCards()[3] != null) {
            setCardImage(visibleResource2, translateToPath(bv.getVisibleCards()[3].getId(), false));
        } else {
            visibleResource2.setImage(null);
        }
    }

    public void setHand(List<PlayableCard> hand, int objective){
        setCardImage(cardInHand1, translateToPath(hand.getFirst().getId(), false));
        setCardImage(cardInHand2, translateToPath(hand.get(1).getId(), false));
        if(hand.size() == 3) {
            setCardImage(cardInHand3, translateToPath(hand.get(2).getId(), false));
        } else {
            cardInHand3.setImage(null);
        }
        setCardImage(privateObjective, translateToPath(objective, false));
    }

    public void setStarter(int starterId, boolean starterFlipped) {
        String path = "file:src/main/resources/images/" + ((!starterFlipped) ? "card_backs/" : "card_fronts/");
        ImageView starterCard = new ImageView();
        setCardImage(starterCard, path + starterId + ".jpg");
        gridFieldPane.add(starterCard,41,41);
    }

    public void setPersonalObjective(int id) {
        setCardImage(privateObjective, translateToPath(id, false));
    }

    public void setPublicObjective(List<ObjectiveCard> oc) {
        setCardImage(publicObjective1, translateToPath(oc.get(0).getId(), false));
        setCardImage(publicObjective2, translateToPath(oc.get(1).getId(), false));
        setCardImage(goldDeckDepth, "file:src/main/resources/images/card_backs/100.jpg");
        setCardImage(resourceDeckDepth, "file:src/main/resources/images/card_backs/100.jpg");
    }

    public static void setCardImage(ImageView imageView, String s) {
        Image image = new Image(s);
        imageView.setImage(image);
        imageView.setFitHeight(110);
        imageView.setFitWidth(159);
        Rectangle2D cut = new Rectangle2D(75.0, 75.0, 875.0, 605.0);
        imageView.setViewport(cut);
    }

    /**
     *
     */
    public void updateGui() {
        FieldView fieldView = gui.getGameView().getPlayer(gui.getUsername()).getField();
        //update field
        for(int y = fieldView.getBottomRightBound().y(); y <= fieldView.getTopLeftBound().y(); y++) {
            for (int x = fieldView.getTopLeftBound().x(); x <= fieldView.getBottomRightBound().x(); x++) {
                Coordinates cell = new Coordinates(x, y);
                if(fieldView.getPlacedCard(cell) != null) {
                    PlacedCard thisPlacedCard = fieldView.getPlacedCard(cell);
                    Node node = getNodeFromGridPane(x + 1,numRows - 2 - y);
                    if(node != null) {
                        gridFieldPane.getChildren().remove(node);
                    }
                    ImageView imageView = new ImageView();
                    if(x == 40 && y == 40) {
                        setCardImage(imageView, translateToPath(thisPlacedCard.card().getId(), !thisPlacedCard.flipped()));
                    } else {
                        setCardImage(imageView, translateToPath(thisPlacedCard.card().getId(), thisPlacedCard.flipped()));
                    }
                    //The coordinates are adjusted to be correctly converted between the view matrix and the gridPane matrix
                    //the gridPane is bigger by 1 row and 1 column, the y orientation is inverted
                    gridFieldPane.add(imageView, x + 1, numRows - 2 - y);
                } else {
                    Node node = getNodeFromGridPane(x + 1,numRows - 2 - y);
                    if(node instanceof Rectangle && !fieldView.getAllValidCoords().contains(cell)) {
                        gridFieldPane.getChildren().remove(node);
                    }
                }
            }
        }
        newPlayablePositions(gui.getGameView().getPlayer(gui.getUsername()).getField());
        //update hand
        if(gui.getGameView().getPlayer(gui.getUsername()).getObjective().isPresent()) {
            setHand(gui.getGameView().getPlayer(gui.getUsername()).getHand(), gui.getGameView().getPlayer(gui.getUsername()).getObjective().get().getId());
        }
        //update board
        setBoard(gui.getGameView().getBoard());
        updateScores();
        //update texts
        turnNum.setText(String.valueOf(gui.getGameView().getCurrentTurn()));
        if (gui.getGameView().isCurrentPlayer(gui.getUsername())) {
            playersTurn.setText("It's your turn!");
            if(gui.getGameView().getTurnPhase().equals(TurnPhase.PLAY)){
                phaseLabel.setText("Play a card!");
            }
        } else if (gui.getGameView().getCurrentPlayer().isPresent()) {
            playersTurn.setText("It's " + gui.getGameView().getCurrentPlayer().get().getUsername() + "'s turn...");
        }
    }

    /**
     * Scans the fieldView to finds playable positions and to put interractable rectangles to play the selected card
     * @param fieldView of the player's field
     */
    public void newPlayablePositions(FieldView fieldView){
        for(int y = fieldView.getBottomRightBound().y(); y <= fieldView.getTopLeftBound().y(); y++) {
            for(int x = fieldView.getTopLeftBound().x(); x <= fieldView.getBottomRightBound().x(); x++) {
                Coordinates cell = new Coordinates(x, y);
                if(fieldView.getPlacedCard(cell) == null) {
                    if(fieldView.getAllValidCoords().contains(cell)) {
                        Node node = getNodeFromGridPane(x + 1,numRows - 2 - y);
                        if(node == null) {
                            if(x != 0 && x != 80 && y != 0 && y != 80){
                                if(y % 2 == 1){
                                    if(x % 2 == 1) {
                                        gridFieldPane.add(playableCell(), x + 1, numRows - 2 - y);
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

    /**
     * Creates the interractable object from which the selected card can be played
     * @return the interractable rectangle
     */
    private Rectangle playableCell() {
        Rectangle playableCell = new Rectangle(0,0,159,110);
        playableCell.setFill(Color.GRAY);
        playableCell.setOpacity(0.2);

        EventHandler<Event> chooseSettingPosition = e -> {
                    if (e instanceof MouseEvent) {
                        if (gui.isMyTurn()){
                            if(isPlayPhase) {
                                if (selectedCardImage != null && selectedCardPath != null && selectedCardID > 0) {
                                    boolean flipped = ((MouseEvent) e).isSecondaryButtonDown();
                                    if (e.getSource() instanceof Rectangle r) { addDrawEventHandlers();
                                        isPlayPhase = false;
                                        int x = GridPane.getColumnIndex(r);
                                        int y = GridPane.getRowIndex(r);
                                        gui.notifyAllListeners(new PlayCardMessage(
                                                gui.getUsername(), gui.getGameView().getPlayer(gui.getUsername()).getHand().get(cardSelectedInHandNum),
                                                flipped, new Coordinates(x - 1, numRows - 2 - y))
                                        );
                                    }
                                } else {
                                    playerMessages.setTextFill(Color.RED);
                                    playerMessages.setText("Select a card first!");
                                }
                            } else {
                                playerMessages.setTextFill(Color.RED);
                                playerMessages.setText("You cannot play a card now!");
                            }
                        }
                    }
        };
        playableCell.addEventHandler(MouseEvent.MOUSE_PRESSED, chooseSettingPosition);
        return playableCell;
    }

    public void setIsPlayPhase(boolean isPlayPhase) { this.isPlayPhase = isPlayPhase; }

    /**
     * Translates a given card ID to the card's image path
     * @param id of the card to be translated
     * @param flipped state of the card
     * @return the image path of the card
     */
    public static String translateToPath(int id, boolean flipped){
        if(!flipped) {
            return "file:src/main/resources/images/card_fronts/" + id + ".jpg";
        } else {
            return "file:src/main/resources/images/card_backs/" + id + ".jpg";
        }
    }

    /**
     * Translates the url of a card image to its actual ID
     * @param url of the card to be given the ID
     * @return the ID number of the card
     */
    private int translateToID(String url){
        return Integer.parseInt(url.replaceAll("[^0-9]",""));
    }

    /**
     * @param col of the requested card
     * @param row of the requested card
     * @return the node ad the given position or null if there is no card
     */
    private Node getNodeFromGridPane(int col, int row) {
        for (Node node : gridFieldPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Sets the current card attributes and gives an error message to the player gui if it's not the right time
     * @param e event detected by the object
     */
    public void selectCard(MouseEvent e) {
        if(gui.isMyTurn()){
            if(isPlayPhase) { //is still false at start of new turn
                if (e.getSource() instanceof ImageView imageView) {
                    System.out.println(imageView + " card selected");
                    selectedCardImage = imageView.getImage();
                    selectedCardPath = imageView.getImage().getUrl();
                    selectedCardID = translateToID(selectedCardPath);
                    if (imageView.equals(cardInHand1)) {
                        cardSelectedInHandNum = 0;
                    } else if (imageView.equals(cardInHand2)) {
                        cardSelectedInHandNum = 1;
                    } else if (imageView.equals(cardInHand3)) {
                        cardSelectedInHandNum = 2;
                    }
                }
            } else {
                playerMessages.setTextFill(Color.RED);
                playerMessages.setText("You must draw a card now!");
            }
        }
    }

    /**
     * Sets the glow effect
     * @param e detected event
     */
    public void lightUp(MouseEvent e) {
        if(e.getSource() instanceof ImageView imageView) {
            Glow glow = new Glow();
            DropShadow blueDropShadow = new DropShadow(10, Color.BLUE);
            glow.setInput(blueDropShadow);
            imageView.setEffect(glow);
        }
    }

    /**
     * Removes the glow effect
     * @param e detected event
     */
    public void lightOff(MouseEvent e) {
        if(e.getSource() instanceof ImageView imageView) {
            Glow glow = new Glow(0);
            DropShadow dropShadow = new DropShadow();
            glow.setInput(dropShadow);
            imageView.setEffect(glow);
        }
    }

    public void setPlayersTurnText(String text) {
        playersTurn.setText(text);
    }

    public void setPlayerMessagesText(String text, Color color) {
        playerMessages.setText(text);
        playerMessages.setTextFill(color);
    }

    public void addFieldTab(Tab tab) {
        tabFieldsPane.getTabs().add(tab);
    }

    public void setMyField(String text) {
        myField.setText(text);
    }

    public void setLobbyID(int id) {
        lobbyID.setText(String.valueOf(id));
    }

    public void setTurnNum(int turn) {
        turnNum.setText(String.valueOf(turn));
    }

    public void setScoreBoardVisible(boolean boardVisible) {
        scoreBoard.visibleProperty().setValue(boardVisible);
    }

    public void setPhaseLabelText(String text) {
        phaseLabel.setText(text);
    }
}
