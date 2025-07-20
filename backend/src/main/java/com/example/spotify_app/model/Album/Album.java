package com.example.spotify_app.model.Album;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.spotify_app.model.common.ExternalUrls;
import com.example.spotify_app.model.common.Image;
import com.example.spotify_app.model.Artist.Artist;
import com.example.spotify_app.model.Track.Track;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @JsonProperty("album_type")
    private String albumType;

    @JsonProperty("total_tracks")
    private int totalTracks;

    @JsonProperty("href")
    private String href;

    @JsonProperty("id")
    private String id;

    @JsonProperty("images")
    private Image[] images;

    @JsonProperty("name")
    private String name;

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("type")
    private String type;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("artists")
    private Artist[] artists;

    @JsonProperty("tracks")
    private TracksResponse tracks;

    @JsonProperty("genres")
    private String[] genres;

    @JsonProperty("label")
    private String label;

    @JsonProperty("popularity")
    private int popularity;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TracksResponse {
        @JsonProperty("href")
        private String href;

        @JsonProperty("limit")
        private int limit;

        @JsonProperty("next")
        private String next;

        @JsonProperty("offset")
        private int offset;

        @JsonProperty("previous")
        private String previous;

        @JsonProperty("total")
        private int total;

        @JsonProperty("items")
        private Track[] items;
    }
}
