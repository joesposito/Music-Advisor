package services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import config.SpotifyConfig;
import model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

// Is responsible for fetching the access keys from Spotify's servers
public class AccessService{
    User user;
    HttpClient client;

    public AccessService(User user, HttpClient client){
        this.user = user;
        this.client = client;
    }

    // Determines where we can use refresh token or not to get an access code
    public boolean requestAccessCode() throws IOException, InterruptedException {
        if(user.getRefreshToken() != null){
            return requestRefreshedAccessCode();
        }else{
            return requestNewAccessCode();
        }
    }


    // Gets an access code using the authorization code the user supplies
    public boolean requestNewAccessCode() {
        if(user.getAuthCode() == null){
            return false;
        }

        try {
            // Make a proper request
            HttpRequest request = newRequest().POST(HttpRequest
                    .BodyPublishers.ofString("grant_type=" + SpotifyConfig.AUTH_GRANT_TYPE + "&code=" +
                            user.getAuthCode() + "&redirect_uri=" + SpotifyConfig.REDIRECT_URI)).build();

            System.out.println("making http request for access_token...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Request was no good
            if (response.statusCode() != 200) {
                return false;
            }

            JsonObject jsonCode = JsonParser.parseString(response.body()).getAsJsonObject();
            String accessCode = jsonCode.get("access_token").getAsString();
            // The difference is we want to make sure we obtain the refresh token and store it for future use
            String refreshToken = jsonCode.get("refresh_token").getAsString();

            user.setAccessCode(accessCode);
            user.setRefreshToken(refreshToken);
            return true;
        }catch(IOException | InterruptedException | NullPointerException e){
            System.out.println("An error has occured: " + e);
            return false;
        }
    }

    // We will use the refresh token to get a new access code
    public boolean requestRefreshedAccessCode() throws IOException, InterruptedException {
        if(user.getRefreshToken() == null){
            return false;
        }

        try {
            // Make a proper request
            HttpRequest request = newRequest().POST(HttpRequest
                    .BodyPublishers.ofString("grant_type=" + SpotifyConfig.REFRESH_GRANT_TYPE +
                            "&refresh_token=" + user.getRefreshToken())).build();

            System.out.println("making http request for access_token...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Request was no good
            if (response.statusCode() != 200) {
                return false;
            }

            JsonObject jsonCode = JsonParser.parseString(response.body()).getAsJsonObject();
            String accessCode = jsonCode.get("access_token").getAsString();
            user.setAccessCode(accessCode);
            return true;
        }catch(IOException | InterruptedException | NullPointerException e){
            System.out.println("An error has occurred: " + e);
            return false;
        }
    }

    // Provides a baseline request for the methods to finish
    public HttpRequest.Builder newRequest(){
        return HttpRequest.newBuilder()
                .uri(URI.create(SpotifyConfig.SPOTIFY_ACCESS_LINK))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " +
                        //Encodes the authorization header in base64 as per API guidelines.
                        Base64.getEncoder().encodeToString((SpotifyConfig.CLIENT_ID + ":"
                                + SpotifyConfig.CLIENT_SECRET)
                                .getBytes()))
                .POST(HttpRequest
                        .BodyPublishers.ofString("grant_type=" + SpotifyConfig.AUTH_GRANT_TYPE + "&code=" +
                                user.getAuthCode() + "&redirect_uri=" + SpotifyConfig.REDIRECT_URI));
    }
}
