package services;

import config.SpotifyConfig;
import model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// This class is responsible for obtaining requests using the Spotify API
public class SpotifyDataService {
    private final User user;
    private HttpClient client;

    public SpotifyDataService(User user, HttpClient client){
        this.user = user;
        this.client = client;
    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    // Given a uri path, the request can be made to get the data
    public String requestData(String uri_path){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SpotifyConfig.DATA_URI + uri_path))
                .header("Authorization", "Bearer " + user.getAccessCode())
                .GET()
                .build();

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 401){
                user.setAuthorized(false);
                return null;
            }

            return response.body();
        }catch(IOException | InterruptedException e){
            System.out.println("An error has occurred while fetching data from the Spotify API: " + e);
            return null;
        }
    }
}
