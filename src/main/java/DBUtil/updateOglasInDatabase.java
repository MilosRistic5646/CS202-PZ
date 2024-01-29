package DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

    public class updateOglasInDatabase {
        public static void updateOglasInDatabase(int oglasId, String noviNaslov, String noviOpis, double novaCena, String novaBoja) {
            try {
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/cs202-db",
                        "root",
                        "");

                String query = "UPDATE Oglas o JOIN Automobil a ON o.automobil_id = a.auto_id SET a.model=?, a.opis=?, o.cena=?, a.boja=? WHERE o.id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, noviNaslov);
                preparedStatement.setString(2, noviOpis);
                preparedStatement.setDouble(3, novaCena);
                preparedStatement.setString(4, novaBoja);
                preparedStatement.setInt(5, oglasId);

                preparedStatement.executeUpdate();

                preparedStatement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

