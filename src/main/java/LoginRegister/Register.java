package LoginRegister;

import javafx.geometry.Insets;
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
        Button cancleButton = new Button("Cancle");
        cancleButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        cancleButton.setOnAction(e -> {
            primaryStage.close();
            new com.example.cs202pz.AutoOglasnaTabla().start(primaryStage);
        });


        registerButton.setOnAction(event -> {
            System.out.println("Implementiraj logiku za registraciju");
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
        add(cancleButton, 1,5 );
    }
}