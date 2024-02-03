package services;

import model.User;
import repo.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

// Responsible for connecting to the UserRepo instance to get information about users
public class LoginService {
    private final UserRepository userRepo;

    public LoginService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    // Find if username exists in the DB
    public boolean userExists(String username) {
        ResultSet userData = userRepo.getUserData(username);

        return userData != null;
    }

    // Given a username, imports the user's data and assigns it to the user instance
    public boolean importData(String username, User user){
        try {
            ResultSet userData = userRepo.getUserData(username);
            user.setRefreshToken(userData.getString("refresh_token"));
            user.setAccessCode(userData.getString("access_code"));
            user.setAuthorized(true);
            return true;
        }catch (SQLException e){
            System.out.println("An error has occurred: " + e);
            return false;
        }
    }
}
