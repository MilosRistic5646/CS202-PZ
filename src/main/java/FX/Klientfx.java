package FX;

import Livechat.LiveChatC;
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
import java.sql.Statement;

public class Klientfx extends Application {

    private VBox oglasiVBox;
    private Button liveChatButton;
    private String korisnikJMBG;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox mainVBox = new VBox();

        mainVBox.setStyle("-fx-background-color: #F5F5F5;");

        oglasiVBox = new VBox(20);
        ucitajOglaseIzBaze();
        oglasiVBox.setAlignment(Pos.CENTER);
        oglasiVBox.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(oglasiVBox);
        scrollPane.setFitToWidth(true);
        mainVBox.getChildren().add(scrollPane);

        liveChatButton = new Button("Live Chat Support");
        liveChatButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        liveChatButton.setOnAction(e -> {
            try {
                new LiveChatC();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox liveChatHBox = new HBox(20, liveChatButton);
        liveChatHBox.setAlignment(Pos.CENTER_RIGHT);
        liveChatHBox.setPadding(new Insets(10));

        mainVBox.getChildren().add(liveChatHBox);

        Scene scene = new Scene(mainVBox, 800, 600);

        primaryStage.setTitle("Auto Oglasna Tabla");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void ucitajOglaseIzBaze() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cs202-db",
                    "root",
                    "");

            String query = "SELECT o.id, o.slika, o.cena, a.auto_id, a.model, a.opis, a.boja, ko.osoba_id " +
                    "FROM Oglas o " +
                    "JOIN Automobil a ON o.automobil_id = a.auto_id " +
                    "LEFT JOIN kaparisanioglas ko ON o.id = ko.oglas_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int oglasId = resultSet.getInt("id");
                int osobaId = resultSet.getInt("osoba_id");
                String naslov = resultSet.getString("model");
                String opis = resultSet.getString("opis");
                double cena = resultSet.getDouble("cena");
                String putanjaDoSlike = resultSet.getString("slika");
                String boja = resultSet.getString("boja");

                Oglas oglas = new Oglas(oglasId, osobaId, naslov, opis, cena, putanjaDoSlike, boja);
                oglasiVBox.getChildren().add(oglas.getOglasNode());
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String promptForJMBG() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Unesite JMBG");
        dialog.setHeaderText(null);
        dialog.setContentText("Unesite JMBG:");

        return dialog.showAndWait().orElse("");
    }

    private void prikaziKaparisanjePopup(String naslovOglasa, String opisOglasa, double cenaOglasa, String bojaOglasa, String putanjaDoSlike) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uspešno kaparisanje");
        alert.setHeaderText(null);

        VBox contentVBox = new VBox(10);
        contentVBox.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView(new Image(putanjaDoSlike));
        imageView.setFitWidth(200);
        imageView.setFitHeight(120);

        Label labelNaslov = new Label("Vozilo: " + naslovOglasa);
        Label labelOpis = new Label("Opis: " + opisOglasa);
        Label labelCena = new Label("Cena: $" + cenaOglasa);
        Label labelBoja = new Label("Boja: " + bojaOglasa);

        contentVBox.getChildren().addAll(imageView, labelNaslov, labelOpis, labelCena, labelBoja);

        alert.getDialogPane().setContent(contentVBox);
        alert.showAndWait();
    }

    private void dodajUKaparisaneOglase(int oglasId, String jmbg) {
        try {
            if (!proveriPostojanjeOsobe(jmbg)) {
                System.out.println("Osoba sa JMBG " + jmbg + " ne postoji u tabeli osoba.");
                return;
            }

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cs202-db",
                    "root",
                    "");

            String query = "INSERT INTO kaparisanioglas (oglas_id, osoba_id) " +
                    "VALUES (?, (SELECT id FROM osoba WHERE jmbg = ?))";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, oglasId);
            preparedStatement.setString(2, jmbg);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean proveriPostojanjeOsobe(String jmbg) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cs202-db",
                    "root",
                    "");

            String query = "SELECT id FROM osoba WHERE jmbg = ?";
            PreparedStatement preparedStatement = connection
                    .prepareStatement(query);
            preparedStatement.setString(1, jmbg);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean postojiOsoba = resultSet.next();

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return postojiOsoba;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private class Oglas {
        private int oglasId;
        private int osobaId;
        private String naslov;
        private String opis;
        private double cena;
        private String putanjaDoSlike;
        private String boja;

        public Oglas(int oglasId, int osobaId, String naslov, String opis, double cena, String putanjaDoSlike, String boja) {
            this.oglasId = oglasId;
            this.osobaId = osobaId;
            this.naslov = naslov;
            this.opis = opis;
            this.cena = cena;
            this.putanjaDoSlike = putanjaDoSlike;
            this.boja = boja;
        }

        public VBox getOglasNode() {
            VBox oglasVBox = new VBox(10);
            oglasVBox.setAlignment(Pos.CENTER);
            oglasVBox.setPadding(new Insets(10));
            oglasVBox.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            ImageView slikaView = new ImageView(new Image(putanjaDoSlike));
            slikaView.setFitWidth(200);
            slikaView.setFitHeight(120);

            Label labelNaslov = new Label(naslov);
            Label labelOpis = new Label(opis);
            Label labelCena = new Label("Cena: $" + cena);
            Label labelBoja = new Label("Boja: " + boja);

            Button kaparisiButton = new Button("Kaparisi");
            kaparisiButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
            kaparisiButton.setOnAction(e -> {
                korisnikJMBG = promptForJMBG();
                dodajUKaparisaneOglase(oglasId, korisnikJMBG);
                prikaziKaparisanjePopup(naslov, opis, cena, boja, putanjaDoSlike);
            });

            oglasVBox.getChildren().addAll(slikaView, labelNaslov, labelOpis, labelCena, labelBoja, kaparisiButton);
            oglasVBox.setStyle("-fx-border-color: #BDBDBD; -fx-border-width: 1; -fx-border-radius: 10; -fx-padding: 10; -fx-background-color: #D3D3D3;");
            return oglasVBox;
        }
    }
}