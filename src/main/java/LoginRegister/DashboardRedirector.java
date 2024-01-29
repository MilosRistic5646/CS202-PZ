package LoginRegister;

import FX.Adminfx;
import FX.Klientfx;
import FX.Vlasnikfx;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class DashboardRedirector {

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


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}