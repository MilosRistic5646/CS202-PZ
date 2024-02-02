package com.example.cs202pz.Class;

import FX.AutoOglasnaTabla;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main extends Application {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/cs202-db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ImageView imageView = new ImageView();
        Text titleText = new Text();
        Button button = new Button("Pogledaj Auto Oglasnu Tablu");

        // Dodajte stil za poboljšanje vidljivosti
        button.setStyle("-fx-text-fill: white; -fx-background-color: #4CAF50;");
        titleText.setStyle("-fx-fill: white;");

        titleText.setFont(new Font(24));

        VBox vbox = new VBox(imageView, titleText, button);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        // Dodajte stil za crnu boju pozadine VBox-a
        vbox.setStyle("-fx-background-color: black;");

        // Kreiranje scene
        Scene scene = new Scene(vbox, 800, 600, Color.BLACK);

        // Postavljanje akcije dugmeta
        button.setOnAction(event -> {
            // Otvori AutoOglasnaTabla.java
            AutoOglasnaTabla.main(new String[]{});
        });

        // Povezivanje s bazom podataka
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Izvršavanje SQL upita
            String sql = "SELECT naslovna, ime FROM autosalon WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, 1);  // Postavi odgovarajući ID oglasa
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Postavljanje slike iz baze
                        String imageUrl = resultSet.getString("naslovna");
                        Image image = new Image(imageUrl);
                        if (image.isError()) {
                            System.out.println("Greška prilikom učitavanja slike");
                        } else {
                            // Prilagodite širinu i visinu slike
                            image.getWidth();
                            image.getHeight();

                            imageView.setImage(image);

                            // Postavljanje naslova
                            String title = resultSet.getString("ime");
                            titleText.setText(title);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Postavljanje naslova prozora i prikazivanje scene
        primaryStage.setTitle("Auto Oglas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
