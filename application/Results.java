package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Results extends Application {

    private Stage questionStage;
    private int score;
    private String selectedCategory;
    private int selectedDifficulty;
    private Button retakeButton;

    public Results(Stage questionStage, int score, String selectedCategory, int selectedDifficulty) {
        this.questionStage = questionStage;
        this.score = score;
        this.selectedCategory = selectedCategory;
        this.selectedDifficulty = selectedDifficulty;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        GridPane grid = createGridPane();
        grid.getStyleClass().add("result-parent-grid");

        Label stageLevel = createLabel("LEVEL 1", "analysis-labels");
        HBox stageContainer = createHBox(stageLevel, Pos.CENTER);

        HBox stars = createStarsHBox(score);

        Label congratulatory = createLabel("COMPLETED", "congrats-labels");
        HBox congratsContainer = createHBox(congratulatory, Pos.CENTER);

        HBox timeContent = createTimeContent(25);

        HBox scoreContent = createScoreContent(score, 5);

        HBox resultButtons = createResultButtons(primaryStage);

        VBox resultContainer = new VBox(10);
        resultContainer.setAlignment(Pos.CENTER);
        resultContainer.getStyleClass().add("modal-dialog");
        GridPane.setConstraints(resultContainer, 0, 8);
        resultContainer.getChildren().addAll(stageContainer, stars, congratsContainer, timeContent, scoreContent, resultButtons);

        grid.getChildren().addAll(resultContainer);

        Scene scene = new Scene(grid, 300, 500);
//        scene.getStylesheets().add("/CSS/design.css");
        scene.getStylesheets().add(getClass().getResource("/CSS/design.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);
        return grid;
    }

    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    private HBox createHBox(Label label, Pos alignment) {
        HBox hbox = new HBox();
        hbox.getChildren().add(label);
        hbox.setAlignment(alignment);
        return hbox;
    }

    private HBox createStarsHBox(int score) {
//        ImageView starGood = new ImageView(new Image("/Images/yeyStar.png"));
//        ImageView star2Good = new ImageView(new Image("/Images/yeyStar.png"));
//        star2Good.setTranslateY(-30);
//        ImageView star3Good = new ImageView(new Image("/Images/yeyStar.png"));
//
//        ImageView star2Bad = new ImageView(new Image("/Images/notYeyStar.png"));
//        star2Bad.setTranslateY(-30);
//        ImageView star3Bad = new ImageView(new Image("/Images/notYeyStar.png"));
        
        ImageView starGood = new ImageView(getClass().getResource("/Images/yeyStar.png").toExternalForm());
        ImageView star2Good = new ImageView(getClass().getResource("/Images/yeyStar.png").toExternalForm());
      star2Good.setTranslateY(-30);	
        ImageView star3Good = new ImageView(getClass().getResource("/Images/yeyStar.png").toExternalForm());
        
        ImageView star2Bad = new ImageView(getClass().getResource("/Images/notYeyStar.png").toExternalForm());
        star2Bad.setTranslateY(-30);
        ImageView star3Bad = new ImageView(getClass().getResource("/Images/notYeyStar.png").toExternalForm());
//        ImageView image = new ImageView(getClass().getResource("/Images/" + imageFileName).toExternalForm());

        HBox stars = new HBox(10);
        stars.setAlignment(Pos.CENTER);

        if (score == 5) {
            stars.getChildren().addAll(rotateImage(starGood, -10), star2Good, rotateImage(star3Good, 10));
        } else if (score >= 3) {
            stars.getChildren().addAll(rotateImage(starGood, -10), star2Good, rotateImage(star3Bad, 10));
        } else {
            stars.getChildren().addAll(rotateImage(starGood, -10), star2Bad, rotateImage(star3Bad, 10));
            
        }

        stars.setPadding(new Insets(30, 0, 10, 0));
        return stars;
    }

    private HBox createTimeContent(int timeLeft) {
//        ImageView timerLogo = new ImageView(new Image("/Images/Timer.png"));
        ImageView timerLogo = new ImageView(getClass().getResource("/Images/Timer.png").toExternalForm());
        Label answerLabel = new Label(timeLeft + "S");
        answerLabel.getStyleClass().add("timer-labels");

        HBox timeContent = new HBox(15);
        timeContent.getStyleClass().add("Hbox-time-content");
        timeContent.getChildren().addAll(timerLogo, answerLabel);
        timeContent.setPadding(new Insets(0, 10, 0, 10));
        timeContent.setAlignment(Pos.CENTER_LEFT);
        return timeContent;
    }

    private HBox createScoreContent(int totalScore, int overAllScore) {
//        ImageView totalScoreLogo = new ImageView(new Image("/Images/TotalScore.png"));
        ImageView totalScoreLogo = new ImageView(getClass().getResource("/Images/TotalScore.png").toExternalForm());
        Label totalScoreLabel = new Label(totalScore + "/" + overAllScore);
        totalScoreLabel.getStyleClass().add("timer-labels");

        HBox scoreContent = new HBox(15);
        scoreContent.getStyleClass().add("Hbox-time-content");
        scoreContent.getChildren().addAll(totalScoreLogo, totalScoreLabel);
        scoreContent.setPadding(new Insets(0, 10, 0, 10));
        scoreContent.setAlignment(Pos.CENTER_LEFT);
        return scoreContent;
    }

    private HBox createResultButtons(Stage primaryStage) {
//        ImageView home = new ImageView(new Image("/Images/Exit.png"));
//        ImageView retake = new ImageView(new Image("/Images/Retake.png"));
//        ImageView advance = new ImageView(new Image("/Images/Advance.png"));
        
        ImageView home = new ImageView(getClass().getResource("/Images/Exit.png").toExternalForm());
        ImageView retake = new ImageView(getClass().getResource("/Images/Retake.png").toExternalForm());
        ImageView advance = new ImageView(getClass().getResource("/Images/Advance.png").toExternalForm());
        
        

        Button homeButton = createButton(home, "Result-buttons", e -> {
            try {
                Scene buttonScene = home.getScene();
                if (buttonScene != null) {
                    Stage currentStage = (Stage) buttonScene.getWindow();
                    
                    if (currentStage != null) {
                        currentStage.close();
                        questionStage.close();
                    } else {
                        System.err.println("Failed to retrieve current stage");
                    }
                    
                    Stage categoryStage = new Stage();
                    
                    Categories categories = new Categories();
                    categories.start(categoryStage);
                } else {
                    System.err.println("Button is not attached to a scene");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        retakeButton = createButton(retake, "Result-buttons", e -> {
            // Close the current Results and previous Questions stages
            primaryStage.close();
            questionStage.close();

            // Retake the questions with the same category and difficulty
            Questions newQuestions = new Questions(selectedCategory, selectedDifficulty);
            try {
                newQuestions.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        
        
        Button advanceButton = createButton(advance, "Result-buttons", e -> {
            primaryStage.close();
            questionStage.close();
            Questions newQuestions = new Questions(selectedCategory, selectedDifficulty + 1);
            try {
                newQuestions.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        if (score < 3) {
        	advanceButton.setDisable(true);
        }
        
        


        HBox resultButtons = new HBox(-20);
        resultButtons.setAlignment(Pos.CENTER);
        resultButtons.getChildren().addAll(homeButton, retakeButton, advanceButton);
        return resultButtons;
    }

    private Button createButton(ImageView graphic, String styleClass, javafx.event.EventHandler<javafx.event.ActionEvent> actionEventEventHandler) {
        Button button = new Button();
        button.setGraphic(graphic);
        button.getStyleClass().add(styleClass);
        button.setOnAction(actionEventEventHandler);
        return button;
    }

    private ImageView rotateImage(ImageView imageView, double angle) {
        Rotate rotate = new Rotate(angle, 0, 0);
        imageView.getTransforms().add(rotate);
        return imageView;
    }
}
