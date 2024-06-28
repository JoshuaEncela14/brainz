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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Signup extends Application {

    Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        ImageView eyeImageView = new ImageView(new Image("Blind.png"));
        ImageView eyeImageView2 = new ImageView(new Image("Blind.png"));

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

        // Name Input
        Label nameLabel = new Label("Enter Username:");
        GridPane.setConstraints(nameLabel, 0, 4);

        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter Username");
        nameInput.setPrefHeight(35);
        nameInput.setPrefWidth(250);
        nameInput.getStyleClass().add("name-field-container");
        GridPane.setConstraints(nameInput, 0, 5);

        // Password Input
        Label passLabel = new Label("Enter Password:");
        GridPane.setConstraints(passLabel, 0, 7);

        PasswordField passInput = new PasswordField();
        passInput.setPromptText("Enter Password");
        passInput.setPrefHeight(35);
        passInput.setPrefWidth(500);
        passInput.getStyleClass().add("password-field");
        HBox passBox = new HBox(passInput, eyeImageView);
        passBox.setAlignment(Pos.CENTER_LEFT);
        passBox.getStyleClass().add("password-field-container");
        GridPane.setConstraints(passBox, 0, 8);

        // Confirm Password Input
        Label confirmLabel = new Label("Confirm Password:");
        GridPane.setConstraints(confirmLabel, 0, 10);

        PasswordField confirmInput = new PasswordField();
        confirmInput.setPromptText("Confirm Password");
        confirmInput.setPrefHeight(35);
        confirmInput.setPrefWidth(500);
        confirmInput.getStyleClass().add("password-field");
        HBox confirmPassBox = new HBox(confirmInput, eyeImageView2);
        confirmPassBox.setAlignment(Pos.CENTER_LEFT);
        confirmPassBox.getStyleClass().add("password-field-container");
        GridPane.setConstraints(confirmPassBox, 0, 11);

        // Sign Up Button
        Button signUpButton = new Button("SIGN-UP");
        signUpButton.setPrefWidth(250);
        HBox hboxsignup = new HBox(signUpButton);
        hboxsignup.setAlignment(Pos.CENTER);
        GridPane.setConstraints(hboxsignup, 0, 17);

        // Sign Up Action
        signUpButton.setOnAction(e -> {
            signUp(nameInput, passInput, confirmInput, nameLabel, passLabel, confirmLabel, passBox, confirmPassBox, url, username, password);
        });

        // Enter key triggers sign up
        nameInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUp(nameInput, passInput, confirmInput, nameLabel, passLabel, confirmLabel, passBox, confirmPassBox, url, username, password);
            }
        });
        
        nameInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUp(nameInput, passInput, confirmInput, nameLabel, passLabel, confirmLabel, passBox, confirmPassBox, url, username, password);
            }
        });

        passInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUp(nameInput, passInput, confirmInput, nameLabel, passLabel, confirmLabel, passBox, confirmPassBox, url, username, password);
            }
        });

        confirmInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUp(nameInput, passInput, confirmInput, nameLabel, passLabel, confirmLabel, passBox, confirmPassBox, url, username, password);
            }
        });

        // Login Link
        Label logLabel = new Label("Already Have an Account? ");
        Hyperlink logInLink = new Hyperlink("Log-in");
        logInLink.getStyleClass().add("hyperlink-sign-up");
        logLabel.getStyleClass().add("label-sign-up");

        HBox hboxlogin = new HBox(logLabel, logInLink);
        hboxlogin.setAlignment(Pos.CENTER);
        GridPane.setConstraints(hboxlogin, 0, 18);

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
                nameLabel, nameInput,
                passLabel, passBox,
                confirmLabel, confirmPassBox,
                hboxsignup,
                hboxlogin
        );

        // Scene and CSS
        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("loginStyle.css");
        window.setScene(scene);
        window.show();
    }

    private void signUp(TextField nameInput, PasswordField passInput, PasswordField confirmInput,
                        Label nameLabel, Label passLabel, Label confirmLabel,
                        HBox passBox, HBox confirmPassBox, String url, String username, String password) {
        String name = nameInput.getText();
        String passwordValue = passInput.getText();
        String confirmValue = confirmInput.getText();

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
        } else if (confirmValue.isEmpty()) {
            confirmPassBox.setStyle("-fx-border-color: red;");
            confirmLabel.setStyle("-fx-text-fill: red;");
            shake(confirmInput);
            valid = false;
        } else if (!passwordValue.equals(confirmValue)) {
            passBox.setStyle("-fx-border-color: red;");
            passLabel.setStyle("-fx-text-fill: red;");
            shake(passInput);
            confirmPassBox.setStyle("-fx-border-color: red;");
            confirmLabel.setStyle("-fx-text-fill: red;");
            confirmLabel.setText("Password do not match");
            shake(confirmInput);
            valid = false;
        }

        if (valid) {
            try {
                String hashedPassword = BCrypt.withDefaults().hashToString(12, passwordValue.toCharArray());
                Connection connection = DriverManager.getConnection(url, username, password);
                String checkSql = "SELECT * FROM `create` WHERE name = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setString(1, name);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    nameInput.setStyle("-fx-border-color: red;");
                    nameLabel.setStyle("-fx-text-fill: red;");
                    nameLabel.setText("Username taken");
                    shake(nameInput);
                } else {
                    String insertSql = "INSERT INTO `create` (name, password) VALUES (?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                    insertStatement.setString(1, name);
                    insertStatement.setString(2, hashedPassword);
                        int rowsInserted = insertStatement.executeUpdate();

                        if (rowsInserted > 0) {
                            System.out.println("Account Created");
                            try {
                                window.close();
                                Stage loginStage = new Stage();
                                new Login().start(loginStage);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        insertStatement.close();
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
        
}