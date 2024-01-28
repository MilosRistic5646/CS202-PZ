package FX;

import Livechat.LiveChatA;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;

public class Adminfx extends Application {

    private TableView<Kaparisanje> kaparisanjaTable;
    private ObservableList<Kaparisanje> kaparisanjaData;


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

        Scene scene = new Scene(mainPane, 800, 600);

        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private class Kaparisanje {
        private String naslov;
        private String korisnik;
        private String boja;

        public Kaparisanje(String naslov, String korisnik, String boja) {
            this.naslov = naslov;
            this.korisnik = korisnik;
            this.boja = boja;
        }
    }
}
