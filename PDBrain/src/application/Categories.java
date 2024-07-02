package application;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Categories extends Application {

    private Stage window;
    private String category;
    private int difficultyId;
    private int userId; // Updated to fetch from logs table
    private Connection conn;
    
    Button stageOne;
    Button stageTwo;
    Button stageThree;
    Button stageFour;
    Button stageFive;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("BRAINZZZ");

        connectToDatabase();
        showCategorySelection();
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/brainzmcq_mysql", "root", "");
            fetchUserIdFromLogs(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchUserIdFromLogs() {
        try {
            String sql = "SELECT id FROM logs WHERE LoggedIn = 1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showCategorySelection() {
        // GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(30, 0, 10, 20));
        grid.setVgap(5);
        grid.setHgap(0);

        // Load image paths for categories
        List<String> imagePaths = loadCategories();

        // Create buttons for each category
        HBox hboxCategory = new HBox(7);
        hboxCategory.setAlignment(Pos.CENTER);

        for (String imagePath : imagePaths) {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);
            Button categoryButton = new Button();
            categoryButton.setGraphic(imageView);
            categoryButton.setPrefWidth(250);
            categoryButton.setOnAction(e -> {
                category = getCategoryFromImagePath(imagePath);
                for (Button btn : hboxCategory.getChildren().filtered(node -> node instanceof Button).toArray(Button[]::new)) {
                    btn.getStyleClass().setAll("button");
                }
                categoryButton.getStyleClass().setAll("button-active");
                // Check and enable stages based on category and scores
                checkStageUnlocks();
            });
            hboxCategory.getChildren().add(categoryButton);
        }

        hboxCategory.getStyleClass().add("hbox-category");

        GridPane.setConstraints(hboxCategory, 0, 0);
        GridPane.setMargin(hboxCategory, new Insets(0, 0, 0, 40));

        // Stage buttons
        stageOne = createStageButton("Stage 1");
        stageTwo = createStageButton("Stage 2");
        stageThree = createStageButton("Stage 3");
        stageFour = createStageButton("Stage 4");
        stageFive = createStageButton("Stage 5");

        // HBox for the first row of stage buttons
        HBox hboxRow1 = new HBox(10);
        hboxRow1.setAlignment(Pos.CENTER);
        hboxRow1.getChildren().addAll(stageOne, stageTwo, stageThree, stageFour, stageFive);
        VBox.setMargin(hboxRow1, new Insets(40, 0, 0, 0));

        // VBox to combine both HBoxes
        VBox vboxStages = new VBox(50);
        vboxStages.setAlignment(Pos.CENTER);
        vboxStages.getChildren().addAll(hboxRow1);

        // Add VBox to the GridPane
        GridPane.setConstraints(vboxStages, 0, 5);
        GridPane.setColumnSpan(vboxStages, 5);
        GridPane.setValignment(vboxStages, javafx.geometry.VPos.CENTER);

        grid.getChildren().addAll(hboxCategory, vboxStages);

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("/CSS/categories.css");
        window.setScene(scene);
        window.show();
    }

    private Button createStageButton(String stageName) {
        Button stageButton = new Button();
        stageButton.setPrefWidth(140);
        stageButton.setPrefHeight(75);
        stageButton.getStyleClass().add(getStageButtonStyle(stageName));
        stageButton.setOnAction(e -> openQuestionsStage(stageName));
        addHoverEffect(stageButton);
        stageButton.setDisable(true); // Initially disable all stage buttons
        return stageButton;
    }

    private String getStageButtonStyle(String stageName) {
        switch (stageName) {
            case "Stage 1":
                return "stage_one";
            case "Stage 2":
                return "stage_two";
            case "Stage 3":
                return "stage_three";
            case "Stage 4":
                return "stage_four";
            case "Stage 5":
                return "stage_five";
            default:
                throw new IllegalArgumentException("Invalid stage: " + stageName);
        }
    }

    private List<String> loadCategories() {
        List<String> imagePaths = new ArrayList<>();
        imagePaths.add("/Images/Categories_english.png");
        imagePaths.add("/Images/Categories_math.png");
        imagePaths.add("/Images/Categories_science.png");
        return imagePaths;
    }

    private void checkStageUnlocks() {
        try {
            String sql = "SELECT math_stage1_score, math_stage2_score, en_stage1_score, en_stage2_score, sci_stage1_score, sci_stage2_score FROM score WHERE UserId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int enStage1Score = rs.getInt("en_stage1_score");
                int enStage2Score = rs.getInt("en_stage2_score");
                int mathStage1Score = rs.getInt("math_stage1_score");
                int mathStage2Score = rs.getInt("math_stage2_score");
                int sciStage1Score = rs.getInt("sci_stage1_score");
                int sciStage2Score = rs.getInt("sci_stage2_score");

                // Reset all stage buttons first
                stageOne.setDisable(true);
                stageTwo.setDisable(true);
                stageThree.setDisable(true);
                stageFour.setDisable(true);
                stageFive.setDisable(true);

                if (category.equals("english")) {
                    stageOne.setDisable(false);
                    stageTwo.setDisable(!(enStage1Score >= 3));
                    stageThree.setDisable(!(enStage2Score >= 8));
                    stageFour.setDisable(!(enStage1Score >= 15));
                    stageFive.setDisable(!(enStage2Score >= 18));
                } else if (category.equals("math")) {
                    stageOne.setDisable(false);
                    stageTwo.setDisable(!(mathStage1Score >= 3));
                    stageThree.setDisable(!(mathStage2Score >= 8));
                    stageFour.setDisable(!(mathStage1Score >= 15));
                    stageFive.setDisable(!(mathStage2Score >= 18));
                } else if (category.equals("science")) {
                    stageOne.setDisable(false);
                    stageTwo.setDisable(!(sciStage1Score >= 3));	
                    stageThree.setDisable(!(sciStage2Score >= 8));
                    stageFour.setDisable(!(sciStage1Score >= 15));
                    stageFive.setDisable(!(sciStage2Score >= 18));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


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
            case "Stage 4":
                difficultyId = 4;
                break;
            case "Stage 5":
                difficultyId = 5;
                break;
            default:
                throw new IllegalArgumentException("Invalid stage: " + stage);
        }

        // Pass the category and difficultyId to the Questions class
        Questions questions = new Questions(category, difficultyId);
        try {
            questions.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        window.close();
    }

    private void addHoverEffect(Button button) {
        // Define scale transitions for hover effect
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), button);
        scaleUp.setToX(1.04);
        scaleUp.setToY(1.04);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(300), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Set event handlers for mouse enter and exit
        button.setOnMouseEntered(event -> scaleUp.playFromStart());
        button.setOnMouseExited(event -> scaleDown.playFromStart());
    }

    private String getCategoryFromImagePath(String imagePath) {
        if (imagePath.contains("Categories_english")) {
            return "english";
        } else if (imagePath.contains("Categories_math")) {
            return "math";
        } else if (imagePath.contains("Categories_science")) {
            return "science";
        } else {
            throw new IllegalArgumentException("Unknown category for imagePath: " + imagePath);
        }
    }
}
