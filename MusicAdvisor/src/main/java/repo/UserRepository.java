package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// This class provides the CRUD operations for managing users in the MySQL database
public class UserRepository {
    private Connection connection;

    public UserRepository(MySQLConnectionManager connectionManager){
        connection = connectionManager.getConnection();
    }

    public void setConnection(Connection connection){
        this.connection =  connection;
    }

    // Creates new table to store user data if one does not exist
    public boolean createDB(){
        try {
            String query  = """
                    CREATE TABLE IF NOT EXISTS `users` (
                      `user_id` INT NOT NULL AUTO_INCREMENT,
                      `username` VARCHAR(20) NOT NULL,
                      `refresh_token` VARCHAR(300) NOT NULL,
                      `access_code` VARCHAR(300) NOT NULL,
                      PRIMARY KEY (`user_id`),
                      UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
                      UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);""";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            return true;
        }catch(NullPointerException | SQLException e){
            System.out.println("An error occurred while creating table: " + e);
            System.out.println("User data will not be saved. Restart the application " +
                    " and check your database connection to try again.\n");
            return false;
        }
    }

    // Gets user data from the DB given a username
    public ResultSet getUserData(String username){
        try {
            String query  = "SELECT * FROM users where username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                return null;
            }else{
                return resultSet;
            }

        }catch(NullPointerException | SQLException e){
            System.out.println("An error occurred while fetching user data: " + e + "\n");
            return null;
        }
    }

    // Adds user to DB given their data
    public boolean addUser(String username, String refreshToken, String accessCode){
        try {
            String query  = "INSERT INTO users (username, refresh_token, access_code) VALUES(?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, refreshToken);
            preparedStatement.setString(3, accessCode);

            preparedStatement.executeUpdate();
            return true;

        }catch(NullPointerException | SQLException e){
            System.out.println("An error occurred while adding a user: " + e + "\n");
            return false;
        }
    }

    // Updates user data
    public boolean updateUser(String username, String refreshToken, String accessCode){
        try {
            String query  = "UPDATE users SET refresh_token=?, access_code=? WHERE username=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, refreshToken);
            preparedStatement.setString(2, accessCode);
            preparedStatement.setString(3, username);
            preparedStatement.executeUpdate();

            return true;
        }catch(NullPointerException | SQLException e){
            System.out.println("An error occurred while updating a user: " + e + "\n");
            return false;
        }
    }
}
