package application;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class musicto extends Application {

    private Stage window;
    private MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            window = primaryStage;
            window.setTitle("DELIVERIES FOR ABC COMPANY");

            // GridPane
            GridPane grid = new GridPane();
            grid.setPadding(new Insets(20, 10, 10, 20));
            grid.setVgap(5);
            grid.setHgap(10);

            // Play music
            playMusic();

            Scene scene = new Scene(grid, 1200, 750);
//            scene.getStylesheets().add("CSS/design.css");
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            System.out.println("Exception in Application start method: " + e.getMessage());
            e.printStackTrace();  // Print the stack trace for any exception that occurs during startup
        }
    }


    private void playMusic() {
        try {
            String audioFilePath = "C:\\Users\\Joshua\\git\\BRAINZ\\PDBrain\\resources\\ere.mp3";
            File audioFile = new File(audioFilePath);
            if (!audioFile.exists()) {
                System.out.println("Audio file not found at path: " + audioFile.getAbsolutePath());
                return;
            }
            System.out.println("Found audio file at: " + audioFile.getAbsolutePath());
            Media media = new Media(audioFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(() -> {
                mediaPlayer.play();
                System.out.println("Playing audio from: " + audioFile.getAbsolutePath());
            });
            mediaPlayer.setOnError(() -> {
                MediaException error = mediaPlayer.getError();
                System.out.println("Error occurred while playing media: " + error.getMessage());
            });
        } catch (MediaException e) {
            System.out.println("MediaException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();  // Print the stack trace for any exception that occurs during music playback
        }
    }
}


