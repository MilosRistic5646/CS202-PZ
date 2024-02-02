package FX;

import LoginRegister.Login;
import Class.Oglas;
import LoginRegister.Register;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

import static DBUtil.UcitajOglase.ucitajOglaseIzBaze;

public class AutoOglasnaTabla extends Application {

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


}
