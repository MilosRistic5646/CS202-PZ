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
import java.sql.ResultSet;
import java.sql.Statement;

public class Klientfx extends Application {

    private VBox oglasiVBox;
    private Button liveChatButton;
    private String korisnikIme;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox mainVBox = new VBox();

        oglasiVBox = new VBox(20);
        ucitajOglaseIzBaze(); // Učitavanje oglasa iz baze
        oglasiVBox.setAlignment(Pos.CENTER);
        oglasiVBox.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(oglasiVBox);
        scrollPane.setFitToWidth(true);
        mainVBox.getChildren().add(scrollPane);

        liveChatButton = new Button("Live Chat support");
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

            String query = "SELECT o.id, o.slika, o.cena, a.auto_id, a.model, a.opis, a.boja " +
                    "FROM Oglas o " +
                    "JOIN Automobil a ON o.automobil_id = a.auto_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int oglasId = resultSet.getInt("id");
                String naslov = resultSet.getString("model");
                String opis = resultSet.getString("opis");
                double cena = resultSet.getDouble("cena");
                String putanjaDoSlike = resultSet.getString("slika");

                Oglas oglas = new Oglas(oglasId, naslov, opis, cena, putanjaDoSlike);
                oglasiVBox.getChildren().add(oglas.getOglasNode());
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dodajUKaparisaneOglase(int oglasId, String korisnikIme) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cs202-db",
                    "root",
                    "");

            String query = "INSERT INTO kaparisanioglas (oglas_id, korisnik) VALUES (" + oglasId + ", '" + korisnikIme + "')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String promptForUsername() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Unesite korisničko ime");
        dialog.setHeaderText(null);
        dialog.setContentText("Unesite korisničko ime:");

        return dialog.showAndWait().orElse("Klijent");
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

    private class Oglas {
        private int oglasId;
        private String naslov;
        private String opis;
        private double cena;
        private String putanjaDoSlike;

        public Oglas(int oglasId, String naslov, String opis, double cena, String putanjaDoSlike) {
            this.oglasId = oglasId;
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
                } else {
                    labelNaslov.setText(naslov);
                    labelNaslov.setTextFill(Color.BLACK);
                }
            });

            Button dugmeIzracunajPopust = new Button("Kaparisi");
            dugmeIzracunajPopust.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
            Label labelPopust = new Label();
            labelPopust.setStyle("-fx-font-size: 14;");

            dugmeIzracunajPopust.setOnAction(event -> {
                double popust = cena * 0.1;
                labelPopust.setText("Kaparaisano: $" + popust);
                prikaziKaparisanjePopup(naslov, opis, cena, bojaChoiceBox.getValue(), putanjaDoSlike);
                dodajUKaparisaneOglase(oglasId, korisnikIme);
            });

            oglasNode.getChildren().addAll(imageView, labelNaslov, labelOpis, labelCena, bojaChoiceBox, dugmeIzracunajPopust, labelPopust);
            oglasNode.setStyle("-fx-border-color: #BDBDBD; -fx-border-width: 1; -fx-border-radius: 10; -fx-padding: 10; -fx-background-color: #D3D3D3;");

            return oglasNode;
        }
    }
}