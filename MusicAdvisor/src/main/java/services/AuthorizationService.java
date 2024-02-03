package services;

import com.sun.net.httpserver.HttpServer;
import config.SpotifyConfig;

import java.io.IOException;
import java.net.InetSocketAddress;

// This class gets authorization codes using the Spotify API
public class AuthorizationService {
    private String authCode;

    public AuthorizationService(){}

    // Gets authorization codes
    public String requestAuthorizationCode() throws IOException, InterruptedException {
        System.out.println("use this link to request the access code:");
        System.out.println(SpotifyConfig.CLIENT_AUTHORIZE_LINK);
        System.out.println("waiting for code...");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Create a context for the server on our designated port (8080)
        server.createContext("/", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String response;

            if(query != null){
                authCode = query;
            }

            if(authCode.contains("code")){
                response = "Got the code. Return back to your program.";
            }else{
                response = "Authorization code not found. Try again.";
            }

            // Returns response to the browswer
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });

        server.start();

        // 30 seconds to timeout
        int timeout = 30000;
        while (timeout > 0 && authCode == null) {
            Thread.sleep(1000);
            timeout -= 1000;
        }

        // Determines the success of the attempt
        if(authCode == null){
            return authCode;
        }else if(authCode.contains("access_denied")) {
            System.out.println("Error: access denied.");
        } else if (timeout <= 0) {
            System.out.println("Error: timeout.");
        }else if (authCode.contains("code")) {
            authCode = authCode.substring(5);
            System.out.println("code received");
        }else{
            System.out.println("An error has occurred while fetching an authorization code.");
        }

        server.stop(1);
        return authCode;
    }
}
