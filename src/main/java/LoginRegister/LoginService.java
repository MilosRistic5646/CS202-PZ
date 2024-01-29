package LoginRegister;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {

    public static boolean authenticateUser(String ime, String prezime, String password) {
        String url = "jdbc:mysql://localhost:3306/cs202-db";
        String username = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword)) {
            String sql = "SELECT * FROM osoba WHERE ime = ? AND prezime = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, ime);
                statement.setString(2, prezime);
                statement.setString(3, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getUloga(String ime, String prezime) {
        String url = "jdbc:mysql://localhost:3306/cs202-db";
        String username = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword)) {
            String sql = "SELECT rola FROM osoba WHERE ime = ? AND prezime = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, ime);
                statement.setString(2, prezime);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String uloga = resultSet.getString("rola");
                        System.out.println("Pronađena uloga: " + uloga);
                        return uloga;
                    } else {
                        System.out.println("Nije pronađena uloga za korisnika: " + ime + " " + prezime);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
