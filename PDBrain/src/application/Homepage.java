package application;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Homepage extends Application{

	Stage window;
	
	public static void main(String[] args) {
        launch(args);
    }
	
	 @Override
	    public void start(Stage primaryStage) {
	    	
	        window = primaryStage;
	        window.setTitle("BRAINZZZ");
	        
	        // GridPane
	        GridPane grid = new GridPane();
	        grid.setAlignment(Pos.TOP_CENTER);
	        grid.setPadding(new Insets(20, 10, 10, 20));
	        grid.setVgap(5);
	        grid.setHgap(10);
	        
	        ImageView logo = new ImageView(new Image("/Images/Brainzz_logo.png"));
	        GridPane.setHalignment(logo, HPos.CENTER);
	        grid.addRow(6, logo);
	        
	        grid.getChildren().add(
	        		logo
	        );
	        
	        Scene scene = new Scene(grid, 960, 520);
//	        scene.getStylesheets().add("./CSS/loginStyle.css");
	        

	        
	        window.setScene(scene);
	        window.show();
	 }
}
