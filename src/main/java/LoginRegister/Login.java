package LoginRegister;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import FX.AutoOglasnaTabla;
import FX.Klientfx;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Login extends GridPane {

    public Login(Stage primaryStage) {
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        TextField imeField = new TextField();
        TextField prezimeField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> {
            primaryStage.close();
            new AutoOglasnaTabla().start(primaryStage);
        });

        loginButton.setOnAction(event -> {
            String ime = imeField.getText();
            String prezime = prezimeField.getText();
            String password = passwordField.getText();

            if (validateInputForLogin(ime, prezime, password)) {
                if (authenticateUser(ime, prezime, password)) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Login Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("You have successfully logged in!");
                    alert.showAndWait();

                    primaryStage.close();
                    new Klientfx().start(primaryStage);
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Login Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid username or password. Please try again.");
                    alert.showAndWait();
                }
            }
        });

        add(new Label("Ime:"), 0, 0);
        add(imeField, 1, 0);
        add(new Label("Prezime:"), 0, 1);
        add(prezimeField, 1, 1);
        add(new Label("Password:"), 0, 2);
        add(passwordField, 1, 2);
        add(loginButton, 0, 3);
        add(cancelButton, 1, 3);
    }

    private boolean validateInputForLogin(String ime, String prezime, String password) {
        if (ime.isEmpty() || prezime.isEmpty() || password.isEmpty()) {
            showAlert("Invalid Input", "Please enter all required fields.");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean authenticateUser(String ime, String prezime, String password) {
        String url = "jdbc:mysql://localhost:3306/cs202-db";
        String username = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword)) {
            String sql = "SELECT * FROM osoba WHERE ime = ? AND prezime = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, ime);
                statement.setString(2, prezime);
                statement.setString(3, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
