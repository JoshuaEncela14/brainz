package application;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Homepage extends Application {

    private Stage window;
    public static boolean loggedIn = false; // Track login status

    private Button musicButton; // Declare logoutButton as an instance variable
    private boolean musicPlaying = true;

    String url = "jdbc:mysql://localhost:3306/brainzmcq_mysql";
    String username = "root";
    String password = "";

    // Singleton instance for MediaPlayer
    private static MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        window = primaryStage;
        window.setTitle("BRAINZZZ");

        // Load and play music
        music();

        GridPane grid = createGridPane();
        grid.getStyleClass().add("root-gridpane");
        setupGridPane(grid);

        Scene scene = new Scene(grid, 960, 520);

        String cssPath = "/CSS/loginStyle.css";
        scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());

        window.setScene(scene);
        window.show();
    }

    public void music() {
        String audioFilePath = "C:\\Users\\Joshua\\git\\BRAINZ\\PDBrain\\resources\\ere.mp3";
        Media audioFile = new Media(new File(audioFilePath).toURI().toString());

        // Check if mediaPlayer is already initialized
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer(audioFile);

            // Set up looping
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        }

        // Check if music is not playing
        if (!mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            mediaPlayer.play();
        }
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(0, 10, 10, 20));
        grid.setVgap(5);
        grid.setHgap(10);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPrefWidth(950);
        grid.getColumnConstraints().add(columnConstraints);

        return grid;
    }

    private void setupGridPane(GridPane grid) {
        HBox headerContainer = createHeaderContainer();
        GridPane.setHalignment(headerContainer, HPos.CENTER);
        GridPane.setConstraints(headerContainer, 0, 0);

        ImageView logo = new ImageView(new Image(getClass().getResource("/Images/Brainzz_logo.png").toExternalForm()));
        GridPane.setHalignment(logo, HPos.CENTER);
        GridPane.setMargin(logo, new Insets(-30, 0, 0, 0));
        GridPane.setConstraints(logo, 0, 1);

        VBox buttonsContainer = createButtonsContainer();
        GridPane.setHalignment(buttonsContainer, HPos.CENTER);
        GridPane.setConstraints(buttonsContainer, 0, 2);
        GridPane.setMargin(buttonsContainer, new Insets(-30, 0, 0, 0));

        grid.getChildren().addAll(headerContainer, logo, buttonsContainer);
    }

    private HBox createHeaderContainer() {
        Button logoutButton = createButton("homepage-logout-buttons", this::handlelogoutButton);
        Button leaderboardButton = createButton("homepage-leaderboard-buttons", this::handleLeaderboardButton);
        musicButton = createButton("homepage-sound-buttons", this::handleSoundButton);
        Button exitButton = createButton("homepage-quit-buttons", this::handleExitButton);

        HBox leftButtonContainer = new HBox();
        leftButtonContainer.getChildren().add(logoutButton);
        leftButtonContainer.setAlignment(Pos.CENTER_LEFT);

        HBox rightButtonContainer = new HBox();
        rightButtonContainer.getChildren().addAll(leaderboardButton, musicButton, exitButton);
        rightButtonContainer.setAlignment(Pos.CENTER_RIGHT);

        HBox buttonsHeaderContainer = new HBox();

        buttonsHeaderContainer.setAlignment(Pos.CENTER); // Center the header container
        buttonsHeaderContainer.setPadding(new Insets(10, 10, 10, 10)); // Add padding if needed
        HBox.setHgrow(rightButtonContainer, Priority.ALWAYS); // Ensure right buttons push to the right
        buttonsHeaderContainer.getChildren().addAll(leftButtonContainer, rightButtonContainer);

        return buttonsHeaderContainer;
    }

    private Button createButton(String styleClass, Runnable action) {
        Button button = new Button();
        button.getStyleClass().add(styleClass);
        button.setOnAction(e -> action.run());
        return button;
    }

    private VBox createButtonsContainer() {
        Button playButton = createButton("homepage-play-buttons", this::handlePlayButton);

        VBox buttonsContainer = new VBox(10);
        buttonsContainer.setAlignment(Pos.CENTER);

        buttonsContainer.getChildren().addAll(playButton);

        return buttonsContainer;
    }

    private void handleExitButton() {
        window.close();
    }

    private void handlelogoutButton() {
        resetLoggedInStatus(url, username, password);

        try {
            Stage LoginStage = new Stage();
            new Login().start(LoginStage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleSoundButton() {
        if (mediaPlayer != null) {
            if (musicPlaying) {
                musicButton.getStyleClass().remove("homepage-sound-buttons");
                musicButton.getStyleClass().add("homepage-no-sound-buttons");
                mediaPlayer.pause();
                musicPlaying = false;
            } else {
                musicButton.getStyleClass().remove("homepage-no-sound-buttons");
                musicButton.getStyleClass().add("homepage-sound-buttons");
                mediaPlayer.play();
                musicPlaying = true;
            }
        }
    }

    private void handlePlayButton() {
        try {
            window.close();
            Stage catStage = new Stage();
            new Categories().start(catStage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleLeaderboardButton() {
    	Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(url, username, password);

            // Calculate total scores for each subject and update overall_score
            String updateScoresQuery = "UPDATE score s " +
                    "JOIN logs l ON s.UserId = l.id " +
                    "SET " +
                    "s.en_total_score = COALESCE(NULLIF(s.en_stage3_score, 0), NULLIF(s.en_stage2_score, 0), s.en_stage1_score, 0), " +
                    "s.sci_total_score = COALESCE(NULLIF(s.sci_stage3_score, 0), NULLIF(s.sci_stage2_score, 0), s.sci_stage1_score, 0), " +
                    "s.math_total_score = COALESCE(NULLIF(s.math_stage3_score, 0), NULLIF(s.math_stage2_score, 0), s.math_stage1_score, 0), " +
                    "s.overall_score = COALESCE(NULLIF(s.en_stage3_score, 0), NULLIF(s.en_stage2_score, 0), s.en_stage1_score, 0) + " +
                    "COALESCE(NULLIF(s.sci_stage3_score, 0), NULLIF(s.sci_stage2_score, 0), s.sci_stage1_score, 0) + " +
                    "COALESCE(NULLIF(s.math_stage3_score, 0), NULLIF(s.math_stage2_score, 0), s.math_stage1_score, 0)";

            stmt = conn.prepareStatement(updateScoresQuery);
            int rowsUpdated = stmt.executeUpdate();

            System.out.println("Overall scores updated for all users.");

            // Ascend the table based on the total score and print the top 4 rows with user names
            String leaderboardQuery = "SELECT l.id AS UserId, l.name, s.overall_score " +
                    "FROM score s " +
                    "JOIN logs l ON s.UserId = l.id " +
                    "ORDER BY s.overall_score DESC LIMIT 4";

            stmt = conn.prepareStatement(leaderboardQuery);
            ResultSet rs = stmt.executeQuery();

            // Printing the top 4 users on the leaderboard
            while (rs.next()) {
                int userId = rs.getInt("UserId");
                String name = rs.getString("name");
                int overallScore = rs.getInt("overall_score");
                System.out.println("UserId: " + userId + ", Name: " + name + ", Overall Score: " + overallScore);
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
        try {
            Stage LeaderboardStage = new Stage();
            new Leaderboard().start(LeaderboardStage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }

    // Method to reset logged-in status
    private void resetLoggedInStatus(String url, String username, String password) {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String resetQuery = "UPDATE logs SET LoggedIn = 0 WHERE LoggedIn = 1";
            PreparedStatement stmt = conn.prepareStatement(resetQuery);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Logged out successfully.");
                loggedIn = false; // Update local status
            }
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
