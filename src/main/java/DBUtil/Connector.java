package DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class Connector {
        public static void main(String[] args) {
            // Postavke za povezivanje sa bazom podataka
            String url = "jdbc:mysql://localhost:3306/cs202-db";
            String user = "root";
            String password = "";

            // Objekat za povezivanje sa bazom podataka
            Connection connection = null;

            try {
                // Učitavanje MySQL drajvera
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Povezivanje sa bazom podataka
                connection = DriverManager.getConnection(url, user, password);

                // Ako se uspešno povežemo, možete izvršavati SQL upite ovde
                System.out.println("Uspesno povezan sa bazom podataka!");

                // Ovde možete dodati i izvršavati vaše SQL upite

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } finally {
                // Zatvaranje konekcije kad završite
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

