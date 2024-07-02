package application;
//hello
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Application;
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

public class Homepage extends Application {

    private Stage window;
    public static boolean loggedIn = false; // Track login status

    private Button musicButton; // Declare logoutButton as an instance variable
    private boolean musicPlaying = true;
    
    
    String url = "jdbc:mysql://localhost:3306/brainzmcq_mysql";
    String username = "root";
    String password = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        window = primaryStage;
        window.setTitle("BRAINZZZ");
        

        GridPane grid = createGridPane();
        grid.getStyleClass().add("root-gridpane"); 
        setupGridPane(grid);

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("./CSS/loginStyle.css");

        window.setScene(scene);
        window.show();
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

        ImageView logo = new ImageView(new Image("/Images/Brainzz_logo.png"));
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
        if (musicPlaying) {

            musicButton.getStyleClass().remove("homepage-sound-buttons");
            musicButton.getStyleClass().add("homepage-no-sound-buttons");
            musicPlaying = false;
            
            // Code to pause music playback
            
        } else {
            // Remove the previous class and add the new one
            musicButton.getStyleClass().remove("homepage-no-sound-buttons");
            musicButton.getStyleClass().add("homepage-sound-buttons");
            musicPlaying = true;
            
            // Code to start or resume music playback
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
        try {
        	Stage leaderboardStage = new Stage();
            new Leaderboard().start(leaderboardStage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    

    private void resetLoggedInStatus(String url, String username, String password) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlReset = "UPDATE logs SET loggedIn = 0";
            try (PreparedStatement statement = connection.prepareStatement(sqlReset)) {
                int rowsUpdated = statement.executeUpdate();
                System.out.println("Rows updated to loggedIn = 0: " + rowsUpdated);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
