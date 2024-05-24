package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.CreateGameMessage;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.JoinGameMessage;
import it.polimi.ingsw.network.message.servertoclient.*;
import it.polimi.ingsw.utilities.Printer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.Stack;


public class GUI extends View {

    private StackPane root;


    public GUI () {
        super();
    }
    public void run() {
        Platform.startup(() -> {
            try {
                start(new Stage());
            } catch (Exception e) {
                System.err.println("Could not load GUI.");
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // create page to insert username and connect
        StackPane root = createWelcomePage();
        this.root = root;

        // create a new scene
        Scene scene = new Scene(root);
        scene.getStylesheets().add("file:src/main/resources/css/style.css");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setTitle("Code Naturalis");

        primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
        });

        primaryStage.show();
    }

    private Pane getBackgroundPane() {
        String backgroundPath = "file:src/main/resources/images/background.jpg";
        Image backgroundImage = new Image(backgroundPath);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0,
                        BackgroundSize.AUTO, true, true, false, false));
        // set background image
        Pane backgroundPane = new Pane();
        backgroundPane.setBackground(new Background(background));
        return backgroundPane;
    }

    private StackPane createWelcomePage() {
        Pane backgroundPane = getBackgroundPane();

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        // welcome text
        Text welcomeText = new Text("Welcome to Code Naturalis");
        welcomeText.getStyleClass().addAll("welcome-text");

        // contains the username form
        VBox connectForm = new VBox();
        connectForm.getStyleClass().addAll("box", "form-box");

        HBox usernameForm = new HBox();
        usernameForm.getStyleClass().addAll("box");

        // form elements
        Label usernameLabel = new Label("Username: ");
        usernameLabel.getStyleClass().addAll("label");

        Label errorLabel = new Label();
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        errorLabel.getStyleClass().addAll("error-label");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Button connectButton = new Button("Connect");
        connectButton.getStyleClass().add("button");

        // add elements to the form
        usernameForm.setSpacing(10);
        usernameForm.getChildren().addAll(usernameLabel, usernameField);

        connectForm.setSpacing(10);
        connectForm.getChildren().addAll(errorLabel, usernameForm, connectButton);

        vbox.getChildren().addAll(welcomeText, connectForm);

        // create a new stack pane to show the boxes
        StackPane root = new StackPane();

        root.getChildren().addAll(backgroundPane, vbox);
        //root.getChildren().addAll(vbox);

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
                            createLobbyPage();
                        });
                        System.out.println("Connect successful");
                    }
                    else if (response instanceof UsernameNotValidMessage m) {
                        Printer.printError(m.getMessage());
                        Platform.runLater(() -> {
                            errorLabel.setText(m.getMessage());
                            errorLabel.setVisible(true);
                            errorLabel.setManaged(true);
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
        return root;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void createLobbyPage() {
        Text lobbyText = new Text("Welcome " +  this.username);
        lobbyText.getStyleClass().addAll("text", "current-action");

        VBox choiceBox = new VBox();
        choiceBox.getStyleClass().addAll("box", "form-box");

        HBox createGameBox = new HBox();
        createGameBox.getStyleClass().addAll("box");
        HBox joinGameBox = new HBox();
        joinGameBox.getStyleClass().addAll("box");

        Label playersCountLabel = new Label("Players count");
        TextField playersCountField = new TextField();
        playersCountField.setPromptText("Players count");
        playersCountLabel.getStyleClass().addAll("label");
        playersCountField.getStyleClass().addAll();

        Button createGameButton = new Button("Create a new game");
        createGameButton.getStyleClass().add("button");

        createGameBox.getChildren().addAll(playersCountLabel, playersCountField, createGameButton);

        Label gameIdLabel = new Label("Game ID");
        gameIdLabel.getStyleClass().addAll("label");
        TextField gameIdField = new TextField();
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
        vbox.setAlignment(Pos.CENTER);
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
                            new Thread(() -> waitingPage(null)).start();
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
                            new Thread(() -> waitingPage(r)).start();
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
    }

    private String getUsername() {
        return this.username;
    }

    private void waitingPage(LobbyMessage message) {
        System.out.println("Waiting page");
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Text playerUsername = new Text(getUsername());
        playerUsername.getStyleClass().addAll("text");

        Text waitingLobbyText = new Text("Waiting for other players");
        waitingLobbyText.getStyleClass().addAll("text", "current-action");

        HBox lobbyUsernames = new HBox();
        lobbyUsernames.getStyleClass().addAll("box");
        if (message != null) {
            lobbyUsernames.getChildren().clear();
            for (String s :  message.getUsernames()) {
                Text player = new Text(s);
                player.getStyleClass().addAll("text");
                lobbyUsernames.getChildren().add(player);
            }
        }

        vbox.getChildren().addAll(playerUsername, waitingLobbyText, lobbyUsernames);
        Platform.runLater(() -> {
            root.getChildren().removeLast();
            root.getChildren().addAll(vbox);
        });


        while (true) {
            ServerToClientMessage response = waitForMessage();
            System.out.println("New message " + response.getClass());
            if (response instanceof LobbyMessage r) {
                Platform.runLater(() -> {
                    lobbyUsernames.getChildren().clear();
                    for (String s : r.getUsernames()) {
                        Text player = new Text(s);
                        player.getStyleClass().addAll("text");
                        lobbyUsernames.getChildren().add(player);
                    }
                });

            }
            else if (response instanceof GameSetupStartedMessage) {
                System.out.println("Game started");
                System.exit(0);
            }
        }
    }

    private void setupGame() {
        System.exit(3);
    }
}
