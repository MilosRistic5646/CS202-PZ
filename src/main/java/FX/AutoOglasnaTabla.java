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

public class AutoOglasnaTabla extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();

        Oglas oglas1 = new Oglas("Renault Clio 2022", "Opis oglasa 1", 10000, "file:src/clio.jpg");
        Oglas oglas2 = new Oglas("Renault Megane 2022", "Opis oglasa 2", 15000, "file:src/megan.jpg");
        Oglas oglas3 = new Oglas("Renault Kwid 2022", "Opis oglasa 3", 20000, "file:src/kwid.jpg");
        Oglas oglas4 = new Oglas("Renault Captur 2022", "Opis oglasa 4", 25000, "file:src/capture.jpg");

        VBox oglasiVBox = new VBox(20);
        oglasiVBox.getChildren().addAll(oglas1.getOglasNode(), oglas2.getOglasNode(), oglas3.getOglasNode(), oglas4.getOglasNode());
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

