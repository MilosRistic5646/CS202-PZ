package FX;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;

import static DBUtil.updateOglasInDatabase.updateOglasInDatabase;

public class Vlasnikfx extends Application {

    private VBox oglasiVBox;
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

        Button dodajOglasButton = new Button("Dodaj Oglas");
        dodajOglasButton.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white;");
        dodajOglasButton.setOnAction(e -> dodajNoviOglas());

        HBox controlsHBox = new HBox(20, dodajOglasButton);
        controlsHBox.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(oglasiVBox);
        scrollPane.setFitToWidth(true);
        mainVBox.getChildren().addAll(controlsHBox, scrollPane);

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
                String boja = resultSet.getString("boja");

                Oglas oglas = new Oglas(oglasId, naslov, opis, cena, putanjaDoSlike, boja);
                oglasiVBox.getChildren().add(oglas.getOglasNode());
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void dodajNoviOglas() {
        // Prikaz dijaloga za unos novog oglasa
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Dodaj novi oglas");
        dialog.setHeaderText(null);

        // Dodajte polja za unos informacija
        TextField putanjaDoSlikeField = new TextField();
        TextField cenaField = new TextField();
        TextField modelField = new TextField();
        TextField opisField = new TextField();
        TextField bojaField = new TextField();

        putanjaDoSlikeField.setPromptText("Putanja do slike");
        cenaField.setPromptText("Cena");
        modelField.setPromptText("Model");
        opisField.setPromptText("Opis");
        bojaField.setPromptText("Boja");

        GridPane grid = new GridPane();
        grid.add(new Label("Putanja do slike:"), 0, 0);
        grid.add(putanjaDoSlikeField, 1, 0);
        grid.add(new Label("Cena:"), 0, 1);
        grid.add(cenaField, 1, 1);
        grid.add(new Label("Model:"), 0, 2);
        grid.add(modelField, 1, 2);
        grid.add(new Label("Opis:"), 0, 3);
        grid.add(opisField, 1, 3);
        grid.add(new Label("Boja:"), 0, 4);
        grid.add(bojaField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        ButtonType potvrdiButton = new ButtonType("Potvrdi", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(potvrdiButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == potvrdiButton) {
                return putanjaDoSlikeField.getText() + ";" + cenaField.getText() + ";" +
                        modelField.getText() + ";" + opisField.getText() + ";" + bojaField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {

            String[] informacije = result.split(";");
            if (informacije.length == 5) {
                dodajOglasUBazu(informacije[0], Double.parseDouble(informacije[1]), informacije[2], informacije[3], informacije[4]);
                oglasiVBox.getChildren().clear();
                ucitajOglaseIzBaze();
            } else {
                // Prikazati poruku o neispravnim podacima
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText(null);
                alert.setContentText("Unesite sve informacije ispravno.");
                alert.showAndWait();
            }
        });
    }

    private class Oglas {
        private int oglasId;
        private String naslov;
        private String opis;
        private double cena;
        private String putanjaDoSlike;
        private String boja;

        public Oglas(int oglasId, String naslov, String opis, double cena, String putanjaDoSlike, String boja) {
            this.oglasId = oglasId;
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

            Button izmeniButton = new Button("Izmeni");
            izmeniButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white;");
            izmeniButton.setOnAction(e -> izmeniOglas(oglasId));

            Button obrisiButton = new Button("Obrisi");
            obrisiButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
            obrisiButton.setOnAction(e -> obrisiOglas(oglasId));

            oglasVBox.getChildren().addAll(slikaView, labelNaslov, labelOpis, labelCena, labelBoja, izmeniButton, obrisiButton);
            oglasVBox.setStyle("-fx-border-color: #BDBDBD; -fx-border-width: 1; -fx-border-radius: 10; -fx-padding: 10; -fx-background-color: #D3D3D3;");
            return oglasVBox;
        }

        private void izmeniOglas(int oglasId) {
            // Prikaz dijaloga za unos novih informacija
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Izmeni oglas");
            dialog.setHeaderText(null);

            // Dodajte polja za unos novih informacija
            TextField noviNaslovField = new TextField(naslov);
            TextField noviOpisField = new TextField(opis);
            TextField novaCenaField = new TextField(String.valueOf(cena));
            TextField novaBojaField = new TextField(boja);

            noviNaslovField.setPromptText("Novi naslov");
            noviOpisField.setPromptText("Novi opis");
            novaCenaField.setPromptText("Nova cena");
            novaBojaField.setPromptText("Nova boja");

            GridPane grid = new GridPane();
            grid.add(new Label("Novi naslov:"), 0, 0);
            grid.add(noviNaslovField, 1, 0);
            grid.add(new Label("Novi opis:"), 0, 1);
            grid.add(noviOpisField, 1, 1);
            grid.add(new Label("Nova cena:"), 0, 2);
            grid.add(novaCenaField, 1, 2);
            grid.add(new Label("Nova boja:"), 0, 3);
            grid.add(novaBojaField, 1, 3);

            dialog.getDialogPane().setContent(grid);

            ButtonType potvrdiButton = new ButtonType("Potvrdi", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(potvrdiButton, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == potvrdiButton) {
                    return noviNaslovField.getText() + ";" + noviOpisField.getText() + ";" +
                            novaCenaField.getText() + ";" + novaBojaField.getText();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(result -> {
                // Ažuriranje baze sa novim informacijama
                String[] noveInformacije = result.split(";");
                if (noveInformacije.length == 4) {
                    updateOglasInDatabase(oglasId, noveInformacije[0], noveInformacije[1], Double.parseDouble(noveInformacije[2]), noveInformacije[3]);
                    // Osvežavanje prikaza
                    oglasiVBox.getChildren().clear();
                    ucitajOglaseIzBaze();
                } else {
                    // Prikazati poruku o neispravnim podacima
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greška");
                    alert.setHeaderText(null);
                    alert.setContentText("Unesite sve informacije ispravno.");
                    alert.showAndWait();
                }
            });
        }


        private void obrisiOglas(int oglasId) {
            // Prikaz dijaloga za potvrdu brisanja
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda brisanja");
            alert.setHeaderText(null);
            alert.setContentText("Da li ste sigurni da želite obrisati ovaj oglas?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Brisanje oglasa iz baze
                    deleteOglasFromDatabase(oglasId);
                    // Uklonite VBox sa oglasa iz prikaza
                    Node obrisiOglas = null;
                    oglasiVBox.getChildren().removeIf(node -> node instanceof VBox && ((VBox) node).getChildren().contains(obrisiOglas));
                }
            });
        }

        private void deleteOglasFromDatabase(int oglasId) {
            // Implementirajte brisanje oglasa iz baze podataka
            try {
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/cs202-db",
                        "root",
                        "");

                String query = "DELETE FROM Oglas WHERE id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, oglasId);

                preparedStatement.executeUpdate();

                preparedStatement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void dodajOglasUBazu(String putanjaDoSlike, double novaCena, String model, String opis, String boja) {
        // Implementacija dodavanja oglasa u bazu podataka
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cs202-db",
                    "root",
                    "");

            // Dodajte novi automobil u tabelu Automobil
            String insertAutomobilQuery = "INSERT INTO Automobil (model, opis, boja) VALUES (?, ?, ?)";
            PreparedStatement automobilStatement = connection.prepareStatement(insertAutomobilQuery, Statement.RETURN_GENERATED_KEYS);
            automobilStatement.setString(1, model);
            automobilStatement.setString(2, opis);
            automobilStatement.setString(3, boja);
            automobilStatement.executeUpdate();

            ResultSet generatedKeys = automobilStatement.getGeneratedKeys();
            int automobilId = 0;
            if (generatedKeys.next()) {
                automobilId = generatedKeys.getInt(1);
            }

            // Dodajte novi oglas u tabelu Oglas
            String insertOglasQuery = "INSERT INTO Oglas (slika, cena, automobil_id) VALUES (?, ?, ?)";
            PreparedStatement oglasStatement = connection.prepareStatement(insertOglasQuery);
            oglasStatement.setString(1, putanjaDoSlike);
            oglasStatement.setDouble(2, novaCena);
            oglasStatement.setInt(3, automobilId);
            oglasStatement.executeUpdate();

            automobilStatement.close();
            oglasStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
