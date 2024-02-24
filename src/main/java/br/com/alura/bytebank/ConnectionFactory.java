package br.com.alura.bytebank;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    String url = "jdbc:postgresql://localhost:5432/bytebank";
    String user = System.getenv("DB_USER");
    String password = System.getenv("DB_PASSWORD");

    public Connection conectar() {
        try {
            String driver = "org.postgresql.Driver";
            Class.forName(driver);
            return createDataSource().getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}
