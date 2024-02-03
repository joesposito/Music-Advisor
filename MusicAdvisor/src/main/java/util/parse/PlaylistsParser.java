package util.parse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Playlist;

import java.util.ArrayList;
import java.util.List;

// Parses playlist data
public class PlaylistsParser implements Parser<Playlist>{

    @Override
    public List<Playlist> parse(String response) {
        List<Playlist> playlists = new ArrayList<>();

        try {
            JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
            JsonObject jsonAlbums = jsonData.getAsJsonObject("playlists");

            for (JsonElement item : jsonAlbums.get("items").getAsJsonArray()) {
                JsonObject playlist = item.getAsJsonObject();
                String name = playlist.get("name").getAsString();
                String url = playlist.getAsJsonObject("external_urls").get("spotify").getAsString();
                playlists.add(new Playlist(name, url));
            }

        }catch (NullPointerException | IllegalStateException e){
            System.out.println("Error parsing data: " + e + "\n");
        }

        return playlists;
    }
}
