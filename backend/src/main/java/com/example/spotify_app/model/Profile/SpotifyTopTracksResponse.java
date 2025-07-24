package com.example.spotify_app.model.Profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.spotify_app.model.Track.Track;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyTopTracksResponse {
    @JsonProperty("items")
    private Track[] items;

    @JsonProperty("total")
    private int total;

    @JsonProperty("limit")
    private Integer limit;

    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("href")
    private String href;

    @JsonProperty("next")
    private String next;

    @JsonProperty("previous")
    private String previous;
}
