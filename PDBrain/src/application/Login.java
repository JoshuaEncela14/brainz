package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import at.favre.lib.crypto.bcrypt.BCrypt;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Login extends Application {

    Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ImageView eyeImageView = new ImageView(new Image("Blind.png"));

        String url = "jdbc:mysql://localhost:3306/login";
        String username = "root";
        String password = "";

        window = primaryStage;
        window.setTitle("BRAINZZZ");

        // GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 10, 10, 20));
        grid.setVgap(5);
        grid.setHgap(10);

        ImageView logo = new ImageView(new Image("logooo.png"));
        grid.addRow(1, logo);

        // Name Label
        Label nameLabel = new Label("Enter Name:");
        GridPane.setConstraints(nameLabel, 0, 4);

        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter Name");
        nameInput.setPrefHeight(35);
        nameInput.setPrefWidth(250);
        nameInput.getStyleClass().add("name-field-container");
        GridPane.setConstraints(nameInput, 0, 5);

        // Password Label
        Label passLabel = new Label("Enter Password:");
        GridPane.setConstraints(passLabel, 0, 7);

        PasswordField passInput = new PasswordField();
        passInput.setPromptText("Enter Password");
        passInput.setPrefHeight(35);
        passInput.setPrefWidth(500);
        passInput.getStyleClass().add("password-field");

        // ImageView for toggling password visibility
        ImageView eyeImageView2 = new ImageView(new Image("Eye.png"));
        eyeImageView2.setFitHeight(24);
        eyeImageView2.setFitWidth(24);
        eyeImageView2.setPreserveRatio(true);

        Text passText = new Text();
        passText.setVisible(false); // Initially invisible
        passText.getStyleClass().add("toggle-password"); // Add CSS styling if needed
        
        StackPane passPane = new StackPane(passInput, passText, eyeImageView);
        passPane.setAlignment(Pos.CENTER_LEFT);
        passPane.getStyleClass().add("password-field");
        GridPane.setConstraints(passPane, 0, 8);
        
        // HBox to contain PasswordField and ImageView
        HBox passBox = new HBox(passPane, eyeImageView); // Include passText in the HBox
        passBox.setAlignment(Pos.CENTER_LEFT);
        passBox.getStyleClass().add("password-field-container");
        GridPane.setConstraints(passBox, 0, 8);

 
        
        eyeImageView.setOnMousePressed(event -> {
            if (passInput.isVisible()) {
                // Password field is visible, switch to text display
                passText.setText(passInput.getText());
                passText.setVisible(true);
                passInput.setVisible(false);
                eyeImageView.setImage(new Image("Eye.png"));
            } else {
                // Text display is visible, switch to password field
                passInput.setText(passText.getText());
                passInput.setVisible(true);
                passText.setVisible(false);
                eyeImageView.setImage(new Image("Blind.png"));
            }
        });

        Button loginButton = new Button("LOG-IN");
        loginButton.setPrefWidth(250);

        HBox hboxlog = new HBox(loginButton);
        hboxlog.setAlignment(Pos.CENTER);
        GridPane.setConstraints(hboxlog, 0, 14);
        loginButton.getStyleClass().add("button-login");

        // Database connection and login logic
        loginButton.setOnAction(e -> {
            login(nameInput, passInput, nameLabel, passLabel, passBox, url, username, password);
        });

        // Enter key triggers login
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

        // Sign-up Link
        Label signUpLabel = new Label("Don't Have an Account?");
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

        // Adding components to grid
        grid.getChildren().addAll(
                nameLabel, nameInput,
                passLabel, passBox,
                hboxlog,
                signUpBox
        );

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

        // Scene and CSS
        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("loginStyle.css");
        window.setScene(scene);
        window.show();
    }

    private void login(TextField nameInput, PasswordField passInput, Label nameLabel, Label passLabel, HBox passBox, String url, String username, String password) {
        String name = nameInput.getText();
        String passwordValue = passInput.getText();

        boolean valid = true;

        if (name.isEmpty()) {
            nameInput.setStyle("-fx-border-color: red;");
            nameLabel.setStyle("-fx-text-fill: red;");
            shake(nameInput);
            valid = false;
        } else if (passwordValue.isEmpty()) {
            passBox.setStyle("-fx-border-color: red;");
            passLabel.setStyle("-fx-text-fill: red;");
            shake(passInput);
            valid = false;
        }

        if (valid) {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                String sql = "SELECT * FROM `create` WHERE name = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, name);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Username exists, now check password
                    String storedHash = resultSet.getString("password");

                    BCrypt.Result result = BCrypt.verifyer().verify(passwordValue.toCharArray(), storedHash);
                    if (result.verified) {
                        System.out.println("Login successful!");
                        try {
                            window.close();
                            Stage catStage = new Stage();
                            new Categories().start(catStage);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        passBox.setStyle("-fx-border-color: red;");
                        passLabel.setStyle("-fx-text-fill: red;");
                        passLabel.setText("Incorrect password"); // Set the label to indicate incorrect password
                        shake(passInput);
                        valid = false;
                    }
                } else {
                    nameInput.setStyle("-fx-border-color: red;");
                    nameLabel.setStyle("-fx-text-fill: red;");
                    nameLabel.setText("Username not found"); // Set the label to indicate username not found
                    shake(nameInput);
                    valid = false;
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
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
}
