package FX;

import LoginRegister.Login;
import LoginRegister.Register;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AutoOglasnaTabla extends Application {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/cs202-db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();

        List<Oglas> oglasi = ucitajOglaseIzBaze();

        VBox oglasiVBox = new VBox(20);
        oglasiVBox.getChildren().addAll(oglasi.stream().map(Oglas::getOglasNode).collect(Collectors.toList()));
        oglasiVBox.setAlignment(Pos.CENTER);
        oglasiVBox.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(oglasiVBox);
        scrollPane.setFitToWidth(true);

        mainPane.setCenter(scrollPane);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        loginButton.setOnAction(e -> prikaziLoginPane(primaryStage));

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
        registerButton.setOnAction(e -> prikaziRegisterPane(primaryStage));

        HBox topHBox = new HBox(20, loginButton, registerButton);
        topHBox.setAlignment(Pos.CENTER_RIGHT);
        topHBox.setPadding(new Insets(10));

        mainPane.setTop(topHBox);

        Scene scene = new Scene(mainPane, 800, 600);

        primaryStage.setTitle("Auto Oglasna Tabla");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void prikaziLoginPane(Stage primaryStage) {
        Login loginPane = new Login(primaryStage);
        Scene loginScene = new Scene(loginPane, 400, 200);
        primaryStage.setScene(loginScene);
    }

    private void prikaziRegisterPane(Stage primaryStage) {
        Register registerPane = new Register(primaryStage);
        Scene registerScene = new Scene(registerPane, 400, 250);
        primaryStage.setScene(registerScene);
    }

    private List<Oglas> ucitajOglaseIzBaze() {
        List<Oglas> oglasi = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT o.id, o.slika, o.cena, a.model, a.opis, a.boja " +
                    "FROM Oglas o " +
                    "JOIN Automobil a ON o.automobil_id = a.auto_id";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int oglasId = resultSet.getInt("id");
                        String naslov = resultSet.getString("model");
                        String opis = resultSet.getString("opis");
                        double cena = resultSet.getDouble("cena");
                        String putanjaDoSlike = resultSet.getString("slika");

                        Oglas oglas = new Oglas(naslov, opis, cena, putanjaDoSlike);
                        oglasi.add(oglas);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return oglasi;
    }

    private static class Oglas {
        private final String naslov;
        private final String opis;
        private final double cena;
        private final String putanjaDoSlike;

        public Oglas(String naslov, String opis, double cena, String putanjaDoSlike) {
            this.naslov = naslov;
            this.opis = opis;
            this.cena = cena;
            this.putanjaDoSlike = putanjaDoSlike;
        }

        public VBox getOglasNode() {
            VBox oglasNode = new VBox(10);
            oglasNode.setAlignment(Pos.CENTER);

            ImageView imageView = new ImageView(new Image(putanjaDoSlike));
            imageView.setFitWidth(300);
            imageView.setFitHeight(180);

            Label labelNaslov = new Label(naslov);
            labelNaslov.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

            Label labelOpis = new Label(opis);
            labelOpis.setStyle("-fx-font-size: 14;");

            Label labelCena = new Label("Cena: $" + cena);
            labelCena.setStyle("-fx-font-size: 14;");

            ChoiceBox<String> bojaChoiceBox = new ChoiceBox<>();
            bojaChoiceBox.getItems().addAll("Boja", "Crvena", "Plava", "Bela", "Siva");
            bojaChoiceBox.getSelectionModel().selectFirst();
            bojaChoiceBox.setStyle("-fx-font-size: 14;");
            bojaChoiceBox.setOnAction(event -> {
                String izabranaBoja = bojaChoiceBox.getValue();
                if (!izabranaBoja.equals("Boja")) {
                    labelNaslov.setText(naslov + " - " + izabranaBoja);
                    labelNaslov.setTextFill(Color.web(izabranaBoja.toLowerCase()));
                } else {
                    labelNaslov.setText(naslov);
                    labelNaslov.setTextFill(Color.BLACK);
                }
            });

            Button dugmeIzracunajPopust = new Button("Izračunaj kaparu");
            dugmeIzracunajPopust.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
            Label labelPopust = new Label();
            labelPopust.setStyle("-fx-font-size: 14;");

            dugmeIzracunajPopust.setOnAction(event -> {
                double popust = cena * 0.1;
                labelPopust.setText("Kapara: $" + popust);
            });

            oglasNode.getChildren().addAll(imageView, labelNaslov, labelOpis, labelCena, bojaChoiceBox, dugmeIzracunajPopust, labelPopust);
            oglasNode.setStyle("-fx-border-color: #BDBDBD; -fx-border-width: 1; -fx-border-radius: 10; -fx-padding: 10; -fx-background-color: #D3D3D3;");

            return oglasNode;
        }
    }
}

