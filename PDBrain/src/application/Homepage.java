package application;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Homepage extends Application {

    private Stage window;
    public static boolean loggedIn = false; // Track login status

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        window = primaryStage;
        window.setTitle("BRAINZZZ");

        GridPane grid = createGridPane();
        setupGridPane(grid);

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("./CSS/loginStyle.css");

        window.setScene(scene);
        window.show();
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(0, 10, 10, 20));
        grid.setVgap(5);
        grid.setHgap(10);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPrefWidth(950);
        grid.getColumnConstraints().add(columnConstraints);

        return grid;
    }

    private void setupGridPane(GridPane grid) {
        Button settingsButton = createButton("settings-button", this::handleSettingsButton);
        GridPane.setHalignment(settingsButton, HPos.LEFT);
        GridPane.setConstraints(settingsButton, 0, 0);

        ImageView logo = new ImageView(new Image("/Images/Brainzz_logo.png"));
        GridPane.setHalignment(logo, HPos.CENTER);
        GridPane.setMargin(logo, new Insets(-80, 0, 0, 0));
        GridPane.setConstraints(logo, 0, 1);

        VBox buttonsContainer = createButtonsContainer();
        GridPane.setHalignment(buttonsContainer, HPos.CENTER);
        GridPane.setConstraints(buttonsContainer, 0, 2);
        GridPane.setMargin(buttonsContainer, new Insets(-30, 0, 0, 0));

        grid.getChildren().addAll(settingsButton, logo, buttonsContainer);
    }

    private Button createButton(String styleClass, Runnable action) {
        Button button = new Button();
        button.getStyleClass().add(styleClass);
        button.setOnAction(e -> action.run());
        return button;
    }

    private VBox createButtonsContainer() {
        Button playButton = createButton("homepage-play-buttons", this::handlePlayButton);
        Button leaderboardButton = createButton("homepage-leaderboard-buttons", this::handleLeaderboardButton);
        Button quitButton = createButton("homepage-quit-buttons", window::close);

        VBox buttonsContainer = new VBox(10);
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.getChildren().addAll(playButton, leaderboardButton, quitButton);

        return buttonsContainer;
    }

    private void handleSettingsButton() {
        window.close();
    }

    private void handlePlayButton() {
        if (Homepage.loggedIn) {
            try {
                window.close();
                Stage catStage = new Stage();
                new Categories().start(catStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                window.close();
                Stage loginStage = new Stage();	
                new Login().start(loginStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleLeaderboardButton() {
        try {
            window.close();
            Stage leaderboardStage = new Stage();
            new Leaderboard().start(leaderboardStage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
