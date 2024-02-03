package util.parse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Album;

import java.util.ArrayList;
import java.util.List;

// Parses new release data
public class NewReleasesParser implements Parser<Album>{
    @Override
    public List<Album> parse(String response){
        List<Album> albums = new ArrayList<>();

        try {
            JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
            JsonObject jsonAlbums = jsonData.getAsJsonObject("albums");

            for (JsonElement item : jsonAlbums.get("items").getAsJsonArray()) {
                List<String> artists = new ArrayList<>();
                JsonObject album = item.getAsJsonObject();
                JsonArray jsonArtists = album.getAsJsonArray("artists");
                String url = album.getAsJsonObject("external_urls").get("spotify").getAsString();
                String name = album.get("name").getAsString();

                for (JsonElement jsonArtist : jsonArtists) {
                    JsonObject artist = jsonArtist.getAsJsonObject();
                    artists.add(artist.get("name").getAsString());
                }

                albums.add(new Album(name, artists, url));
            }
        }catch(NullPointerException | IllegalStateException e){
            System.out.println("Error parsing data: " + e);
        }

        return albums;
    }
}
