package com.example.spotify_app.model.Artist;

import com.example.spotify_app.model.Album.Album;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistAlbumsResponse {
    @JsonProperty("href")
    private String href;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("previous")
    private String previous;

    @JsonProperty("total")
    private int total;

    @JsonProperty("next")
    private String next;

    @JsonProperty("items")
    private Album[] albums;
}
