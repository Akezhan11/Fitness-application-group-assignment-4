package edu.aitu.oop3.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    public static final String URL = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.cwkmoedzdextltlplwvr";
    private static final String PASSWORD = "v24bUAN69ShtPsw1";
    private DatabaseConnection() {

    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
