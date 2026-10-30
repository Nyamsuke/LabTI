package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Koneksi {

    private static final String URL
            = "jdbc:mysql://localhost:3306/valorant_joki"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=Asia/Jakarta"
            + "&characterEncoding=UTF-8";

    private static Connection connection;

    private Koneksi() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                throw new SQLException("Driver MySQL tidak ditemukan. Pastikan dependency mysql-connector-j tersedia di classpath.", ex);
            }
            connection = DriverManager.getConnection(URL, "root", "123");
        }
        return connection;
    }
}
