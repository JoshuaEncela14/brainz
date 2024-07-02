package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.prism.paint.Color;

import at.favre.lib.crypto.bcrypt.BCrypt;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Login extends Application {

    Stage window;
    
    ImageView enterUsername = new ImageView(new Image("./Images/Enter_name.png"));
    ImageView enterPassword = new ImageView(new Image("./Images/Enter_password.png"));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        
        ImageView eyeImageView = new ImageView(new Image("./Images/Blind.png"));
        ImageView eyeImageView2 = new ImageView(new Image("./Images/Eye.png"));
        
        String url = "jdbc:mysql://localhost:3306/brainzmcq_mysql";
        String username = "root";
        String password = "";

        window = primaryStage;
        window.setTitle("BRAINZZZ");

        GridPane grid = new GridPane();
        grid.getStyleClass().add("root-login-gridpane"); 
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(20, 10, 10, 20));
        grid.setVgap(5);
        grid.setHgap(10);
        
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPrefWidth(950);
        grid.getColumnConstraints().add(columnConstraints);

        Button backButton = new Button();
        backButton.getStyleClass().add("back-Button");
        backButton.setOnAction(e -> {
            try {
                window.close();
                Stage HomeStage = new Stage();
                new Homepage().start(HomeStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        GridPane.setHalignment(backButton, HPos.LEFT);
        GridPane.setConstraints(backButton, 0, 0);
        
        ImageView logo = new ImageView(new Image("/Images/logooo.png"));
        GridPane.setHalignment(logo, HPos.CENTER);
        grid.addRow(1, logo);

        HBox nameLabel = new HBox(enterUsername);
        nameLabel.setAlignment(Pos.CENTER_LEFT); 
        nameLabel.setStyle("-fx-max-width: 500");
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        GridPane.setConstraints(nameLabel, 0, 4);

        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter Name");
        nameInput.setPrefHeight(35);
        nameInput.setMaxWidth(500);
        nameInput.getStyleClass().add("name-field-container");
        GridPane.setHalignment(nameInput, HPos.CENTER);
        GridPane.setConstraints(nameInput, 0, 5);

        HBox passLabel = new HBox(enterPassword);
        passLabel.setAlignment(Pos.CENTER_LEFT); 
        passLabel.setStyle("-fx-max-width: 500");
        GridPane.setHalignment(passLabel, HPos.CENTER);
        GridPane.setConstraints(passLabel, 0, 7);
        GridPane.setMargin(passLabel, new Insets(10, 0, 0, 0));

        PasswordField passInput = new PasswordField();
        passInput.setPromptText("Enter Password");
        passInput.setPrefHeight(35);
        passInput.setPrefWidth(500);
        passInput.getStyleClass().add("password-field");

        eyeImageView2.setFitHeight(24);
        eyeImageView2.setFitWidth(24);
        eyeImageView2.setPreserveRatio(true);

        Text passText = new Text();
        passText.setVisible(false);
        passText.getStyleClass().add("toggle-password");
        
        StackPane passPane = new StackPane(passInput, passText, eyeImageView);
        passPane.setAlignment(Pos.CENTER_LEFT);
        passPane.getStyleClass().add("password-field");
        GridPane.setConstraints(passPane, 0, 8);

        HBox passBox = new HBox(passPane, eyeImageView);
        passBox.getStyleClass().add("password-field-container");
        passBox.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(passBox, 0, 8);
        GridPane.setHalignment(passBox, HPos.CENTER);

        eyeImageView.setOnMousePressed(event -> {
            if (passInput.isVisible()) {
                passText.setText(passInput.getText());
                passText.setVisible(true);
                passInput.setVisible(false);
                eyeImageView.setImage(new Image("/Images/Eye.png"));
            } else {
                passInput.setText(passText.getText());
                passInput.setVisible(true);
                passText.setVisible(false);
                eyeImageView.setImage(new Image("/Images/Blind.png"));
            }
        });

        Button loginButton = new Button();
        loginButton.getStyleClass().add("login-button");
        loginButton.setPrefWidth(250);

        HBox hboxlog = new HBox(loginButton);
        hboxlog.setAlignment(Pos.CENTER);
        GridPane.setConstraints(hboxlog, 0, 14);

        loginButton.getStyleClass().add("button-login");

        loginButton.setOnAction(e -> {
            login(nameInput, passInput, nameLabel, passLabel, passBox, url, username, password);
        });

        passInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login(nameInput, passInput, nameLabel, passLabel, passBox, url, username, password);
            }
        });
        
        nameInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login(nameInput, passInput, nameLabel, passLabel, passBox, url, username, password);
            }
        });

        Label signUpLabel = new Label("Don't Have an Account? ");
        Hyperlink signUpLink = new Hyperlink("Sign-up");
        signUpLink.getStyleClass().add("hyperlink-sign-up");
        signUpLabel.getStyleClass().add("label-sign-up");

        HBox signUpBox = new HBox(signUpLabel, signUpLink);
        signUpBox.setAlignment(Pos.CENTER);
        GridPane.setConstraints(signUpBox, 0, 16);

        signUpLink.setOnAction(e -> {
            try {
                window.close();
                Stage signUpStage = new Stage();
                new Signup().start(signUpStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        grid.getChildren().addAll(
                backButton,
                nameLabel, nameInput,
                passLabel, passBox,
                hboxlog,
                signUpBox
        );

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

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("./CSS/loginStyle.css");
        
        window.setScene(scene);
        window.show();
    }

    private void login(TextField nameInput, PasswordField passInput, HBox nameLabel, HBox passLabel, HBox passBox, String url, String username, String password) {
        String name = nameInput.getText();
        String passwordValue = passInput.getText();

        boolean valid = true;

        if (name.isEmpty()) {
            nameInput.setStyle("-fx-border-color: red;");
            enterUsername.setImage(new Image("./Images/Wrong_name.png"));
            shake(nameInput);
            valid = false;
        } else if (passwordValue.isEmpty()) {
            passBox.setStyle("-fx-border-color: red;");
            enterPassword.setImage(new Image("./Images/Wrong_password.png"));
            shake(passInput);
            valid = false;
        }

        if (valid) {
            Connection connection = null;
            PreparedStatement statementReset = null;
            PreparedStatement statementSelect = null;
            PreparedStatement statementUpdate = null;
            ResultSet resultSet = null;
            try {
                connection = DriverManager.getConnection(url, username, password);

                // Reset the loggedIn status for all users
                String sqlReset = "UPDATE logs SET loggedIn = 0";
                statementReset = connection.prepareStatement(sqlReset);
                int rowsUpdated = statementReset.executeUpdate();
                System.out.println("Rows updated to loggedIn = 0: " + rowsUpdated);

                // Proceed with the login logic
                String sqlSelect = "SELECT * FROM logs WHERE name = ?";
                statementSelect = connection.prepareStatement(sqlSelect);
                statementSelect.setString(1, name);

                resultSet = statementSelect.executeQuery();

                if (resultSet.next()) {
                	
                    String storedHash = resultSet.getString("password");

                    BCrypt.Result result = BCrypt.verifyer().verify(passwordValue.toCharArray(), storedHash);
                    if (result.verified) {
                        int userId = resultSet.getInt("id");
                        String sqlUpdate = "UPDATE logs SET loggedIn = 1 WHERE id = ?";
                        statementUpdate = connection.prepareStatement(sqlUpdate);
                        statementUpdate.setInt(1, userId);
                        statementUpdate.executeUpdate();

                        Homepage.loggedIn = true;
                        System.out.println("Login successful!");

                        try {
                            window.close();
                            Stage catStage = new Stage();
                            new Categories().start(catStage);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        // Handle incorrect password
                        passBox.setStyle("-fx-border-color: red;");
                        passLabel.setStyle("-fx-text-fill: red;");
                        passLabel.setAlignment(Pos.CENTER_LEFT);
                        GridPane.setHalignment(passLabel, HPos.CENTER);
                        passLabel.setStyle("-fx-max-width: 500");
                        enterPassword.setImage(new Image("./Images/Incorrect_password.png"));
                        shake(passInput);
                    }
                } else {
                    // Handle username not found
                    nameInput.setStyle("-fx-border-color: red;");
                    nameLabel.setStyle("-fx-text-fill: red;");
                    nameLabel.setAlignment(Pos.CENTER_LEFT);
                    GridPane.setHalignment(nameLabel, HPos.CENTER);
                    nameLabel.setStyle("-fx-max-width: 500");
                    enterUsername.setImage(new Image("./Images/Username_not_found.png"));
                    shake(nameInput);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                    if (statementSelect != null) statementSelect.close();
                    if (statementUpdate != null) statementUpdate.close();
                    if (statementReset != null) statementReset.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
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


    private void shake(TextField textField) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), textField);
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }
}

//hello