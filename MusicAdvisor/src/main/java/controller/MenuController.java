package controller;

import model.User;

import java.io.IOException;

// The menu controller determines the flow of the application given some user input
public class MenuController {
    private final MenuActionsHandler menuActionsHandler;
    private final User user;

    public MenuController(User user) {
        this.user = user;
        menuActionsHandler = new MenuActionsHandler(user);
    }

    // Initiates the menu
   public void run() throws IOException, InterruptedException {
        String keyWord = "";
        String specifier = "";

        while (!keyWord.equals("exit")) {
            // We request input
            String[] query = InputHandler.requestKeyInput();

            // It should be the case we have some input here
            if(query.length >= 1){
                keyWord = query[0];
            }

            // This means the user enter multiple words, we want the second grouping that resulted from the split
            if(query.length >= 2){
                specifier = query[1].strip();
            }

            switch (keyWord) {
                case "menu":
                    menuActionsHandler.menu();
                    break;
                case "login":
                    menuActionsHandler.login(specifier);
                    break;
                case "auth":
                    menuActionsHandler.authorize();
                    break;
                case "featured":
                    menuActionsHandler.featured();
                    break;
                case "new":
                    menuActionsHandler.newReleases();
                    break;
                case "categories":
                    menuActionsHandler.categories();
                    break;
                case "playlists":
                    menuActionsHandler.playlists(specifier);
                    break;
                case "next":
                    menuActionsHandler.next();
                    break;
                case "prev":
                    menuActionsHandler.prev();
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Invalid input. Please try again or type \"menu\" for options.");
                    break;
            }

            // We must reset the specifier here, it is not a guarantee the user will always enter one
            specifier = "";
        }

        System.out.println("---GOODBYE!---");
        System.exit(0);
    }
}


