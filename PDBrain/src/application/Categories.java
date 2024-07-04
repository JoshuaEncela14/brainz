package application;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Categories extends Application {

    private Stage window;
    private String category;
    private int difficultyId;
    private int userId;
    private Connection conn;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

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
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
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
                return null;
            }
        };

        new Thread(task).start();
    }

    private void showCategorySelection() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(30, 0, 10, 20));
        grid.setVgap(5);
        grid.setHgap(0);

        List<String> imagePaths = loadCategories();

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

        GridPane.setConstraints(hboxCategory, 0, 1);
        GridPane.setMargin(hboxCategory, new Insets(0, 0, 0, 40));

        stageOne = createStageButton("Stage 1");
        stageTwo = createStageButton("Stage 2");
        stageThree = createStageButton("Stage 3");
        stageFour = createStageButton("Stage 4");
        stageFive = createStageButton("Stage 5");

        HBox hboxRow1 = new HBox(10);
        hboxRow1.setAlignment(Pos.CENTER);
        hboxRow1.getChildren().addAll(stageOne, stageTwo, stageThree, stageFour, stageFive);
        VBox.setMargin(hboxRow1, new Insets(40, 0, 0, 0));

        VBox vboxStages = new VBox(50);
        vboxStages.setAlignment(Pos.CENTER);
        vboxStages.getChildren().addAll(hboxRow1);

        GridPane.setConstraints(vboxStages, 0, 5);
        GridPane.setColumnSpan(vboxStages, 5);
        GridPane.setValignment(vboxStages, javafx.geometry.VPos.CENTER);

        // Add back button
        Button backButton = new Button();
        backButton.getStyleClass().add("back-Button");
        backButton.setOnAction(e -> {
            try {
                window.close();
                Stage homeStage = new Stage();
                new Homepage().start(homeStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        GridPane.setHalignment(backButton, HPos.LEFT);
        GridPane.setConstraints(backButton, 0, 0);
        GridPane.setMargin(backButton, new Insets(0, 50, 0, -75));
//        grid.getChildren().add(backButton);
        

        grid.getChildren().addAll(backButton, hboxCategory, vboxStages);

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
        stageButton.setDisable(true);
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
        executorService.submit(() -> {
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

                    Platform.runLater(() -> {
                        stageOne.setDisable(true);
                        stageTwo.setDisable(true);
                        stageThree.setDisable(true);
                        stageFour.setDisable(true);
                        stageFive.setDisable(true);

                        ForkJoinPool forkJoinPool = new ForkJoinPool();
                        forkJoinPool.invoke(new StageUnlockTask(category, enStage1Score, enStage2Score, mathStage1Score, mathStage2Score, sciStage1Score, sciStage2Score));
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private class StageUnlockTask extends RecursiveTask<Void> {
        private final String category;
        private final int enStage1Score;
        private final int enStage2Score;
        private final int mathStage1Score;
        private final int mathStage2Score;
        private final int sciStage1Score;
        private final int sciStage2Score;

        public StageUnlockTask(String category, int enStage1Score, int enStage2Score, int mathStage1Score, int mathStage2Score, int sciStage1Score, int sciStage2Score) {
            this.category = category;
            this.enStage1Score = enStage1Score;
            this.enStage2Score = enStage2Score;
            this.mathStage1Score = mathStage1Score;
            this.mathStage2Score = mathStage2Score;
            this.sciStage1Score = sciStage1Score;
            this.sciStage2Score = sciStage2Score;
        }

        @Override
        protected Void compute() {
            switch (category) {
                case "english":
                    stageOne.setDisable(false);
                    stageTwo.setDisable(!(enStage1Score >= 3));
                    stageThree.setDisable(!(enStage2Score >= 8));
                    stageFour.setDisable(!(enStage1Score >= 15));
                    stageFive.setDisable(!(enStage2Score >= 18));
                    break;
                case "math":
                    stageOne.setDisable(false);
                    stageTwo.setDisable(!(mathStage1Score >= 3));
                    stageThree.setDisable(!(mathStage2Score >= 8));
                    stageFour.setDisable(!(mathStage1Score >= 15));
                    stageFive.setDisable(!(mathStage2Score >= 18));
                    break;
                case "science":
                    stageOne.setDisable(false);
                    stageTwo.setDisable(!(sciStage1Score >= 3));
                    stageThree.setDisable(!(sciStage2Score >= 8));
                    stageFour.setDisable(!(sciStage1Score >= 15));
                    stageFive.setDisable(!(sciStage2Score >= 18));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid category: " + category);
            }
            return null;
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

        Questions questions = new Questions(category, difficultyId);
        try {
            questions.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        window.close();
    }

    private void addHoverEffect(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), button);
        scaleUp.setToX(1.04);
        scaleUp.setToY(1.04);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(300), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

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

    @Override
    public void stop() throws Exception {
        super.stop();
        executorService.shutdown();
    }
}
