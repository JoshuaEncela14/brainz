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
    private Stage window;
    private int finalScore;
    private long[] questionTimes; // Array to store time spent on each question


    private Stage questionStage; // Reference to the Question stage
     
    public Results(Stage window, int finalScore, long[] questionTimes) {
        this.window = window;
        this.finalScore = finalScore;
        this.questionTimes = questionTimes;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // Creating main grid layout
        GridPane grid = createGridPane();
        grid.getStyleClass().add("result-parent-grid");

        // Creating star images
        ImageView starGood = new ImageView(new Image("/Images/yeyStar.png"));
        ImageView star2Good = new ImageView(new Image("/Images/yeyStar.png"));
        star2Good.setTranslateY(-30);
        ImageView star3Good = new ImageView(new Image("/Images/yeyStar.png"));

        ImageView starBad = new ImageView(new Image("/Images/notYeyStar.png"));
        ImageView star2Bad = new ImageView(new Image("/Images/notYeyStar.png"));
        star2Bad.setTranslateY(-30);
        ImageView star3Bad = new ImageView(new Image("/Images/notYeyStar.png"));

        // Creating components
        Label stageLevel = createLabel("LEVEL 1", "analysis-labels");
        HBox stageContainer = createHBox(stageLevel, Pos.CENTER);

        HBox stars = createStarsHBox(finalScore); // Update to use createStarsHBox(finalScore)

        Label congratulatory = createCongratulatoryMessage(finalScore);
        HBox congratsContainer = createHBox(congratulatory, Pos.CENTER);

        HBox timeContent = createTimeContent(25); // Pass the time left value

        HBox scoreContent = createScoreContent(finalScore, 5); // Pass totalScore and overAllScore

        HBox resultButtons = createResultButtons(primaryStage);

        // Creating result container
        VBox resultContainer = new VBox(10);
        resultContainer.setAlignment(Pos.CENTER);
        resultContainer.getStyleClass().add("modal-dialog");
        GridPane.setConstraints(resultContainer, 0, 8);
        resultContainer.getChildren().addAll(stageContainer, stars, congratsContainer, timeContent, scoreContent, resultButtons);

        // Adding result container to grid
        grid.getChildren().addAll(resultContainer);

        // Creating scene and setting style
        Scene scene = new Scene(grid, 300, 500);
        scene.getStylesheets().add("/CSS/design.css");
        scene.setFill(Color.TRANSPARENT);

        // Setting scene to stage
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


    private HBox createStarsHBox(int finalScore) {
        ImageView starGood = new ImageView(new Image("/Images/yeyStar.png"));
        ImageView star2Good = new ImageView(new Image("/Images/yeyStar.png"));
        ImageView star3Good = new ImageView(new Image("/Images/yeyStar.png"));

        ImageView starBad = new ImageView(new Image("/Images/notYeyStar.png"));
        ImageView star2Bad = new ImageView(new Image("/Images/notYeyStar.png"));
        ImageView star3Bad = new ImageView(new Image("/Images/notYeyStar.png"));

        HBox stars = new HBox(10);
        stars.setAlignment(Pos.CENTER);

        if (finalScore == 5) {
            stars.getChildren().addAll(rotateImage(starGood, -10), star2Good, rotateImage(star3Good, 10));
        } else if (finalScore >= 3) {
            stars.getChildren().addAll(rotateImage(starGood, -10), star2Good, rotateImage(star3Bad, 10));
        } else {
            stars.getChildren().addAll(rotateImage(starGood, -10), star2Bad, rotateImage(star3Bad, 10));
        }

        stars.setPadding(new Insets(30, 0, 10, 0)); // top, right, bottom, left
        return stars;
    }

    
    private HBox createTimeContent(int timeLeft) {
        ImageView timerLogo = new ImageView(new Image("/Images/Timer.png"));
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
        ImageView totalScoreLogo = new ImageView(new Image("/Images/TotalScore.png"));
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
        ImageView home = new ImageView(new Image("/Images/Exit.png"));
        ImageView retake = new ImageView(new Image("/Images/Retake.png"));
        ImageView advance = new ImageView(new Image("/Images/Advance.png"));

        Button homeButton = createButton(home, "Result-buttons", e -> {
           
            
            try {
            	 primaryStage.close();
                 questionStage.close(); 
                Stage homeStage = new Stage();
                new Homepage().start(homeStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        Button retakeButton = createButton(retake, "Result-buttons", e -> {
            primaryStage.close();
            questionStage.close();
        });
        Button advanceButton = createButton(advance, "Result-buttons", e -> {
            advanceToNextStage(primaryStage);
        });

        HBox resultButtons = new HBox(-20);
        resultButtons.setAlignment(Pos.CENTER);
        resultButtons.getChildren().addAll(homeButton, retakeButton, advanceButton);
        return resultButtons;
    }
    
    private VBox createTimeContent(long[] questionTimes) {
        VBox timeContent = new VBox(10);
        timeContent.setAlignment(Pos.CENTER_LEFT);
        timeContent.getStyleClass().add("time-content");

        for (int i = 0; i < questionTimes.length; i++) {
            Label timeLabel = new Label("Question " + (i + 1) + ": " + formatTime(questionTimes[i]));
            timeLabel.getStyleClass().add("timer-labels");
            timeContent.getChildren().add(timeLabel);
        }

        return timeContent;
    }

    private String formatTime(long milliseconds) {
        // Convert milliseconds to seconds or minutes as per your requirement
        long seconds = milliseconds / 1000;
        return seconds + " seconds";
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
    private void advanceToNextStage(Stage primaryStage) {
        try {
            Categories categories = new Categories();
            Stage stage = new Stage();
            categories.start(stage);

            primaryStage.close();
            questionStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private Label createCongratulatoryMessage(int score) {
        String message;
        if (score == 5) {
            message = "Excellent!";
        } else if (score >= 3) {
            message = "Great job!";
        } else {
            message = "Try Again";
        }
        return createLabel(message, "congrats-labels");
    }
}