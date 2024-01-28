package Class;

import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Oglas {
    private int oglasId;
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
        return oglasNode;
    }
}