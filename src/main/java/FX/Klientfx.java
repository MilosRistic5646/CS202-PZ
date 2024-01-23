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
import java.io.IOException;
import java.util.Optional;


public class Klientfx extends Application {

    private VBox oglasiVBox;
    private Button liveChatButton;
    private String korisnikIme;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        VBox mainVBox = new VBox();

        Oglas oglas1 = new Oglas("Renault Clio 2022", "Renault Clio pogodan gradski automobil", 10000, "file:src/clio.jpg");
        Oglas oglas2 = new Oglas("Renault Megane 2022", "Opis oglasa 2", 15000, "file:src/megan.jpg");
        Oglas oglas3 = new Oglas("Renault Kwid 2022", "Opis oglasa 3", 20000, "file:src/kwid.jpg");
        Oglas oglas4 = new Oglas("Renault Captur 2022", "Opis oglasa 4", 25000, "file:src/capture.jpg");

        oglasiVBox = new VBox(20);
        oglasiVBox.getChildren().addAll(oglas1.getOglasNode(), oglas2.getOglasNode(), oglas3.getOglasNode(), oglas4.getOglasNode());
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
            } catch (IOException ex) {
                throw new RuntimeException(ex);
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

    private String promptForUsername() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Unesite korisničko ime");
        dialog.setHeaderText(null);
        dialog.setContentText("Unesite korisničko ime:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse("Klijent");
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
        private String naslov;
        private String opis;
        private double cena;
        private String putanjaDoSlike;

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
            });

            oglasNode.getChildren().addAll(imageView, labelNaslov, labelOpis, labelCena, bojaChoiceBox, dugmeIzracunajPopust, labelPopust);
            oglasNode.setStyle("-fx-border-color: #BDBDBD; -fx-border-width: 1; -fx-border-radius: 10; -fx-padding: 10; -fx-background-color: #D3D3D3;");

            return oglasNode;
        }
    }
}