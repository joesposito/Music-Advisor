package config;

// Stores Spotify API call information useful for building requests
public class SpotifyConfig {
    public static final String CLIENT_ID = "your_client_id_here";
    public static final String CLIENT_SECRET = "your_client_secret_here";
    public static final String RESPONSE_TYPE = "code";
    public static final String REDIRECT_URI = "http://localhost:8080";
    public static final String AUTH_GRANT_TYPE = "authorization_code";
    public static final String REFRESH_GRANT_TYPE = "refresh_token";
    public static final String SPOTIFY_AUTHORIZE_LINK = "https://accounts.spotify.com/authorize";
    public static final String SPOTIFY_ACCESS_LINK = "https://accounts.spotify.com/api/token";
    public static final String CLIENT_AUTHORIZE_LINK = SPOTIFY_AUTHORIZE_LINK + "?client_id=" + CLIENT_ID + "&response_type=" +
            RESPONSE_TYPE + "&redirect_uri=" + REDIRECT_URI;

    public static final String DATA_URI = "https://api.spotify.com";
    public static final String FEATURED_PLAYLISTS = "/v1/browse/featured-playlists";
    public static final String PLAYLISTS = "/v1/browse/playlists";
    public static final String NEW_RELEASES = "/v1/browse/new-releases";
    public static final String CATEGORIES = "/v1/browse/categories";
}
