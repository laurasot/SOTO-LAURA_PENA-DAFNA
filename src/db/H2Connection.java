package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Connection {
    private final static String DB_JDBC_DRIVER = "org.h2.Driver";
    private final static String DB_URL = "jdbc:h2:./cuentas"; // indica bd H2 ./cuentas indica ubicación y nombre de bd
    private final static String DB_USER = "sa";
    private final static String DB_PASS = "sa";
    public static Connection connectionDb () throws ClassNotFoundException, SQLException {
        Class.forName(DB_JDBC_DRIVER);
        return DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
    }
}
