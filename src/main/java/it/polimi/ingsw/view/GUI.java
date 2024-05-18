package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.*;

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
        String backgroundPath = "file:src/main/resources/images/background.jpg";
        Image backgroundImage = new Image(backgroundPath);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0,
                        BackgroundSize.AUTO, true, true, false, false));
        Pane backgroundPane = new Pane();
        backgroundPane.setBackground(new Background(background));



        Text text = new Text("Welcome to Code Naturalis");

        text.setFont(new Font("Arial", 40));
        text.setFill(Color.WHITE);

        VBox textBox = new VBox(text);
        textBox.setAlignment(Pos.CENTER);
        textBox.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 20px");

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundPane, textBox);
        StackPane.setAlignment(textBox, Pos.CENTER);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Code Naturalis");

        StackPane.setAlignment(text, Pos.CENTER);



        primaryStage.show();
    }
}
