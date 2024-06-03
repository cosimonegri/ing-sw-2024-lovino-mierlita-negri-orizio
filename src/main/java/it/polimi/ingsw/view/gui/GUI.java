package it.polimi.ingsw.view.gui;

import com.sun.scenario.DelayedRunnable;
import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.CreateGameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.JoinGameMessage;
import it.polimi.ingsw.network.message.servertoclient.*;
import it.polimi.ingsw.utilities.Printer;
import it.polimi.ingsw.view.View;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GUI extends View {

    private StackPane root;
    private Stage window;
    private int gameId;



    public GUI () {
        super();
    }
    public void run() {
        Platform.startup(() -> {
            try {
//                startGame();
                start(new Stage());
            } catch (Exception e) {
                System.err.println("Could not load GUI.");
//                e.printStackTrace();
            }

        });
    }

    @Override
    public void start(Stage window) throws Exception {
        this.window = window;
        // Set background
        Background backgroundPane = getBackgroundPane();

        VBox vbox = new VBox();
        vbox.getStyleClass().addAll("welcome-box");
        vbox.setOpacity(0);

        // welcome text
        Text welcomeText = new Text("Welcome to Codex Naturalis");
        welcomeText.getStyleClass().addAll("welcome-text");

        // contains the use form
        VBox connectForm = new VBox();
        connectForm.getStyleClass().addAll("vbox");
        connectForm.setOpacity(0);

        HBox usernameForm = new HBox();
        usernameForm.getStyleClass().addAll("username-box");

        // form elements
        Label usernameLabel = new Label("Username: ");
        usernameLabel.getStyleClass().addAll("label");

        Label errorLabel = new Label();
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        errorLabel.getStyleClass().addAll("error-label");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.getStyleClass().addAll("label");


        Button connectButton = new Button("Connect");
        connectButton.getStyleClass().add("button");

        // add elements to the form
        usernameForm.setSpacing(10);
        usernameForm.getChildren().addAll(usernameLabel, usernameField, connectButton);

        connectForm.setSpacing(10);
        connectForm.getChildren().addAll(errorLabel, usernameForm);

        vbox.getChildren().addAll(welcomeText, connectForm);

        // create a new stack pane to show the boxes
        StackPane root = new StackPane();

        root.setBackground(backgroundPane);
        root.getChildren().addAll(vbox);
        //root.getChildren().addAll(backgroundPane, vbox);

        // add transition
        PauseTransition stageShow = new PauseTransition(Duration.seconds(2));
        FadeTransition vboxFadeIn = fadeIn(vbox, 2);
        PauseTransition vboxShow = new PauseTransition(Duration.seconds(1));
        FadeTransition welcomeTextFadeIn = fadeIn(welcomeText, 2);
        PauseTransition welcomeTestShow = new PauseTransition(Duration.seconds(1));
        FadeTransition connectFormFadeIn = fadeIn(connectForm, 2);

        stageShow.setOnFinished(e -> vboxFadeIn.play());
        vboxFadeIn.setOnFinished(e -> vboxShow.play());
        vboxShow.setOnFinished(e -> welcomeTextFadeIn.play());
        welcomeTextFadeIn.setOnFinished(e -> welcomeTestShow.play());
        welcomeTestShow.setOnFinished(e -> connectFormFadeIn.play());
//        transitionOnCreateScene(true, vbox, welcomeText, connectForm);


        // connect button
        connectButton.setOnAction(e -> {
            connectButton.setDisable(true);
            Task<Void> connectTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    String username = usernameField.getText();
                    notifyAllListeners(new UsernameMessage(username));

                    ServerToClientMessage response = waitForMessage();

                    if (response instanceof UsernameAckMessage) {
                        Platform.runLater(() -> {
                            setUsername(username);
                            createJoinCreateGame();
                        });
                        System.out.println("Connect successful");
                    }
                    else if (response instanceof UsernameNotValidMessage m) {
                        Printer.printError(m.getMessage());
                        Platform.runLater(() -> {
                            showErrorMessage(m.getMessage(), errorLabel);
                        });

                    }
                    else {
                        addMessage(response);
                    }
                    Platform.runLater(() -> connectButton.setDisable(false));
                    return null;
                }
            };
            new Thread(connectTask).start();
        });

        usernameField.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER) {
                connectButton.fire();
            }
        } );

        this.root = root;

        // create a new scene
        Scene scene = new Scene(root);
        scene.getStylesheets().add("file:src/main/resources/css/style.css");
        window.setScene(scene);
        window.setFullScreen(true);
        window.setFullScreenExitHint("");
        window.setTitle("Code Naturalis");
        window.setMinWidth(1100);
        window.setMinHeight(750);

        window.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
        });

        window.setOnShown(e -> {
            vboxFadeIn.play();
        });
        // disable auto focus
        Platform.runLater(vbox::requestFocus);

        // skip animation
        EventHandler<Event> skipTransitionEvent = e -> {
            if ((e instanceof KeyEvent && ((KeyEvent) e).getCode() == KeyCode.ENTER) ||
            e instanceof MouseEvent && ((MouseEvent) e).isPrimaryButtonDown()) {
                stageShow.stop();
                vboxFadeIn.stop();
                vbox.setOpacity(1);
                vboxShow.stop();
                welcomeTextFadeIn.stop();
                welcomeText.setOpacity(1);
                welcomeTestShow.stop();
                connectFormFadeIn.stop();
                connectForm.setOpacity(1);
            }
        };
        vbox.addEventHandler(MouseEvent.MOUSE_PRESSED, skipTransitionEvent);
        vbox.addEventHandler(KeyEvent.KEY_PRESSED, skipTransitionEvent);

        window.show();
    }

    private void showErrorMessage(String message, Label errorLabel) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
        PauseTransition errorShow = new PauseTransition(Duration.seconds(3));
        FadeTransition errorFadeOut = fadeOut(errorLabel, 5);

        errorShow.setOnFinished(e -> errorFadeOut.play());
        errorFadeOut.setOnFinished(e -> {
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
        });
        errorShow.play();
    }
    private FadeTransition fadeOut(Node node, int sec) {
        FadeTransition fadeNodeOut = new FadeTransition(Duration.seconds(sec), node);
        fadeNodeOut.setFromValue(1);
        fadeNodeOut.setToValue(0);
        return fadeNodeOut;
    }
    private FadeTransition fadeIn(Node node, int sec) {
        FadeTransition fadeNodeIn = new FadeTransition(Duration.seconds(sec), node);
        fadeNodeIn.setFromValue(0);
        fadeNodeIn.setToValue(1);
        return fadeNodeIn;
    }
    private Background getBackgroundPane() {
        String backgroundPath = "file:src/main/resources/images/codex-background.jpg";
        Image backgroundImage = new Image(backgroundPath);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0,
                        BackgroundSize.AUTO, true, true, false, false));
        // set background image
        //Pane backgroundPane = new Pane();
        //backgroundPane.setBackground(new Background(background));
        return new Background(background);
    }

    private void setUsername(String username) {
        this.username = username;
    }
    private void createJoinCreateGame() {
        Text lobbyText = new Text("Welcome " +  this.username);
        lobbyText.getStyleClass().addAll("welcome-text");

        VBox choiceBox = new VBox();
        choiceBox.getStyleClass().addAll("vbox");

        HBox createGameBox = new HBox();
        createGameBox.setOpacity(0);
        createGameBox.getStyleClass().addAll("username-box", "create-game-box");
        HBox joinGameBox = new HBox();
        joinGameBox.setOpacity(0);
        joinGameBox.getStyleClass().addAll("username-box", "create-game-box");

        Label playersCountLabel = new Label("Players count");
        TextField playersCountField = new TextField();
        playersCountField.getStyleClass().addAll("label");
        playersCountField.setPromptText("Players count");
        playersCountLabel.getStyleClass().addAll("label");
        playersCountField.getStyleClass().addAll();

        Button createGameButton = new Button("New game");
        createGameButton.getStyleClass().add("button");

        createGameBox.getChildren().addAll(playersCountLabel, playersCountField, createGameButton);

        Label gameIdLabel = new Label("Game ID");
        gameIdLabel.getStyleClass().addAll("label");
        TextField gameIdField = new TextField();
        gameIdField.getStyleClass().addAll("label");
        gameIdField.setPromptText("Game ID");

        Button joinGameButton = new Button("Join game");
        joinGameButton.getStyleClass().add("button");

        Label errorLabel = new Label();
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        errorLabel.getStyleClass().addAll("error-label");

        joinGameBox.getChildren().addAll(gameIdLabel, gameIdField, joinGameButton);

        choiceBox.getChildren().addAll(createGameBox, joinGameBox, errorLabel);

        VBox vbox = new VBox();
        vbox.getStyleClass().addAll("welcome-box");
        vbox.getChildren().addAll(lobbyText, choiceBox);

        root.getChildren().removeLast();
        root.getChildren().addAll(vbox);

        // create game button
        createGameButton.setOnAction(event -> {
            createGameButton.setDisable(true);
            joinGameButton.setDisable(true);
            Task<Void> createGameTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    String playersCount = playersCountField.getText().strip();
                    try {
                        int count = Integer.parseInt(playersCount);
                        if (count < 2 || count > 4) throw new NumberFormatException();

                        notifyAllListeners(new CreateGameMessage(getUsername(), count));
                        ServerToClientMessage response = waitForMessage();

                        if (response instanceof CreateGameAckMessage r) {
                            System.out.println("Created game with ID " + r.getGameId() + ". Waiting for players to join...");
                            setGameId(r.getGameId());
                            new Thread(() -> createWaitingLobbyPage(null)).start();
                        } else if (response instanceof CreateGameErrorMessage) {
                            // cannot read json files in the server
                            Printer.printError("Cannot create a game. Try again later...");
                            System.exit(1);
                        }
                    } catch (NumberFormatException e) {
                        Platform.runLater(() -> {
                            errorLabel.setText("Invalid number");
                            errorLabel.setVisible(true);
                            errorLabel.setManaged(true);
                        });
                    }
                    Platform.runLater(() -> {
                        createGameButton.setDisable(false);
                        joinGameButton.setDisable(false);
                    });
                    return null;
                }
            };
            new Thread(createGameTask).start();
        });

        // join game button
        joinGameButton.setOnAction(event -> {
            joinGameButton.setDisable(true);
            createGameButton.setDisable(true);
            Task<Void> joinGameTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    String gameId = gameIdField.getText().strip();
                    try {
                        int id = Integer.parseInt(gameId);
                        notifyAllListeners(new JoinGameMessage(getUsername(), id));

                        ServerToClientMessage response = waitForMessage();

                        if (response instanceof LobbyMessage r) {
                            System.out.println("Entered lobby");
                            setGameId(id);
                            new Thread(() -> createWaitingLobbyPage(r)).start();
                        }
                        else if (response instanceof LobbyNotValidMessage r) {
                            Printer.printError(r.getMessage());
                            Platform.runLater(() -> {
                                errorLabel.setText("Invalid game id");
                                errorLabel.setVisible(true);
                                errorLabel.setManaged(true);
                            });
                        }
                    } catch (NumberFormatException e) {
                        errorLabel.setText("Invalid number");
                        errorLabel.setOpacity(1);
                    }
                    Platform.runLater(() -> {
                        joinGameButton.setDisable(false);
                        createGameButton.setDisable(false);
                    });
                    return null;
                }
            };
            new Thread(joinGameTask).start();
        });

        gameIdField.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER) {
                joinGameButton.fire();
            }
        } );

        playersCountField.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER) {
                createGameButton.fire();
            }
        } );

        // add transition
        PauseTransition stageShow = new PauseTransition(Duration.seconds(0.5));
        FadeTransition lobbyTextFadeIn = fadeIn(lobbyText, 2);
        PauseTransition lobbyTextShow = new PauseTransition(Duration.seconds(1));
        FadeTransition createGameChoiceFadeIn = fadeIn(createGameBox, 2);
        PauseTransition showCreateGameChoice = new PauseTransition(Duration.seconds(1));
        FadeTransition joinGameChoiceFadeIn = fadeIn(joinGameBox, 2);

        stageShow.setOnFinished(e -> lobbyTextFadeIn.play());
        lobbyTextFadeIn.setOnFinished(e -> lobbyTextShow.play());
        lobbyTextShow.setOnFinished(e -> createGameChoiceFadeIn.play());
        createGameChoiceFadeIn.setOnFinished(e -> showCreateGameChoice.play());
        showCreateGameChoice.setOnFinished(e -> joinGameChoiceFadeIn.play());

        stageShow.play();

        // disable auto focus
        Platform.runLater(vbox::requestFocus);

        // skip animation
        EventHandler<Event> skipTransitionEvent = e -> {
            if ((e instanceof KeyEvent && ((KeyEvent) e).getCode() == KeyCode.ENTER) ||
                    e instanceof MouseEvent && ((MouseEvent) e).isPrimaryButtonDown()) {
                stageShow.stop();
                lobbyTextFadeIn.stop();
                lobbyText.setOpacity(1);
                lobbyTextShow.stop();
                createGameChoiceFadeIn.stop();
                createGameBox.setOpacity(1);
                showCreateGameChoice.stop();
                joinGameChoiceFadeIn.stop();
                joinGameBox.setOpacity(1);
            }
        };
        vbox.addEventHandler(MouseEvent.MOUSE_PRESSED, skipTransitionEvent);
        vbox.addEventHandler(KeyEvent.KEY_PRESSED, skipTransitionEvent);
    }

    private void setGameId(int gameId) {
        this.gameId = gameId;
    }

    private String getUsername() {
        return this.username;
    }

    private void createWaitingLobbyPage(LobbyMessage message) {
        System.out.println("Waiting page");
        VBox vbox = new VBox();
        vbox.getStyleClass().addAll("welcome-box");
        vbox.setAlignment(Pos.CENTER);
        Text playerUsername = new Text(getUsername());
        playerUsername.setOpacity(0);
        playerUsername.getStyleClass().addAll("welcome-text");

        Text waitingLobbyText = new Text("Lobby " + getGameId() + ": waiting for other players");
        waitingLobbyText.setOpacity(0);
        waitingLobbyText.getStyleClass().addAll("lobby-text");

        HBox lobbyUsernames = new HBox();
        lobbyUsernames.setOpacity(0);
        lobbyUsernames.getStyleClass().addAll("hbox");

        if (message != null) {
            lobbyUsernames.getChildren().clear();
            for (String s :  message.getUsernames()) {
                Text player = new Text(s);
                player.getStyleClass().addAll("lobby-text");
                lobbyUsernames.getChildren().add(player);
            }
        }

        vbox.getChildren().addAll(playerUsername, waitingLobbyText, lobbyUsernames);
        Platform.runLater(() -> {
            root.getChildren().removeLast();
            root.getChildren().addAll(vbox);
        });

        PauseTransition showPage = new PauseTransition(Duration.seconds(0.5));
        FadeTransition usernameFadeIn = fadeIn(playerUsername, 2);
        PauseTransition showUsername = new PauseTransition(Duration.seconds(1));
        FadeTransition waitingTextFadeIn = fadeIn(waitingLobbyText, 2);
        PauseTransition showWaitingText = new PauseTransition(Duration.seconds(1));
        FadeTransition lobbyUsernamesFadeIn = fadeIn(lobbyUsernames, 2);

        showPage.setOnFinished(e -> usernameFadeIn.play());
        usernameFadeIn.setOnFinished(e -> showUsername.play());
        showUsername.setOnFinished(e -> waitingTextFadeIn.play());
        waitingTextFadeIn.setOnFinished(e -> showWaitingText.play());
        showWaitingText.setOnFinished(e -> lobbyUsernamesFadeIn.play());

        showPage.play();

        // skip animation
        EventHandler<Event> skipTransitionEvent = e -> {
            if ((e instanceof KeyEvent && ((KeyEvent) e).getCode() == KeyCode.ENTER) ||
                    e instanceof MouseEvent && ((MouseEvent) e).isPrimaryButtonDown()) {
                showPage.stop();
                usernameFadeIn.stop();
                playerUsername.setOpacity(1);
                showUsername.stop();
                waitingTextFadeIn.stop();
                waitingLobbyText.setOpacity(1);
                showWaitingText.stop();
                lobbyUsernamesFadeIn.stop();
                lobbyUsernames.setOpacity(1);
            }
        };
        vbox.addEventHandler(MouseEvent.MOUSE_PRESSED, skipTransitionEvent);
        vbox.addEventHandler(KeyEvent.KEY_PRESSED, skipTransitionEvent);

        while (true) {
            ServerToClientMessage response = waitForMessage();
            System.out.println("New message " + response.getClass());
            if (response instanceof LobbyMessage r) {
                Platform.runLater(() -> {
                    lobbyUsernames.getChildren().clear();
                    lobbyUsernames.setOpacity(0);
                    for (String s : r.getUsernames()) {
                        Text player = new Text(s);
                        player.getStyleClass().addAll("lobby-text");
                        lobbyUsernames.getChildren().add(player);
                    }
                    lobbyUsernamesFadeIn.play();
                });

            }
            else if (response instanceof ViewUpdateMessage r) {
                System.out.println("Game started");
                try {
                    gameView = r.getGameView();
                    startGame();
                } catch (IOException e) {
                    System.err.println("Cannot start game interface");
                }
            }
        }
    }

    private void startGame() throws IOException {
        // load scene builder fxml

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/playingGui.fxml"));
        GridPane root = loader.load();

        GuiController controller = loader.getController();

        //set text
        controller.gridPane.setBackground(getBackgroundPane());
        controller.board.setBackground(Background.EMPTY);
        controller.lobbyID.setText(String.valueOf(gameId));
        controller.turnNum.setText(String.valueOf(gameView.getCurrentTurn()));
        if (gameView.isCurrentPlayer(this.username)) {
            controller.playersTurn.setText("It's your turn!");
        } else {
            controller.playersTurn.setText("It's " + gameView.getCurrentPlayer().getUsername() + "'s turn...");
        }
        controller.myField.setText(this.username);

        //set cards
        controller.setHand( gameView.getPlayer(this.username).getHand().get(0).getId(),
                            gameView.getPlayer(this.username).getHand().get(1).getId(),
                            gameView.getPlayer(this.username).getHand().get(2).getId(),
                            gameView.getPlayer(this.username).getObjectiveOptions().getFirst().getId());

        Scene scene = new Scene(root);
        Platform.runLater(() -> {
            window.setScene(scene);
            window.setMinHeight(750);
            window.setMinWidth(1100);});
    }
    private int getGameId() {
        return this.gameId;
    }
}
