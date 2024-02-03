package model;

import java.util.List;

// Class models an album entity
public class Album {
    private final String name;
    private final List<String> artists;
    private final String url;

    public Album(String name, List<String> artists, String url) {
        this.name = name;
        this.artists = artists;
        this.url = url;
    }

    @Override
    public String toString() {
        return this.name + "\n" + artists + "\n" + url;
    }
}
