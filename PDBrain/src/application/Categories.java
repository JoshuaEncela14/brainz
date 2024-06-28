package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Categories extends Application {

    Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("BRAINZZZ");

        // GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(20, 10, 10, 20));
        grid.setVgap(5);
        grid.setHgap(10);

        Button engUpButton = new Button("English");
        engUpButton.setPrefWidth(250);
        engUpButton.getStyleClass().add("button-active");

        Button mathUpButton = new Button("Mathematics");
        mathUpButton.setPrefWidth(250);

        Button scieButton = new Button("Science");
        scieButton.setPrefWidth(250);

        engUpButton.setOnAction(e -> {
            engUpButton.getStyleClass().setAll("button-active");
            mathUpButton.getStyleClass().setAll("button");
            scieButton.getStyleClass().setAll("button");
        });

        mathUpButton.setOnAction(e -> {
            engUpButton.getStyleClass().setAll("button");
            mathUpButton.getStyleClass().setAll("button-active");
            scieButton.getStyleClass().setAll("button");
        });

        scieButton.setOnAction(e -> {
            engUpButton.getStyleClass().setAll("button");
            mathUpButton.getStyleClass().setAll("button");
            scieButton.getStyleClass().setAll("button-active");
        });

        Button stageOne = new Button("Stage 1");
        stageOne.setPrefWidth(140);
        stageOne.setPrefHeight(75);
        stageOne.getStyleClass().add("button-with-background"); // Apply CSS class correctly
        
        stageOne.setOnAction(e -> {
            try {
                window.close();

                Stage QuestionStage = new Stage();
                new Questions().start(QuestionStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button stageTwo = new Button("Stage 2");
        stageTwo.setPrefWidth(140);
        stageTwo.setPrefHeight(75);

        Button stageThree = new Button("Stage 3");
        stageThree.setPrefWidth(140);
        stageThree.setPrefHeight(75);

        Button stageFour = new Button("Stage 4");
        stageFour.setPrefWidth(140);
        stageFour.setPrefHeight(75);

        Button stageFive = new Button("Stage 5");
        stageFive.setPrefWidth(140);
        stageFive.setPrefHeight(75);

        // HBox for the first row of stage buttons
        HBox hboxRow1 = new HBox(144);
        hboxRow1.setAlignment(Pos.CENTER);
        hboxRow1.getChildren().addAll(stageOne, stageThree, stageFive);

        // HBox for the second row of stage buttons
        HBox hboxRow2 = new HBox(144);
        hboxRow2.setAlignment(Pos.CENTER);
        hboxRow2.getChildren().addAll(stageTwo, stageFour);

        // VBox to combine both HBoxes
        VBox vboxStages = new VBox(50);
        vboxStages.setAlignment(Pos.CENTER);
        vboxStages.getChildren().addAll(hboxRow1, hboxRow2);

        // Add VBox to the GridPane
        GridPane.setConstraints(vboxStages, 0, 5);
        GridPane.setColumnSpan(vboxStages, 5); // Span across 5 columns
        GridPane.setValignment(vboxStages, VPos.CENTER);

        HBox hboxCategory = new HBox(10); // Add spacing between buttons
        hboxCategory.setAlignment(Pos.CENTER);
        hboxCategory.getChildren().addAll(engUpButton, mathUpButton, scieButton);
        GridPane.setConstraints(hboxCategory, 0, 0);
        GridPane.setColumnSpan(hboxCategory, 5); //
        hboxCategory.getStyleClass().add("hbox-category");

        hboxCategory.setStyle(
                "-fx-background-color: #F5F5F5;" +
                        "-fx-padding: 5;" +
                        "-fx-spacing: 5;" +
                        "-fx-background-radius: 4;"
        );

        grid.getChildren().addAll(hboxCategory, vboxStages);

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("categories.css");
        window.setScene(scene);
        window.show();
    }
}
