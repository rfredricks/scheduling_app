package database;

import password.Password;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Database connection class.
    @author Rebecca Fredricks */
public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//localhost/";
    private static final String dbName = "client_schedule";

    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName + "?connectionTimeZone=SERVER";

    private static final String MYSQLjdbcDriver = "com.mysql.cj.jdbc.Driver";
    private static final String username = "sqlUser";

    private static Connection conn = null;

    /** Open a database connection. */
    public static void startConnection() {
        try{
            Class.forName(MYSQLjdbcDriver);
            conn = DriverManager.getConnection(jdbcURL, username, Password.getPassword());
            System.out.println("Connection successful");
        }
        catch (SQLException | ClassNotFoundException ignored){ }
    }

    /** Return the open database connection.
        @return the connection */
    public static Connection getConnection(){
        return conn;
    }

    /** Close the open database connection. */
    public static void closeConnection(){
        try {
            conn.close();
        }
        catch (Exception ignored){ }
    }

}
