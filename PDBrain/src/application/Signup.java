package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Signup extends Application {

    Stage window;
    
    ImageView enterUsername = new ImageView(new Image("./Images/Enter_namee.png"));
    ImageView enterPassword = new ImageView(new Image("./Images/Enter_passwordd.png"));
    ImageView confirmPassword = new ImageView(new Image("./Images/Confirm_password.png"));


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	   primaryStage.initStyle(StageStyle.TRANSPARENT);

        ImageView eyeImageView = new ImageView(new Image("./Images/Blind.png"));
        ImageView eyeImageView3 = new ImageView(new Image("./Images/Blind.png"));
        ImageView eyeImageView2 = new ImageView(new Image("./Images/Eye.png"));
        ImageView eyeImageView4 = new ImageView(new Image("./Images/Blind.png"));

        String urll= "jdbc:mysql://localhost:3306/brainzmcq_mysql";
        String username = "root";
        String password = "";

        window = primaryStage;
        window.setTitle("BRAINZZZ");

        // GridPane
        GridPane grid = new GridPane();
        grid.getStyleClass().add("root-login-gridpane"); 
        grid.setAlignment(Pos.TOP_CENTER);

        grid.setPadding(new Insets(50, 10, 10, 20));
        grid.setVgap(5);
        grid.setHgap(10);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPrefWidth(950); 
        grid.getColumnConstraints().add(columnConstraints);
        
//        Button backButton = new Button();
//        GridPane.setMargin(backButton, new Insets(0, 0, 0, 0));
//        backButton.getStyleClass().add("back-Button");
//        
//        backButton.setOnAction(e -> {
//        	window.close();
//        });
//        
//        GridPane.setHalignment(backButton, HPos.LEFT);
//        GridPane.setConstraints(backButton, 0, 0, 1, 5);
        
        
        ImageView logo = new ImageView(new Image("/Images/logooo.png"));
        GridPane.setHalignment(logo, HPos.CENTER);
        grid.addRow(6, logo);
        GridPane.setMargin(logo, new Insets(-30, 0, 0, 0));

        // Name Input
        HBox nameLabel = new HBox();
        nameLabel.getChildren().add(enterUsername);
        GridPane.setConstraints(nameLabel, 0, 8);
        nameLabel.setStyle("-fx-max-width: 500");
        nameLabel.setAlignment(Pos.CENTER_LEFT); 
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        GridPane.setMargin(nameLabel, new Insets(-30, 0, 0, 0));

        GridPane.setMargin(nameLabel, new Insets(0, 0, 0, 0));

        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter Username");
        GridPane.setHalignment(nameInput, HPos.CENTER);
        nameInput.getStyleClass().add("username-field-container");
        GridPane.setConstraints(nameInput, 0, 9);
//        GridPane.setMargin(nameInput, new Insets(-30, 0, 0, 0));

        // Password Input
        HBox passLabel = new HBox();
        passLabel.getChildren().add(enterPassword);
        passLabel.setAlignment(Pos.CENTER_LEFT); 
        GridPane.setHalignment(passLabel, HPos.CENTER);
        passLabel.setStyle("-fx-max-width: 500");
        GridPane.setConstraints(passLabel, 0, 11);
        GridPane.setMargin(passLabel, new Insets(6, 0, -5, 0));

        PasswordField passInput = new PasswordField();
        passInput.setPromptText("Enter Password");
        passInput.setPrefHeight(35);
        passInput.setPrefWidth(500);
        passInput.getStyleClass().add("password-field");

        eyeImageView2.setFitHeight(24);
        eyeImageView2.setFitWidth(24);
        eyeImageView2.setPreserveRatio(true);

        Text passText = new Text();
        passText.setVisible(false); // Initially invisible
        passText.getStyleClass().add("toggle-password"); // Add CSS styling if needed
        
        StackPane passPane = new StackPane(passInput, passText, eyeImageView);
        passPane.setAlignment(Pos.CENTER_LEFT);
        passPane.getStyleClass().add("password-field");
        GridPane.setConstraints(passPane, 0, 12);
        
        // HBox to contain PasswordField and ImageView
        HBox passBox = new HBox(passPane, eyeImageView); // Include passText in the HBox
        
      
        passBox.getStyleClass().add("password-field-container");
        GridPane.setConstraints(passBox, 0, 12);
        passBox.setAlignment(Pos.CENTER_LEFT);
        GridPane.setHalignment(passBox, HPos.CENTER);

 
        
        eyeImageView.setOnMousePressed(event -> {
            if (passInput.isVisible()) {
                passText.setText(passInput.getText());
                passText.setVisible(true);
                passBox.requestFocus();
                passInput.setVisible(false);
                eyeImageView.setImage(new Image("/Images/Eye.png"));
            } else {
                passInput.setText(passText.getText());
                passInput.setVisible(true);
                passInput.requestFocus();
                passText.setVisible(false);
                eyeImageView.setImage(new Image("/Images/Blind.png"));
            }
        });

        // Confirm Password Input
        HBox confirmLabel = new HBox();
        confirmLabel.getChildren().add(confirmPassword);
        confirmLabel.setAlignment(Pos.CENTER_LEFT); 
        GridPane.setHalignment(confirmLabel, HPos.CENTER);
        confirmLabel.setStyle("-fx-max-width: 500");
        GridPane.setConstraints(confirmLabel, 0, 14);
        GridPane.setMargin(confirmLabel, new Insets(10, 0, 0, 0));

        PasswordField confirmInput = new PasswordField();
        confirmInput.setPromptText("Confirm Password");
        confirmInput.setPrefHeight(35);
        confirmInput.setPrefWidth(500);
        confirmInput.getStyleClass().add("password-field");
        
        eyeImageView4.setFitHeight(24);
        eyeImageView4.setFitWidth(24);
        eyeImageView4.setPreserveRatio(true);

        Text passText2 = new Text();
        passText2.setVisible(false); // Initially invisible
        passText2.getStyleClass().add("toggle-password"); // Add CSS styling if needed
        
        StackPane passPane2 = new StackPane(confirmInput, passText2, eyeImageView3);
        passPane2.setAlignment(Pos.CENTER_LEFT);
        passPane2.getStyleClass().add("password-field");
        GridPane.setConstraints(passPane2, 0, 15);
        
        HBox confirmPassBox = new HBox(passPane2, eyeImageView3);
        
        confirmPassBox.getStyleClass().add("password-field-container");
        GridPane.setConstraints(confirmPassBox, 0, 15);
        confirmPassBox.setAlignment(Pos.CENTER_LEFT);
        GridPane.setHalignment(confirmPassBox, HPos.CENTER);
        
        eyeImageView3.setOnMousePressed(event -> {
            if (confirmInput.isVisible()) {
                passText2.setText(passInput.getText());
                passText2.setVisible(true);
                confirmPassBox.requestFocus();
                confirmInput.setVisible(false);
                eyeImageView3.setImage(new Image("/Images/Eye.png"));
            } else {
            	confirmInput.setText(passText.getText());
            	confirmInput.setVisible(true);
            	confirmInput.requestFocus();
                passText2.setVisible(false);
                eyeImageView3.setImage(new Image("/Images/Blind.png"));
            }
        });

        // Sign Up Button
        Button signUpButton = new Button();
        signUpButton.setPrefWidth(250);
        signUpButton.getStyleClass().add("login-button");
        HBox hboxsignup = new HBox(signUpButton);
        hboxsignup.setAlignment(Pos.CENTER);
        GridPane.setConstraints(hboxsignup, 0, 20);

        // Sign Up Action
        signUpButton.setOnAction(e -> {
            signUp(nameInput, passInput, confirmInput, nameLabel, passLabel, confirmLabel, passBox, confirmPassBox, urll, username, password);
        });

        // Enter key triggers sign up
        nameInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUp(nameInput, passInput, confirmInput, nameLabel, passLabel, confirmLabel, passBox, confirmPassBox, urll, username, password);
            }
        });
        
        nameInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUp(nameInput, passInput, confirmInput, nameLabel, passLabel, confirmLabel, passBox, confirmPassBox, urll, username, password);
            }
        });

        passInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUp(nameInput, passInput, confirmInput, nameLabel, passLabel, confirmLabel, passBox, confirmPassBox, urll, username, password);
            }
        });

        confirmInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUp(nameInput, passInput, confirmInput, nameLabel, passLabel, confirmLabel, passBox, confirmPassBox, urll, username, password);
            }
        });

        // Login Link
        Label logLabel = new Label("Already Have an Account? ");
        Hyperlink logInLink = new Hyperlink("Log-in");
        logInLink.getStyleClass().add("hyperlink-sign-up");
        logLabel.getStyleClass().add("label-sign-up");
        

        HBox hboxlogin = new HBox(logLabel, logInLink);
        hboxlogin.setAlignment(Pos.CENTER);
        GridPane.setConstraints(hboxlogin, 0, 21);

        logInLink.setOnAction(e -> {
            try {
                window.close();
                Stage loginStage = new Stage();
                new Login().start(loginStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Focus listener for styling
        nameInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                nameInput.getStyleClass().add("password-field-container-active");
            } else {
                nameInput.getStyleClass().remove("password-field-container-active");
            }
        });

        passInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                passBox.getStyleClass().add("password-field-container-active");
            } else {
                passBox.getStyleClass().remove("password-field-container-active");
            }
        });

        confirmInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                confirmPassBox.getStyleClass().add("password-field-container-active");
            } else {
                confirmPassBox.getStyleClass().remove("password-field-container-active");
            }
        });

        // Adding components to grid
        grid.getChildren().addAll(
//        		backButton,
                nameLabel, nameInput,
                passLabel, passBox,
                confirmLabel, confirmPassBox,
                hboxsignup,
                hboxlogin
        );

        // Scene and CSS
        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("./CSS/loginStyle.css");

        window.setScene(scene);
        window.show();
    }

    private void signUp(TextField nameInput, PasswordField passInput, PasswordField confirmInput,
    		HBox nameLabel, HBox passLabel, HBox confirmLabel,
                        HBox passBox, HBox confirmPassBox, String url, String username, String password) {
    	
        String name = nameInput.getText();
        String passwordValue = passInput.getText();
        String confirmValue = confirmInput.getText();

        boolean valid = true;

        if (name.isEmpty()) {
            nameInput.setStyle("-fx-border-color: red;");
            nameLabel.setStyle("-fx-text-fill: red;");
            nameLabel.setAlignment(Pos.CENTER_LEFT);
            nameLabel.setStyle("-fx-max-width: 500");
            enterUsername.setImage(new Image("./Images/Wrong_name.png"));
            shake(nameInput);
            valid = false;
        } else if (passwordValue.isEmpty()) {
            passBox.setStyle("-fx-border-color: red;");
            passLabel.setStyle("-fx-text-fill: red;");
            passLabel.setAlignment(Pos.CENTER_LEFT);
            passLabel.setStyle("-fx-max-width: 500");
            enterPassword.setImage(new Image("./Images/Wrong_password.png"));
            shake(passInput);
            valid = false;
        } else if (confirmValue.isEmpty()) {
            confirmPassBox.setStyle("-fx-border-color: red;");
            confirmLabel.setStyle("-fx-text-fill: red;");
            confirmLabel.setAlignment(Pos.CENTER_LEFT);
            confirmLabel.setStyle("-fx-max-width: 500");
            confirmPassword.setImage(new Image("./Images/Confirm_password_no_input.png"));
            shake(confirmInput);
            valid = false;

        } else if (!passwordValue.equals(confirmValue)) {
            passBox.setStyle("-fx-border-color: red;");
            passLabel.setStyle("-fx-text-fill: red;");
            shake(passInput);
            confirmPassBox.setStyle("-fx-border-color: red;");
            confirmLabel.setStyle("-fx-text-fill: red;");

            passLabel.setAlignment(Pos.CENTER_LEFT);
            passLabel.setStyle("-fx-max-width: 500");
            enterPassword.setImage(new Image("./Images/Password_do_not_match.png"));
            GridPane.setMargin(passLabel, new Insets(6, 0, 0, 0));

            confirmLabel.setAlignment(Pos.CENTER_LEFT);
            confirmLabel.setStyle("-fx-max-width: 500");
            confirmPassword.setImage(new Image("./Images/Password_do_not_match.png"));
            shake(confirmInput);
            valid = false;
        }

        if (valid) {
            try {
                String hashedPassword = BCrypt.withDefaults().hashToString(12, passwordValue.toCharArray());
                Connection connection = DriverManager.getConnection(url, username, password);
                String checkSql = "SELECT * FROM `logs` WHERE name = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setString(1, name);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    nameInput.setStyle("-fx-border-color: red;");
                    nameLabel.setStyle("-fx-text-fill: red;");
                    nameLabel.setAlignment(Pos.CENTER_LEFT);
                    nameLabel.setStyle("-fx-max-width: 500");
                    enterUsername.setImage(new Image("./Images/Username_taken.png"));
                    GridPane.setMargin(nameLabel, new Insets(0, 0, 2, 0));
                    shake(nameInput);
                } else {
                    // Insert into logs table and retrieve generated keys
                    String insertSql = "INSERT INTO `logs` (name, password) VALUES (?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
                    insertStatement.setString(1, name);
                    insertStatement.setString(2, hashedPassword);

                    int rowsInserted = insertStatement.executeUpdate();

                    // Retrieve generated keys
                    int userID = -1;
                    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        userID = generatedKeys.getInt(1);
                    }

                    // Close resources
                    insertStatement.close();
                    generatedKeys.close();

                    if (rowsInserted > 0 && userID != -1) {
                        // Insert initial score into score table
                        insertScore(userID, 0);

                        System.out.println("Account Created");

                        // Close signup window and open login window
                        window.close();
                        Stage homeStage = new Stage();
                        
                        new Homepage().start(homeStage);
                    }
                }

                resultSet.close();
                checkStatement.close();
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Connection failed or error in SQL statement!");
                ex.printStackTrace();
            }
        }

    }
        private void shake(TextField textField) {
            TranslateTransition tt = new TranslateTransition(Duration.millis(50), textField);
            tt.setFromX(0);
            tt.setByX(10);
            tt.setCycleCount(6);
            tt.setAutoReverse(true);
            tt.play();
        }
     // hello
        private void insertScore(int userID, int score) {
            String url = "jdbc:mysql://localhost:3306/brainzmcq_mysql";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String insertSql = "INSERT INTO score (UserID, overall_score, en_score, sci_score, math_score) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setInt(1, userID);
                insertStatement.setInt(2, score);
                insertStatement.setInt(3, 0); // Provide a default value for en_score
                insertStatement.setInt(4, 0); 
                insertStatement.setInt(5, 0); 

                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Score inserted successfully.");
                }
            } catch (SQLException ex) {
                System.out.println("Error inserting score: " + ex.getMessage());
            }
        }


        
}