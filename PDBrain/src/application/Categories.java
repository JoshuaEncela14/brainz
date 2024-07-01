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
    private int userId = 1; // Set this to the logged-in user's ID
    private Connection conn;

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
            });
            hboxCategory.getChildren().add(categoryButton);
        }
        
        hboxCategory.getStyleClass().add("hbox-category");

        GridPane.setConstraints(hboxCategory, 0, 0);
        GridPane.setMargin(hboxCategory, new Insets(0, 0, 0, 40));

     // Stage buttons
        Button stageOne = new Button();
        stageOne.setPrefWidth(140);
        stageOne.setPrefHeight(75);
        stageOne.getStyleClass().add("stage_one");
        stageOne.setOnAction(e -> openQuestionsStage("Stage 1"));
        addHoverEffect(stageOne);

        Button stageTwo = new Button();
        stageTwo.setPrefWidth(140);
        stageTwo.setPrefHeight(75);
        stageTwo.getStyleClass().add("stage_two");
        stageTwo.setOnAction(e -> openQuestionsStage("Stage 2"));
        addHoverEffect(stageTwo);

        Button stageThree = new Button();
        stageThree.setPrefWidth(140);
        stageThree.setPrefHeight(75);
        stageThree.getStyleClass().add("stage_three");
        stageThree.setOnAction(e -> openQuestionsStage("Stage 3"));
        addHoverEffect(stageThree);

        Button stageFour = new Button();
        stageFour.setPrefWidth(140);
        stageFour.setPrefHeight(75);
        stageFour.getStyleClass().add("stage_four");
        stageFour.setOnAction(e -> openQuestionsStage("Stage 4"));
        addHoverEffect(stageFour);

        Button stageFive = new Button();
        stageFive.setPrefWidth(140);
        stageFive.setPrefHeight(75);
        stageFive.getStyleClass().add("stage_five");
        stageFive.setOnAction(e -> openQuestionsStage("Stage 5"));
        addHoverEffect(stageFive);

        // HBox for the first row of stage buttons
        HBox hboxRow1 = new HBox(10);
        hboxRow1.setAlignment(Pos.CENTER);
        hboxRow1.getChildren().addAll(stageOne, stageTwo, stageFour, stageThree, stageFive);
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

        // Check scores and disable stages accordingly
        checkStageUnlocks(stageOne, stageTwo, stageThree, stageFour, stageFive);

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("/CSS/categories.css");
        window.setScene(scene);
        window.show();
    }

    private Button createStageButton(String stageName) {
        Button stageButton = new Button(stageName);
        stageButton.setPrefWidth(140);
        stageButton.setPrefHeight(75);
        stageButton.setOnAction(e -> openQuestionsStage(stageName));
        addHoverEffect(stageButton);
        return stageButton;
    }

    private List<String> loadCategories() {
        List<String> imagePaths = new ArrayList<>();
        imagePaths.add("/Images/Categories_english.png");
        imagePaths.add("/Images/Categories_math.png");
        imagePaths.add("/Images/Categories_science.png");
        return imagePaths;
    }

    private void checkStageUnlocks(Button stageOne, Button stageTwo, Button stageThree, Button stageFour, Button stageFive) {
        try {
            String sql = "SELECT stageId, overall_score FROM score WHERE UserID = ? AND category = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, category);
            ResultSet rs = pstmt.executeQuery();

            int[] scores = new int[5];
            while (rs.next()) {
                int stageId = rs.getInt("stageId");
                int overallScore = rs.getInt("overall_score");
                scores[stageId - 1] = overallScore;
            }

         // Unlocking logic for Stage 2
	        if (scores[0] >= 3 && scores[0] <= 5) {
	            stageTwo.setDisable(false);
	        } else {
	            stageTwo.setDisable(true);
	        }
	     // Additional unlocking logic can be added here
            stageThree.setDisable(scores[1] < 15); // Example score requirement for stage 3
            stageFour.setDisable(scores[2] < 15); // Example score requirement for stage 4
            stageFive.setDisable(scores[3] < 15); // Example score requirement for stage 5
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
    
    public void recordScore(int userId, String category, int stageId, int score) {
	    try {
	        String sql = "INSERT INTO score (UserID, category, stageId, overall_score) VALUES (?, ?, ?, ?) " +
	                "ON DUPLICATE KEY UPDATE overall_score = VALUES(overall_score)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, userId);
	        pstmt.setString(2, category);
	        pstmt.setInt(3, stageId);
	        pstmt.setInt(4, score);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
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
>>>>>>> branch 'main' of https://github.com/JoshuaEncela14/brainz.git
