package controller;

import java.util.NoSuchElementException;
import java.util.Scanner;

// Responsible for user input
public class InputHandler {
    // Gets a menu option
    public static String[] requestKeyInput () {
        String input;
        String[] inputs = new String[0];
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter an option, or type \"menu\" for options: ");

        try {
            while(true){
                input = (scanner.nextLine()).toLowerCase();
                // We want at most 2 words from the user in any of our prompts
                inputs = input.split("\\W", 2);
                System.out.println();

                if(MenuActionsHandler.actions.contains(inputs[0])){
                    return inputs;
                }else{
                    System.out.println("Invalid input. Please try again or type \"menu\" for options.");
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println("Input terminated.");
        }

        return inputs;
    }

    // Will get a valid username
    public static String requestUsernameInput(){
        String input = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please a new username (6 character minimum, 20 character maximum) to store your " +
                "access key: ");

        try {
            while(true){
                input = (scanner.nextLine()).toLowerCase();
                System.out.println();

                // Our username specifications
                if(input.length() >= 6 &&  input.length() <= 20){
                    return input;
                }else{
                    System.out.println("Invalid username (6 character minimum, 20 character maximum).");
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println("Input terminated.");
            return input;
        }
    }
}
