package com.example.spotify_app.model.Album;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.spotify_app.model.Track.Track;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumTracksResponse {
    @JsonProperty("tracks")
    private Track[] tracks;
}
