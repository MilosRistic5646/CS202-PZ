package DBUtil;

import java.sql.Connection;
import Class.Oglas;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UcitajOglase {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/cs202-db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static List<Oglas> ucitajOglaseIzBaze() {
        List<Oglas> oglasi = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT o.id, o.slika, o.cena, a.model, a.opis, a.boja " +
                    "FROM Oglas o " +
                    "JOIN Automobil a ON o.automobil_id = a.auto_id";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int oglasId = resultSet.getInt("id");
                        String naslov = resultSet.getString("model");
                        String opis = resultSet.getString("opis");
                        double cena = resultSet.getDouble("cena");
                        String putanjaDoSlike = resultSet.getString("slika");

                        Oglas oglas = new Oglas(naslov, opis, cena, putanjaDoSlike);
                        oglasi.add(oglas);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return oglasi;
    }
}
