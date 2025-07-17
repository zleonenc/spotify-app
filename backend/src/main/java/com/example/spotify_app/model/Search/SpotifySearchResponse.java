package com.example.spotify_app.model.Search;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.spotify_app.model.Album.Album;
import com.example.spotify_app.model.Artist.Artist;
import com.example.spotify_app.model.Track.Track;
import com.example.spotify_app.model.common.PagedResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpotifySearchResponse {
    @JsonProperty("tracks")
    private PagedResponse<Track> tracks;

    @JsonProperty("artists")
    private PagedResponse<Artist> artists;

    @JsonProperty("albums")
    private PagedResponse<Album> albums;
}
