package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.clienttoserver.UsernameMessage;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.network.message.servertoclient.UsernameAckMessage;
import it.polimi.ingsw.network.message.servertoclient.UsernameNotValidMessage;
import it.polimi.ingsw.utilities.Printer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.util.Duration;

import java.util.EventListener;

public class GUI extends View {

    public GUI () {
        super();
    }
    public void run() {
        Platform.startup(() -> {
            try {
                start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load background image
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

        // welcome text
        Text welcomeText = new Text("Welcome to Code Naturalis");
        welcomeText.getStyleClass().addAll("text", "welcome-text");

        // contains the welcome text
        VBox welcomeBox = new VBox(welcomeText);
        welcomeBox.getStyleClass().addAll("box", "text-box");

        // contains the username form
        VBox usernameForm = new VBox();
        usernameForm.getStyleClass().addAll("box", "form-box");

        // form elements
        Label usernameLabel = new Label("Username: ");
        usernameLabel.getStyleClass().addAll("text", "label", "username-label");

        Label errorLabel = new Label("Username not valid: ");
        errorLabel.getStyleClass().addAll("text", "label", "error-label");

        TextField usernameField = new TextField();

        Button connectButton = new Button("Connect");
        connectButton.getStyleClass().add("button");
        // add elements to the form
        usernameForm.getChildren().addAll(usernameLabel, usernameField, connectButton, errorLabel);

        // create a new stack pane to show the boxes
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundPane, welcomeBox, usernameForm);
        StackPane.setAlignment(welcomeBox, Pos.CENTER);
        StackPane.setAlignment(usernameForm, Pos.CENTER);

        // create a new scene
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("file:src/main/resources/css/style.css");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setTitle("Code Naturalis");

        primaryStage.setOnShown(event -> {

            // show welcome message
            FadeTransition welcomeTextFadeIn = new FadeTransition(Duration.seconds(3), welcomeBox);
            welcomeTextFadeIn.setFromValue(0);
            welcomeTextFadeIn.setToValue(1);
            welcomeTextFadeIn.setCycleCount(1);
            welcomeTextFadeIn.setAutoReverse(false);

            // show delay
            PauseTransition stayOnScreen = new PauseTransition(Duration.seconds(1));

            // hide welcome text
            FadeTransition welcomeTextFadeOut = new FadeTransition(Duration.seconds(2), welcomeBox);
            welcomeTextFadeOut.setFromValue(1);
            welcomeTextFadeOut.setToValue(0);
            welcomeTextFadeOut.setCycleCount(1);
            welcomeTextFadeOut.setAutoReverse(false);

            // show username form
            FadeTransition showUsernameForm = new FadeTransition(Duration.seconds(2), usernameForm);
            showUsernameForm.setFromValue(0);
            showUsernameForm.setToValue(1);
            showUsernameForm.setCycleCount(1);
            showUsernameForm.setAutoReverse(false);

            // animation sequence
            welcomeTextFadeIn.setOnFinished(e -> {
                stayOnScreen.play();
            });
            stayOnScreen.setOnFinished(e -> {
                welcomeTextFadeOut.play();
            });
            welcomeTextFadeOut.setOnFinished(e -> {
                showUsernameForm.play();
            });

            // initial delay
            PauseTransition initialDelay = new PauseTransition(Duration.seconds(1));
            initialDelay.setOnFinished(e -> {
                welcomeTextFadeIn.play();
            });
            initialDelay.play();

            // skip animation
            root.setOnMouseClicked(e -> {
                welcomeTextFadeIn.stop();
                stayOnScreen.stop();
                welcomeTextFadeOut.stop();
                welcomeBox.setOpacity(0);
                usernameForm.setOpacity(1);
            });
        });

        // connect button
        connectButton.setOnAction(e -> {
            String username = usernameField.getText();
            notifyAllListeners(new UsernameMessage(username));
            ServerToClientMessage response = waitForMessage();
            if (response instanceof UsernameAckMessage) {
                this.username = username;
                System.out.println("Connect successful");
                errorLabel.setText("Entering lobby");
                errorLabel.setOpacity(1);
                // todo load new scene
            }
            else if (response instanceof UsernameNotValidMessage m) {
                Printer.printError(m.getMessage());
                errorLabel.setText(m.getMessage());
                errorLabel.setOpacity(1);

                FadeTransition errorFadeOut = new FadeTransition(Duration.seconds(3), errorLabel);
                errorFadeOut.setFromValue(1);
                errorFadeOut.setToValue(0);
                errorFadeOut.setCycleCount(1);
                errorFadeOut.setAutoReverse(false);

                PauseTransition showError = new PauseTransition(Duration.seconds(3));
                showError.setOnFinished(event -> {
                    errorFadeOut.play();
                });

                showError.play();
            }
            else {
                addMessage(response);
            }
        });

        primaryStage.show();
    }
}
