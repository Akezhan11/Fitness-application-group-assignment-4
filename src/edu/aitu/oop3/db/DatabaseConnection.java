package edu.aitu.oop3.db;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    public static final String URL = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.cwkmoedzdextltlplwvr";
    private static final String PASSWORD = loadPassword();
    private DatabaseConnection() {

    }
    private static String loadPassword() {
        Properties props = new Properties();
        try(InputStream input = new FileInputStream("config.properties")) {
            props.load(input);
            String value = props.getProperty("DB_PASSWORD");
            if(value == null || value.isBlank()){
                throw new RuntimeException("DB_PASSWORD is not set in config.properties");
            }
            return value;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load DB_PASSWORD from config.properities", e);
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
