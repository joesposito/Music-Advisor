package controller;

import cache.CategoryCache;
import config.SpotifyConfig;
import model.User;
import repo.MySQLConnectionManager;
import repo.UserRepository;
import services.AccessService;
import services.AuthorizationService;
import services.LoginService;
import services.SpotifyDataService;
import util.parse.CategoriesParser;
import util.parse.NewReleasesParser;
import util.parse.Parser;
import util.parse.PlaylistsParser;
import view.PaginatedDisplay;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

// Responsible for handling all user input actions that are determined in the menu controller
public class MenuActionsHandler {
    public static List<String> actions = Arrays.asList("menu", "login", "auth", "featured", "new", "categories",
            "playlists", "next", "prev", "exit");
    private PaginatedDisplay display;
    private final User user;
    private final SpotifyDataService spotifyRequestService;
    private final UserRepository userRepo;
    private final HttpClient client;

    // Creates an action handler object so we can handle actions pertinent to a certain menu
    // Instantiates services and connections necessary for the program
    public MenuActionsHandler(User user) {
        this.user = user;
        client = HttpClient.newBuilder().build();
        spotifyRequestService = new SpotifyDataService(user, client);
        userRepo = new UserRepository(new MySQLConnectionManager());
        userRepo.createDB();
    }

    // Returns whether the user is authorized or not
    public boolean isAuthorized(){
        if(!user.isAuthorized()){
            System.out.println("User is not authorized or your access code may have " +
                    "expired, enter \"auth\" to (re)authorize.\n");
            return false;
        }

        return true;
    }

    // Prints out menu options
    public void menu(){
        for(String action : actions){
            System.out.println("> " + action);
        }

        System.out.println();
    }

    // Tries to log the user in, given a username
    public boolean login(String username){
        // No need to proceed if we've authorized the user then they must be logged in
        if(user.isAuthorized()){
            System.out.println("User is already authorized.\n");
            return true;
        }


        // We create a service to obtain user info in our DB
        LoginService loginService = new LoginService(userRepo);
        boolean userExists = loginService.userExists(username);

        // If a user with this username exists, we can import their data, including their access and refresh tokens
        if(userExists){
            System.out.println("---SUCCESS---\n");
            user.setUsername(username);
            user.setLoggedIn(true);
            return loginService.importData(username, user);
        }else{
            System.out.println("Username does not exist.\n");
            return false;
        }
    }

    // This method handles the logic behind obtaining authorization from Spotify
    public boolean authorize() throws IOException, InterruptedException {
        // Exit if we're authorized, not necessarily if we're logged in though.
        // We can be logged in but unauthorized in the case that our access key expires.
        if(user.isAuthorized()){
            System.out.println("User is already authorized.\n");
            return true;
        }

        // If we're not logged in, then we need to get a username from the user for future storage
        if (!user.isLoggedIn()) {
            String username = InputHandler.requestUsernameInput();
            user.setUsername(username);

            // We try and log the user in first to import their data as this username may already exist
            if(login(username)){
                return true;
            }

            // At this point it is the case that we have a new user, so we MUST get an authorization code
            AuthorizationService authorizationService = new AuthorizationService();
            String authCode = authorizationService.requestAuthorizationCode();
            user.setAuthCode(authCode);
        }

        // Obtain a fresh access code
        AccessService accessService = new AccessService(user, client);
        boolean accessCodeRequest = accessService.requestAccessCode();

        if (accessCodeRequest) {
            System.out.println("---SUCCESS---\n");
            user.setAuthorized(true);

            // If this is a new user we need to add it to the database, then we'll log them in
            if(!user.isLoggedIn()){
                userRepo.addUser(user.getUsername(), user.getRefreshToken(), user.getAccessCode());
                user.setLoggedIn(true);
            }else{
                userRepo.updateUser(user.getUsername(), user.getRefreshToken(), user.getAccessCode());
            }

            return true;
        } else {
            System.out.println("---AUTHENTICATION FAILED---\n");
            user.setAuthorized(false);
            user.setLoggedIn(false);
            return false;
        }
    }


    // Responsible for orchestrating the retrieval and printing of user requested data
    private <T> void fetchAndDisplayData(String endpoint, Parser<T> parser, String displayHeader) {
        String jsonData = spotifyRequestService.requestData(endpoint);

        // Very important in order to reduce API calls that we verify the user's authorization AFTER we request
        // the data. At this point we can determine if the access key is still active. If not, return.
        if(!isAuthorized()){return;}

        List<T> data = parser.parse(jsonData);

        //Prints data
        if (!data.isEmpty()) {
            display = new PaginatedDisplay<>(data);
            System.out.println("---" + displayHeader.toUpperCase() + "---");
            display.printNextPage();
        } else {
            System.out.println("No data available for " + displayHeader.toLowerCase() + ".");
        }
    }

    // Fetches and prints out featured playlists
    public void featured(){
        fetchAndDisplayData(SpotifyConfig.FEATURED_PLAYLISTS, new PlaylistsParser(), "FEATURED");
    }

    // Fetches and prints out new releases
    public void newReleases(){
        fetchAndDisplayData(SpotifyConfig.NEW_RELEASES, new NewReleasesParser(), "NEW RELEASES");
    }

    // Fetches and prints out categories
    public void categories(){
        fetchAndDisplayData(SpotifyConfig.CATEGORIES, new CategoriesParser(), "CATEGORIES");
    }

    // Fetches and prints out the playlists of a certain category
    public void playlists(String category) {
        try {
            // We must get the category id given a name
            // If we haven't yet cached the categories, we must make an API call
            if(CategoryCache.getCategoryHashMap().isEmpty()){
                (new CategoriesParser()).parse(spotifyRequestService.requestData(SpotifyConfig.CATEGORIES));
            }

            // Now we may determine the proper category id belonging to the category
            String category_id = CategoryCache.getCategoryHashMap()
                    .get(category)
                    .getId();

            // Fetch data as usual
            fetchAndDisplayData(SpotifyConfig.PLAYLISTS + "/" + category_id, new PlaylistsParser(),
                    category.toUpperCase()
                    + " PLAYLISTS");

        }catch(NullPointerException | NoSuchElementException e){
            System.out.println("Please enter a valid playlist.\n");
        }
    }

    // Prints the next page of data
    public void next(){
        if(!isAuthorized()){return;}
        display.printNextPage();
    }

    // Prints the previous page of data
    public void prev(){
        if(!isAuthorized()){return;}
        display.printPrevPage();
    }
}
