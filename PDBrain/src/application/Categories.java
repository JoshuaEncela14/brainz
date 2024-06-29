package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
		grid.setPadding(new Insets(20, 10, 10, 20));
		grid.setVgap(5);
		grid.setHgap(10);

		// Load categories from the database
		List<String> categories = loadCategories();

		// Create buttons for each category
		HBox hboxCategory = new HBox(10);
		hboxCategory.setAlignment(Pos.CENTER);
		hboxCategory.getStyleClass().add("hbox-category");

		for (String category : categories) {
			Button categoryButton = new Button(category);
			categoryButton.setPrefWidth(250);
			categoryButton.setOnAction(e -> {
				for (Button btn : hboxCategory.getChildren().filtered(node -> node instanceof Button).toArray(Button[]::new)) {
					btn.getStyleClass().setAll("button");
				}
				categoryButton.getStyleClass().setAll("button-active");

				// Set selected category and show stage selection
				this.category = category;
				// showCategorySelection();
			});
			hboxCategory.getChildren().add(categoryButton);
		}

		GridPane.setConstraints(hboxCategory, 0, 0);
		GridPane.setColumnSpan(hboxCategory, 5);
		hboxCategory.setStyle(
				"-fx-background-color: #F5F5F5;" +
						"-fx-padding: 5;" +
						"-fx-spacing: 5;" +
						"-fx-background-radius: 4;"
				);

		// Stage buttons
		Button stageOne = new Button("Stage 1");
		stageOne.setPrefWidth(140);
		stageOne.setPrefHeight(75);
		stageOne.getStyleClass().add("button-with-background");
		stageOne.setOnAction(e -> openQuestionsStage("Stage 1"));

		Button stageTwo = new Button("Stage 2");
		stageTwo.setPrefWidth(140);
		stageTwo.setPrefHeight(75);
		stageTwo.setOnAction(e -> openQuestionsStage("Stage 2"));

		Button stageThree = new Button("Stage 3");
		stageThree.setPrefWidth(140);
		stageThree.setPrefHeight(75);
		stageThree.setOnAction(e -> openQuestionsStage("Stage 3"));

		Button stageFour = new Button("Stage 4");
		stageFour.setPrefWidth(140);
		stageFour.setPrefHeight(75);
		stageFour.setOnAction(e -> openQuestionsStage("Stage 4"));

		Button stageFive = new Button("Stage 5");
		stageFive.setPrefWidth(140);
		stageFive.setPrefHeight(75);
		stageFive.setOnAction(e -> openQuestionsStage("Stage 5"));

		// HBox for the first row of stage buttons
		HBox hboxRow1 = new HBox(144);
		hboxRow1.setAlignment(Pos.CENTER);
		hboxRow1.getChildren().addAll(stageOne, stageThree, stageFive);

		// HBox for the second row of stage buttons
		HBox hboxRow2 = new HBox(144);
		hboxRow2.setAlignment(Pos.CENTER);
		hboxRow2.getChildren().addAll(stageTwo, stageFour);

		// VBox to combine both HBoxes
		VBox vboxStages = new VBox(50);
		vboxStages.setAlignment(Pos.CENTER);
		vboxStages.getChildren().addAll(hboxRow1, hboxRow2);

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

	private List<String> loadCategories() {
		List<String> categories = new ArrayList<>();

		categories.add("English");
		categories.add("Math");
		categories.add("Science");

		return categories;
	}

<<<<<<< HEAD
	private void checkStageUnlocks(Button stageOne, Button stageTwo, Button stageThree, Button stageFour, Button stageFive) {
		try {
			String sql = "SELECT stageId, overall_score FROM score WHERE UserID = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			int[] scores = new int[5];
			while (rs.next()) {
				int stageId = rs.getInt("stageId");
				int overallScore = rs.getInt("overall_score");
				scores[stageId - 1] = overallScore;
			}

			if (scores[0] < 50) stageTwo.setDisable(true); // Example score requirement for stage 2
			if (scores[1] < 70) stageThree.setDisable(true); // Example score requirement for stage 3
			if (scores[2] < 85) stageFour.setDisable(true); // Example score requirement for stage 4
			if (scores[3] < 90) stageFive.setDisable(true); // Example score requirement for stage 5
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

		Questions questions = new Questions(category, difficultyId);
		try {
			questions.start(new Stage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		window.close();
	}
}
=======
        Questions questions = new Questions(category, difficultyId);
        try {
            questions.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        window.close();
    }
    
    
}
>>>>>>> branch 'main' of https://github.com/JoshuaEncela14/brainz.git
