package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDb {

    public static Connection conectar() {
        try {
            String driver = "org.postgresql.Driver";
            String url = "jdbc:postgresql://localhost:5432/bytebank";
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, password);

            System.out.println("Conexão estabelecida");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... x) {
        Connection conn = conectar();

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
