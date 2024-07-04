package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Questions extends Application {

    private String category = "english";

    private static final String DB_URL = "jdbc:mysql://localhost:3306/brainzmcq_mysql";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private String[] imageFiles = {"A.png", "B.png", "C.png", "D.png"};

    private Stage window;
    private int currentNumber = 0;
    private Timeline timeline;
    int newScore = 0;

    private List<Question> questions = new ArrayList<>();
    private Label questionLabel;
    private Label currentQuestionLabel;
    private Button[] optionButtons;

    private String selectedCategory;
    private int selectedDifficulty;

    public Questions(String selectedCategory, int selectedDifficulty) {
        this.selectedCategory = selectedCategory;
        this.selectedDifficulty = selectedDifficulty;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("BRAINZZZ");

        // Load questions from the database
        loadQuestionsFromDB(selectedCategory, selectedDifficulty);

        GridPane grid = createGridPane();
        HBox header = createHeader();
        HBox timer = createTimer();
        VBox questionOptions = createQuestionOptions();

        grid.getChildren().addAll(header, timer, createQuestionContainer(), questionOptions);

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("/CSS/design.css");
        window.setScene(scene);
        window.show();

        // Show first question
        updateQuestion();
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);

        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setPercentWidth(100);
        grid.getColumnConstraints().add(colConstraints);

        return grid;
    }

    private HBox createHeader() {
        Button lifelineOne = createLifelineButton("50:50");
        Button lifelineTwo = createLifelineButton("Pause");

        HBox lifelines = new HBox(10, lifelineOne, lifelineTwo);
        lifelines.setAlignment(Pos.CENTER);
        HBox.setMargin(lifelines, new Insets(0, 0, 0, 10));

        currentQuestionLabel = new Label((currentNumber + 1) + " / " + questions.size());
        currentQuestionLabel.getStyleClass().add("label-question");

        HBox questionTracking = new HBox(currentQuestionLabel);
        questionTracking.getStyleClass().add("hbox-question-tracking");
        questionTracking.setAlignment(Pos.CENTER);
        questionTracking.setSpacing(10);

        HBox header = new HBox(10, lifelines, questionTracking);
        header.getStyleClass().add("hbox-questions-header");
        header.setAlignment(Pos.CENTER_LEFT);

        GridPane.setConstraints(header, 0, 0, GridPane.REMAINING, 1);

        return header;
    }

    private Button createLifelineButton(String text) {
        Button lifelineButton = new Button(text);
        lifelineButton.setPrefWidth(90);
        lifelineButton.setPrefHeight(40);
        lifelineButton.getStyleClass().add("lifeline-buttons");

        lifelineButton.setOnAction(event -> {
            if (text.equals("Pause")) {
                pauseTimer();
                lifelineButton.setDisable(true);
            } else if (text.equals("50:50")) {
                executeFiftyFiftyLifeline();
                lifelineButton.setDisable(true); // Disable lifeline after use
            }
        });

        return lifelineButton;
    }

    private void executeFiftyFiftyLifeline() {
        String correctAnswer = questions.get(currentNumber).getCorrectAnswer();
        int correctIndex = -1;

        // Find the index of the correct answer
        for (int i = 0; i < optionButtons.length; i++) {
            HBox optionContent = (HBox) optionButtons[i].getGraphic();
            Label answerLabel = (Label) optionContent.getChildren().get(1);
            if (answerLabel.getText().equals(correctAnswer)) {
                correctIndex = i;
                break;
            }
        }

        // Disable two incorrect options
        List<Integer> disableIndices = new ArrayList<>();
        for (int i = 0; i < optionButtons.length; i++) {
            if (i != correctIndex) {
                disableIndices.add(i);
            }
        }

        // Randomly disable two incorrect options
        for (int j = 0; j < 2; j++) {
            int randomIndex = (int) (Math.random() * disableIndices.size());
            int indexToDisable = disableIndices.get(randomIndex);
            optionButtons[indexToDisable].setDisable(true);
            disableIndices.remove(randomIndex); // Ensure unique disables
        }
    }
    
    private void handleTimeout() {
        // Disable all buttons
        for (Button btn : optionButtons) {
            btn.setDisable(true);
        }

        // Start a delay to move to the next question or results
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(ev -> {
            currentNumber++;
            if (currentNumber < questions.size()) {
                updateQuestion();
                resetTimer();
                for (Button btn : optionButtons) {
                    btn.setStyle("");
                    btn.setDisable(false);
                }
            } else {
                Results resultWindow = new Results(window, newScore);
                try {
                    resultWindow.start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        pause.play();
    }


    private HBox createTimer() {
        HBox timer = new HBox();
        timer.getStyleClass().add("hbox-questions-timer");

        Rectangle timerBar = new Rectangle(960, 5, Color.web("#960A0A"));
        timer.getChildren().add(timerBar);

        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(timerBar.widthProperty(), 960)),
                new KeyFrame(Duration.seconds(30), e -> {
                    timerBar.setWidth(0); // Reset timer bar
                    handleTimeout();
                }, new KeyValue(timerBar.widthProperty(), 0))
        );
        timeline.setCycleCount(1);

        GridPane.setConstraints(timer, 0, 1, GridPane.REMAINING, 1);

        return timer;
    }
    


    private void pauseTimer() {
        timeline.pause();

        Timeline resumeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> timeline.play())
        );
        resumeTimeline.play();
    }

    private VBox createQuestionOptions() {
        optionButtons = new Button[4];
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = createOptionButton(imageFiles[i]);
        }

        for (Button button : optionButtons) {
            button.setOnAction(e -> {
                Button clickedButton = (Button) e.getSource();
                HBox optionContent = (HBox) clickedButton.getGraphic();
                Label answerLabel = (Label) optionContent.getChildren().get(1);

                // Check if the answer is correct
                if (answerLabel.getText().equals(questions.get(currentNumber).getCorrectAnswer())) {
                    clickedButton.setStyle("-fx-background-color: green");
                    // Update score when answer is correct
                    newScore ++;
                    updateScore(selectedCategory, newScore);
                } else {
                    clickedButton.setStyle("-fx-background-color: red");
                }

                // Highlight the correct answer
                for (Button btn : optionButtons) {
                    HBox content = (HBox) btn.getGraphic();
                    Label label = (Label) content.getChildren().get(1);
                    if (label.getText().equals(questions.get(currentNumber).getCorrectAnswer())) {
                        btn.setStyle("-fx-background-color: green");
                    }
                }

                // Disable all buttons
                for (Button btn : optionButtons) {
                    btn.setDisable(true);
                }

                resetTimer();

                // Start a delay to move to the next question
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(ev -> {
                    currentNumber++;
                    if (currentNumber < questions.size()) {
                        updateQuestion();
                        for (Button btn : optionButtons) {
                            btn.setStyle("");
                            btn.setDisable(false);
                        }
                    } else {
                    	timeline.stop();
                        Results resultWindow = new Results(window, newScore);
                        try {
                            resultWindow.start(new Stage());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                pause.play();
            });
        }

        HBox optionOneThree = new HBox(125, optionButtons[0], optionButtons[2]);
        optionOneThree.getStyleClass().add("hbox-question-optionAC");
        optionOneThree.setAlignment(Pos.CENTER);

        HBox optionTwoFour = new HBox(125, optionButtons[1], optionButtons[3]);
        optionTwoFour.getStyleClass().add("hbox-question-optionAC");
        optionTwoFour.setAlignment(Pos.CENTER);

        VBox questionOptions = new VBox(25, optionOneThree, optionTwoFour);
        questionOptions.getStyleClass().add("hbox-question-optionAC");
        questionOptions.setAlignment(Pos.CENTER);

        GridPane.setConstraints(questionOptions, 0, 3, GridPane.REMAINING, 1);

        return questionOptions;
    }

    private Button createOptionButton(String imageFileName) {
        Button optionButton = new Button();
        optionButton.getStyleClass().add("label-question-option");

        HBox optionContent = new HBox(10);
        optionContent.setAlignment(Pos.CENTER_LEFT);

        Image image = new Image(getClass().getResourceAsStream("/images/" + imageFileName));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        Label answerLabel = new Label();
        answerLabel.getStyleClass().add("label-question-answer");

        optionContent.getChildren().addAll(imageView, answerLabel);
        optionContent.setPadding(new Insets(0, 10, 0, 10));

        optionButton.setGraphic(optionContent);

        return optionButton;
    }

    private HBox createQuestionContainer() {
        questionLabel = new Label();
        questionLabel.getStyleClass().add("label-question-label");
        questionLabel.setLineSpacing(10);

        HBox questionContainer = new HBox(questionLabel);
        questionContainer.setAlignment(Pos.CENTER);
        questionContainer.getStyleClass().add("hbox-questions-container");

        GridPane.setConstraints(questionContainer, 0, 2, GridPane.REMAINING, 1);
        GridPane.setMargin(questionContainer, new Insets(30, 0, 30, 0));
        GridPane.setHalignment(questionContainer, HPos.CENTER);

        return questionContainer;
    }

    private void updateQuestion() {
        if (currentNumber < questions.size()) {
        	
            timeline.stop(); // Stop previous timeline if any
            timeline.playFromStart(); // Restart the timer
            
            Question currentQuestion = questions.get(currentNumber);
            questionLabel.setText(currentQuestion.getQuestionText());
            currentQuestionLabel.setText((currentNumber + 1) + " / " + questions.size());

            List<String> choices = currentQuestion.getChoices();
            for (int i = 0; i < optionButtons.length; i++) {
                HBox optionContent = (HBox) optionButtons[i].getGraphic();
                Label answerLabel = (Label) optionContent.getChildren().get(1);
                answerLabel.setText(choices.get(i));
                optionButtons[i].setStyle("");
                optionButtons[i].setDisable(false);

                // Clear previous styles
                optionButtons[i].setStyle("");
            }
        }
    }

    private void resetTimer() {
        timeline.stop();

        // Reset timer bar width to initial state
        for (KeyValue keyValue : timeline.getKeyFrames().get(1).getValues()) {
            if (keyValue.getTarget() instanceof Rectangle) {
                Rectangle timerBar = (Rectangle) keyValue.getTarget();
                timerBar.setWidth(960);
                break;
            }
        }

        Timeline pauseTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeline.play();
        }));
        pauseTimeline.play();
    }

    private void loadQuestionsFromDB(String category, int difficulty) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT q.questions, c.choice, c.is_correct " +
                     "FROM questions q " +
                     "JOIN choices c ON q.id = c.question_id " +
                     "WHERE q.category_id = (SELECT id FROM categories WHERE category_name = '" + category + "') " +
                     "AND q.difficulty_id = " + difficulty)) {

            Question currentQuestion = null;
            while (rs.next()) {
                String questionText = rs.getString("questions");
                String choice = rs.getString("choice");
                boolean isCorrect = rs.getBoolean("is_correct");

                if (currentQuestion == null || !currentQuestion.getQuestionText().equals(questionText)) {
                    if (currentQuestion != null) {
                        questions.add(currentQuestion);
                    }
                    currentQuestion = new Question(questionText);
                }
                currentQuestion.addChoice(choice, isCorrect);
            }
            if (currentQuestion != null) {
                questions.add(currentQuestion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    private void updateScore(String category, int newScore) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String scoreColumn = "";
        String stageColumn = "";

        switch (category) {
            case "english":
                scoreColumn = "en_stage" + selectedDifficulty + "_score";
                stageColumn = "en_stage";
                break;
            case "math":
                scoreColumn = "math_stage" + selectedDifficulty + "_score";
                stageColumn = "math_stage";
                break;
            case "science":
                scoreColumn = "sci_stage" + selectedDifficulty + "_score";
                stageColumn = "sci_stage";
                break;
            default:
                // Handle unexpected category (if needed)
                return;
        }

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Retrieve userId from logs where loggedIn is 1
            String userIdQuery = "SELECT id FROM logs WHERE loggedIn = 1";
            stmt = conn.prepareStatement(userIdQuery);
            ResultSet rs = stmt.executeQuery();

            int userId = -1; // Default value for userId
            if (rs.next()) {
                userId = rs.getInt("id");
            } else {
                System.out.println("No userId found for loggedIn = 1");
                return;
            }

            // Get current score for the user
            String currentScoreQuery = "SELECT " + scoreColumn + " FROM score WHERE UserId = ?";
            stmt = conn.prepareStatement(currentScoreQuery);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int currentScore = rs.getInt(scoreColumn);

                // Update score only if the new score is higher than the current score
                if (newScore > currentScore) {
                    String updateScoreQuery = "UPDATE score SET " + scoreColumn + " = ? WHERE UserId = ?";
                    stmt = conn.prepareStatement(updateScoreQuery);
                    stmt.setInt(1, newScore);
                    stmt.setInt(2, userId);
                    int rowsUpdated = stmt.executeUpdate();
                    System.out.println(category + " stage " + selectedDifficulty + " score updated to " + newScore + " for user with userId: " + userId);
                } else {
                    System.out.println(category + " stage " + selectedDifficulty + " score not updated. Current score (" + currentScore + ") is not lower than new score (" + newScore + ")");
                }

                // Handle cumulative score for stage 2
                if (selectedDifficulty == 2) {
                    String stage1ScoreColumn = scoreColumn.replace("_stage2_", "_stage1_");
                    String stage1ScoreQuery = "SELECT " + stage1ScoreColumn + " FROM score WHERE UserId = ?";
                    stmt = conn.prepareStatement(stage1ScoreQuery);
                    stmt.setInt(1, userId);
                    rs = stmt.executeQuery();

                    if (rs.next()) {
                        int stage1Score = rs.getInt(stage1ScoreColumn);

                        // Calculate cumulative score
                        int cumulativeScore = stage1Score + newScore;

                        // Update stage 2 score with cumulative score
                        String updateStage2ScoreQuery = "UPDATE score SET " + scoreColumn + " = ? WHERE UserId = ?";
                        stmt = conn.prepareStatement(updateStage2ScoreQuery);
                        stmt.setInt(1, cumulativeScore);
                        stmt.setInt(2, userId);
                        int rowsUpdated = stmt.executeUpdate();
                        System.out.println(category + " stage 2 cumulative score updated to " + cumulativeScore + " for user with userId: " + userId);
                    } else {
                        System.out.println("No stage 1 score found for user with userId: " + userId);
                    }
                }

                // Handle cumulative score for stage 3
                if (selectedDifficulty == 3) {
                    String stage2ScoreColumn = scoreColumn.replace("_stage3_", "_stage2_");
                    String stage2ScoreQuery = "SELECT " + stage2ScoreColumn + " FROM score WHERE UserId = ?";
                    stmt = conn.prepareStatement(stage2ScoreQuery);
                    stmt.setInt(1, userId);
                    rs = stmt.executeQuery();

                    if (rs.next()) {
                        int stage2Score = rs.getInt(stage2ScoreColumn);

                        // Calculate cumulative score
                        int cumulativeScore = stage2Score + newScore;

                        // Update stage 3 score with cumulative score
                        String updateStage3ScoreQuery = "UPDATE score SET " + scoreColumn + " = ? WHERE UserId = ?";
                        stmt = conn.prepareStatement(updateStage3ScoreQuery);
                        stmt.setInt(1, cumulativeScore);
                        stmt.setInt(2, userId);
                        int rowsUpdated = stmt.executeUpdate();
                        System.out.println(category + " stage 3 cumulative score updated to " + cumulativeScore + " for user with userId: " + userId);
                    } else {
                        System.out.println("No stage 2 score found for user with userId: " + userId);
                    }
                }

            } else {
                System.out.println("No score found for user with userId: " + userId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

class Question {
    private String questionText;
    private List<String> choices;
    private String correctAnswer;

    public Question(String questionText) {
        this.questionText = questionText;
        this.choices = new ArrayList<>();
    }

    public void addChoice(String choice, boolean isCorrect) {
        choices.add(choice);
        if (isCorrect) {
            this.correctAnswer = choice;
        }
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}