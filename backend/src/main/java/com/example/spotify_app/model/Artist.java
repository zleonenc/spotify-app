package com.example.spotify_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Artist {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("popularity")
    private int popularity;

    @JsonProperty("followers")
    private Followers followers;

    @JsonProperty("genres")
    private String[] genres;

    public Artist() {
    }

    public Artist(String id, String name, int popularity, Followers followers, String[] genres) {
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.followers = followers;
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", popularity=" + popularity +
                ", followers=" + followers +
                ", genres=" + java.util.Arrays.toString(genres) +
                '}';
    }

    public static class Followers {
        @JsonProperty("total")
        private int total;

        public Followers() {
        }

        public Followers(int total) {
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "Followers{total=" + total + '}';
        }
    }
}
