package main;
import controller.MenuController;
import model.User;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        //Initializes program with a new user.
        User user = new User();
        MenuController menu = new MenuController(user);
        menu.run();
    }
}
