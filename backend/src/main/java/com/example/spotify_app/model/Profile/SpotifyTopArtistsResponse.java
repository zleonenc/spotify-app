package com.example.spotify_app.model.Profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.spotify_app.model.Artist.Artist;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyTopArtistsResponse {
    @JsonProperty("items")
    private Artist[] items;

    @JsonProperty("total")
    private int total;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("href")
    private String href;

    @JsonProperty("next")
    private String next;

    @JsonProperty("previous")
    private String previous;
}
