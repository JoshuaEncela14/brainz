package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class music extends Application {

    private MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Music Player");

            // Load the media file
            String audioFilePath = "C:\\Users\\Joshua\\git\\BRAINZ\\PDBrain\\resources\\ere.mp3";
            File audioFile = new File(audioFilePath);
            if (!audioFile.exists()) {
                System.out.println("Audio file not found at: " + audioFile.getAbsolutePath());
                return;
            }
            Media media = new Media(audioFile.toURI().toString());

            // Create a MediaPlayer
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(() -> {
                System.out.println("Media error occurred: " + mediaPlayer.getError().getMessage());
            });

            // Play the media when it's ready
            mediaPlayer.setOnReady(() -> {
                mediaPlayer.play();
                System.out.println("Playing audio from: " + audioFile.getAbsolutePath());
            });

            // Show the stage
            Scene scene = new Scene(new javafx.scene.layout.StackPane(), 200, 100);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Exception in Application start method: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Cleanup resources if needed
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }
}
