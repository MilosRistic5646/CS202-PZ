module com.example.cs202pz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.example.cs202pz to javafx.fxml;
    exports com.example.cs202pz;
    exports Livechat;
    exports com.example.cs202pz.Class;
    opens Livechat to javafx.fxml;
    exports FX;
    opens FX to javafx.fxml;
    exports LoginRegister;
    opens LoginRegister to javafx.fxml;

}