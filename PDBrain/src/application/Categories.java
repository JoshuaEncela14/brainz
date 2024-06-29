package application;
 
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Categories extends Application {

    private Stage window;
    private String category;
    private int difficultyId;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("BRAINZZZ");

        showCategorySelection();
    }

    private void showCategorySelection() {
        // GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(20, 10, 10, 20));
        grid.setVgap(5);
        grid.setHgap(10);

        // Load categories from the database
        List<String> categories = loadCategories();

        // Create buttons for each category
        HBox hboxCategory = new HBox(10); // Add spacing between buttons
        hboxCategory.setAlignment(Pos.CENTER);
        hboxCategory.getStyleClass().add("hbox-category");

        for (String category : categories) {
            Button categoryButton = new Button(category);
            categoryButton.setPrefWidth(250);
            categoryButton.setOnAction(e -> {
                for (Button btn : hboxCategory.getChildren().filtered(node -> node instanceof Button).toArray(Button[]::new)) {
                    btn.getStyleClass().setAll("button");
                }
                categoryButton.getStyleClass().setAll("button-active");

                // Set selected category and show stage selection
                this.category = category;
               // showStageSelection();
            });
            hboxCategory.getChildren().add(categoryButton);
        }

        GridPane.setConstraints(hboxCategory, 0, 0);
        GridPane.setColumnSpan(hboxCategory, 5);
        hboxCategory.setStyle(
                "-fx-background-color: #F5F5F5;" +
                        "-fx-padding: 5;" +
                        "-fx-spacing: 5;" +
                        "-fx-background-radius: 4;"
        );

        // Stage buttons
        Button stageOne = new Button("Stage 1");
        stageOne.setPrefWidth(140);
        stageOne.setPrefHeight(75);
        stageOne.getStyleClass().add("button-with-background");
        stageOne.setOnAction(e -> openQuestionsStage("Stage 1"));

        Button stageTwo = new Button("Stage 2");
        stageTwo.setPrefWidth(140);
        stageTwo.setPrefHeight(75);
        stageTwo.setOnAction(e -> openQuestionsStage("Stage 2"));

        Button stageThree = new Button("Stage 3");
        stageThree.setPrefWidth(140);
        stageThree.setPrefHeight(75);
        stageThree.setOnAction(e -> openQuestionsStage("Stage 3"));

        Button stageFour = new Button("Stage 4");
        stageFour.setPrefWidth(140);
        stageFour.setPrefHeight(75);
        // No action for Stage 4 as per requirements

        Button stageFive = new Button("Stage 5");
        stageFive.setPrefWidth(140);
        stageFive.setPrefHeight(75);
        // No action for Stage 5 as per requirements

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
        GridPane.setColumnSpan(vboxStages, 5);
        GridPane.setValignment(vboxStages, javafx.geometry.VPos.CENTER);

        grid.getChildren().addAll(hboxCategory, vboxStages);

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("categories.css");
        window.setScene(scene);
        window.show();
    }

    private List<String> loadCategories() {
        List<String> categories = new ArrayList<>();

        categories.add("English");
        categories.add("Math");
        categories.add("Science");

        return categories;
    }

//    private void showStageSelection() {
//        // Additional UI changes for stage selection if needed
//    }

    private void openQuestionsStage(String stage) {
        switch (stage) {
            case "Stage 1":
                difficultyId = 1;
                break;
            case "Stage 2":
                difficultyId = 2;
                break;
            case "Stage 3":
                difficultyId = 3;
                break;
            default:
                throw new IllegalArgumentException("Invalid stage: " + stage);
        }

        Questions questions = new Questions(category, difficultyId);
        try {
            questions.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        window.close();
    }
}
