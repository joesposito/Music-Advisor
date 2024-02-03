package repo;

import config.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// This class initiates the connection to the MySQL database
public class MySQLConnectionManager {
    private static final String JDBC_URL = DBConfig.JDBC_URL;
    private static final String USER = DBConfig.USER;
    private static final String PASSWORD = DBConfig.PASSWORD;
    private Connection connection;

    public MySQLConnectionManager(){
        createConnection();
    }

    // Establishes connection to database
    public boolean createConnection(){
        try{
            Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

            if(connection.isValid(0)){
                this.connection = connection;
                return true;
            }
        }catch(SQLException e){
            System.out.println("An error has occurred while creating a connection: " + e);
        }
        return false;
    }

    public Connection getConnection(){
        return connection;
    }
}
