package model;

// Class models a playlist entity
public class Playlist {
    String name;
    String url;

    public Playlist(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String toString() {
        return this.name + "\n" + this.url;
    }
}
