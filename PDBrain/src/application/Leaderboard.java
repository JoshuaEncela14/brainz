package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Leaderboard extends Application {

    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("BRAINZZZ");

        String leadText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi elementum luctus placerat. Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet";
        String[] ranking = new String[10];
        String[] names = {"Enzon", "Comia", "Aniciete", "Encela"};
        String[] scores = {"950", "870", "869", "714"};
        String[] medals = {"/Images/1st_place_medal.png", "/Images/2nd_place_medal.png", "/Images/3rd_place_medal.png"};

        for (int i = 0; i < ranking.length; i++) {
            ranking[i] = "# " + (i + 1);
        }

        GridPane grid = createGridPane();
        Label leaderboard = createLabel("LEADERBOARD", "label-leaderboard", 0, 1);
        Label leader = createLabel(leadText, "label-under-leaderboard", 0, 2);

        HBox rank1Container = createRank1Container(ranking[0], names[0], medals[0], scores[0], "rank1-label-leaderboard",
                "rank1ContainerLeft-leaderboard", "rank1ContainerRight-leaderboard", 0, 3);

        HBox rank2Container = createRankContainer(ranking[1], names[1], medals[1], scores[1], "ranking-label-leaderboard",
                "rankingContainerLeft-leaderboard", "rankingContainerRight-leaderboard", 0, 4);
        
        HBox rank3Container = createRankContainer(ranking[2], names[2], medals[2], scores[2], "ranking-label-leaderboard",
                "rankingContainerLeft-leaderboard", "rankingContainerRight-leaderboard", 0, 5);
        
        HBox rank4Container = createNoRankContainer(ranking[3], names[3], scores[3], "ranking-label-leaderboard",
                "rankingContainerLeft-leaderboard", "rankingContainerRight-leaderboard", 0, 6);

        VBox leaderboardContainer = new VBox(20);
        leaderboardContainer.getChildren().addAll(leaderboard, leader, rank1Container, rank2Container, rank3Container, rank4Container);
        leaderboardContainer.setAlignment(Pos.TOP_CENTER);
        leaderboardContainer.getStyleClass().add("leaderboard-container");

        grid.getChildren().addAll(leaderboardContainer);
        GridPane.setConstraints(leaderboardContainer, 0, 0);
        VBox.setMargin(rank2Container, new Insets(-5, 0, 0, 0));
        VBox.setMargin(rank3Container, new Insets(-5, 0, 0, 0));
        VBox.setMargin(rank4Container, new Insets(-5, 0, 0, 0));
        GridPane.setMargin(leaderboardContainer, new Insets(-10, 0, 0, 0));

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("design.css");
        window.setScene(scene);
        window.show();
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 10, 10, 20));
        grid.setVgap(10);
        grid.setHgap(10);
        return grid;
    }

    private Label createLabel(String text, String styleClass, int columnIndex, int rowIndex) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        GridPane.setConstraints(label, columnIndex, rowIndex);
        return label;
    }

    private HBox createRank1Container(String rankText, String name, String medalUrl, String score,
                                     String rankLabelStyleClass, String leftContainerStyleClass,
                                     String rightContainerStyleClass, int columnIndex, int rowIndex) {
        HBox leftContainer = new HBox();
        Label rankLabel = new Label(rankText);
        rankLabel.getStyleClass().add(rankLabelStyleClass);
        leftContainer.getChildren().add(rankLabel);
        leftContainer.getStyleClass().add(leftContainerStyleClass);
        leftContainer.setAlignment(Pos.CENTER);

        HBox rightContainer = new HBox(10);
        rightContainer.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("rank1-label-right-leaderboard");

        ImageView medalImageView = new ImageView(new Image(getClass().getResourceAsStream(medalUrl)));
        HBox.setMargin(medalImageView, new Insets(0, 0, 0, 160));

        Label scoreLabel = new Label(" " + score);
        scoreLabel.getStyleClass().add("rank1-label-right-score-leaderboard");

        rightContainer.getChildren().addAll(nameLabel, medalImageView, scoreLabel);
        rightContainer.getStyleClass().add(rightContainerStyleClass);

        HBox rankContainer = new HBox(20);
        rankContainer.setAlignment(Pos.CENTER);
        rankContainer.getChildren().addAll(leftContainer, rightContainer);
        GridPane.setConstraints(rankContainer, columnIndex, rowIndex);

        return rankContainer;
    }
    
    
    private HBox createRankContainer(String rankText, String name, String medalUrl, String score,
						            String rankLabelStyleClass, String leftContainerStyleClass,
						            String rightContainerStyleClass, int columnIndex, int rowIndex) {
		HBox leftContainer = new HBox();
		Label rankLabel = new Label(rankText);
		rankLabel.getStyleClass().add(rankLabelStyleClass);
		leftContainer.getChildren().add(rankLabel);
		leftContainer.getStyleClass().add(leftContainerStyleClass);
		leftContainer.setAlignment(Pos.CENTER);
		
		HBox rightContainer = new HBox(10);
		rightContainer.setAlignment(Pos.CENTER_LEFT);
		
		Label nameLabel = new Label(name);
		nameLabel.getStyleClass().add("ranking-label-right-leaderboard");
		
		ImageView medalImageView = new ImageView(new Image(getClass().getResourceAsStream(medalUrl)));
		HBox.setMargin(medalImageView, new Insets(0, 0, 0, 160));
		
		Label scoreLabel = new Label(" " + score);
		scoreLabel.getStyleClass().add("ranking-label-right-score-leaderboard");
		
		rightContainer.getChildren().addAll(nameLabel, medalImageView, scoreLabel);
		rightContainer.getStyleClass().add(rightContainerStyleClass);
		
		HBox rankContainer = new HBox(20);
		rankContainer.setAlignment(Pos.CENTER);
		rankContainer.getChildren().addAll(leftContainer, rightContainer);
		GridPane.setConstraints(rankContainer, columnIndex, rowIndex);
		
		return rankContainer;
		}
    
    private HBox createNoRankContainer(String rankText, String name, String score,
						            String rankLabelStyleClass, String leftContainerStyleClass,
						            String rightContainerStyleClass, int columnIndex, int rowIndex) {
			HBox leftContainer = new HBox();
			Label rankLabel = new Label(rankText);
			rankLabel.getStyleClass().add(rankLabelStyleClass);
			leftContainer.getChildren().add(rankLabel);
			leftContainer.getStyleClass().add(leftContainerStyleClass);
			leftContainer.setAlignment(Pos.CENTER);
			
			HBox rightContainer = new HBox(10);
			rightContainer.setAlignment(Pos.CENTER_LEFT);
			
			Label nameLabel = new Label(name);
			nameLabel.getStyleClass().add("ranking-label-right-leaderboard");
			


			
			Label scoreLabel = new Label(" " + score);
			scoreLabel.getStyleClass().add("ranking-label-right-score-leaderboard");
			HBox.setMargin(scoreLabel, new Insets(0, 0, 0, 185));
			
			rightContainer.getChildren().addAll(nameLabel, scoreLabel);
			rightContainer.getStyleClass().add(rightContainerStyleClass);
			
			HBox rankContainer = new HBox(20);
			rankContainer.setAlignment(Pos.CENTER);
			rankContainer.getChildren().addAll(leftContainer, rightContainer);
			GridPane.setConstraints(rankContainer, columnIndex, rowIndex);
			
			return rankContainer;
		}
}
