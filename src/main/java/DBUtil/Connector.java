package DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class Connector {
        public static void main(String[] args) {
            String url = "jdbc:mysql://localhost:3306/cs202-db";
            String user = "root";
            String password = "";

            Connection connection = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(url, user, password);

                System.out.println("Uspesno povezan sa bazom podataka!");


            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } finally {
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

