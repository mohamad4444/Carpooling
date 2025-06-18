package de.hskl.swtp.carpooling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JDBCConnectionHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/swtp_carpooling";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static JDBCConnectionHelper instance;

    private JDBCConnectionHelper() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC-Treiber konnte nicht geladen werden.", e);
        }
    }

    public static JDBCConnectionHelper getInstance() {
        if (instance == null) {
            synchronized (JDBCConnectionHelper.class) {
                if (instance == null) {
                    instance = new JDBCConnectionHelper();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
