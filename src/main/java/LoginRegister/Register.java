package LoginRegister;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import FX.AutoOglasnaTabla;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Register extends GridPane {

    public Register(Stage primaryStage) {
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        TextField imeField = new TextField();
        TextField prezimeField = new TextField();
        TextField jmbgField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        Button registerButton = new Button("Register");
        Button cancleButton = new Button("Cancel");
        cancleButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        cancleButton.setOnAction(e -> {
            primaryStage.close();
            new AutoOglasnaTabla().start(primaryStage);
        });

        registerButton.setOnAction(event -> {
            String ime = imeField.getText();
            String prezime = prezimeField.getText();
            String jmbg = jmbgField.getText();
            String password = passwordField.getText();

            if (validateInput(ime, prezime, jmbg, password)) {
                if (registerUser(ime, prezime, jmbg, password)) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Registration Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("You have successfully registered!");
                    alert.showAndWait();

                    primaryStage.close();
                    new AutoOglasnaTabla().start(primaryStage);
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Registration Error");
                    alert.setHeaderText(null);
                    alert.setContentText("An error occurred during registration. Please try again.");
                    alert.showAndWait();
                }
            }
        });

        add(new Label("Ime:"), 0, 0);
        add(imeField, 1, 0);
        add(new Label("Prezime:"), 0, 1);
        add(prezimeField, 1, 1);
        add(new Label("JMBG:"), 0, 2);
        add(jmbgField, 1, 2);
        add(new Label("Password:"), 0, 3);
        add(passwordField, 1, 3);
        add(new Label("Confirm Password:"), 0, 4);
        add(confirmPasswordField, 1, 4);
        add(registerButton, 0, 5);
        add(cancleButton, 1, 5);
    }

    private boolean validateInput(String ime, String prezime, String jmbg, String password) {
        if (ime.isEmpty() || ime.length() > 255) {
            showAlert("Invalid Input", "Please enter a valid name (1-255 characters).");
            return false;
        }

        if (prezime.isEmpty() || prezime.length() > 255) {
            showAlert("Invalid Input", "Please enter a valid surname (1-255 characters).");
            return false;
        }

        if (jmbg.isEmpty() || jmbg.length() != 13) {
            showAlert("Invalid Input", "Please enter a valid JMBG (13 characters).");
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            showAlert("Invalid Input", "Password must be at least 6 characters long.");
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

    private boolean registerUser(String ime, String prezime, String jmbg, String password) {
        String url = "jdbc:mysql://localhost:3306/cs202-db";
        String username = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword)) {
            String sql = "INSERT INTO osoba (ime, prezime, jmbg, password, rola) VALUES (?, ?, ?, ?, 'korisnik')";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, ime);
                statement.setString(2, prezime);
                statement.setString(3, jmbg);
                statement.setString(4, password);

                int rowsAffected = statement.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}