package LoginRegister;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import FX.Adminfx;
import FX.AutoOglasnaTabla;
import FX.Klientfx;
import FX.Vlasnikfx;

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
                if (LoginService.authenticateUser(ime, prezime, password)) {
                    String uloga = LoginService.getUloga(ime, prezime);
                    redirectToDashboard(uloga, primaryStage);
                } else {
                    showAlert("Login Error", "Invalid username or password. Please try again.");
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
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void redirectToDashboard(String uloga, Stage primaryStage) {
        if (uloga != null) {
            switch (uloga.toUpperCase()) {
                case "KORISNIK":
                    new Klientfx().start(primaryStage);
                    break;
                case "ADMIN":
                    new Adminfx().start(primaryStage);
                    break;
                case "VLASNIK":
                    new Vlasnikfx().start(primaryStage);
                    break;
                default:
                    showAlert("Login Error", "Invalid user role.");
            }
        } else {
            showAlert("Login Error", "User role not found.");
        }
    }
}