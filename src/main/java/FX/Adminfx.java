package FX;

import Livechat.LiveChatA;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Adminfx extends Application {

    private TableView<Kaparisanje> kaparisanjaTable;
    private ObservableList<Kaparisanje> kaparisanjaData;

    private static final String URL = "jdbc:mysql://localhost:3306/cs202-db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();

        kaparisanjaTable = new TableView<>();
        kaparisanjaData = FXCollections.observableArrayList();
        kaparisanjaTable.setItems(kaparisanjaData);

        TableColumn<Kaparisanje, String> voziloColumn = new TableColumn<>("Vozilo");
        voziloColumn.setCellValueFactory(new PropertyValueFactory<>("naslov"));

        TableColumn<Kaparisanje, String> korisnikColumn = new TableColumn<>("Korisnik");
        korisnikColumn.setCellValueFactory(new PropertyValueFactory<>("korisnik"));

        TableColumn<Kaparisanje, String> bojaColumn = new TableColumn<>("Boja");
        bojaColumn.setCellValueFactory(new PropertyValueFactory<>("boja"));

        kaparisanjaTable.getColumns().addAll(voziloColumn, korisnikColumn, bojaColumn);

        Button liveChatButton = new Button("Live Chat Support");
        liveChatButton.setOnAction(e -> {
            try {
                new LiveChatA();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        VBox liveChatVBox = new VBox(10, liveChatButton);
        liveChatVBox.setPadding(new Insets(10));
        liveChatVBox.setStyle("-fx-border-color: #BDBDBD; -fx-border-width: 1; -fx-border-radius: 10; -fx-padding: 10;");
        mainPane.setCenter(kaparisanjaTable);
        mainPane.setRight(liveChatVBox);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT automobil.model, osoba.ime, osoba.prezime, automobil.boja " +
                    "FROM KaparisaniOglas " +
                    "JOIN Osoba ON KaparisaniOglas.osoba_id = Osoba.id " +
                    "JOIN Oglas ON KaparisaniOglas.oglas_id = Oglas.id " +
                    "JOIN Automobil ON Oglas.automobil_id = Automobil.auto_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String naslov = resultSet.getString("model");
                    String korisnik = resultSet.getString("ime") + " " + resultSet.getString("prezime");
                    String boja = resultSet.getString("boja");

                    kaparisanjaData.add(new Kaparisanje(naslov, korisnik, boja));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(mainPane, 800, 600);
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static class Kaparisanje {
        public String naslov;
        public String korisnik;
        public String boja;

        public Kaparisanje(String naslov, String korisnik, String boja) {
            this.naslov = naslov;
            this.korisnik = korisnik;
            this.boja = boja;
        }

        public String getNaslov() {
            return naslov;
        }

        public void setNaslov(String naslov) {
            this.naslov = naslov;
        }

        public String getKorisnik() {
            return korisnik;
        }

        public void setKorisnik(String korisnik) {
            this.korisnik = korisnik;
        }

        public String getBoja() {
            return boja;
        }

        public void setBoja(String boja) {
            this.boja = boja;
        }
    }
}