package com.example.spotify_app.model.Track;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.spotify_app.model.common.ExternalUrls;
import com.example.spotify_app.model.Album.Album;
import com.example.spotify_app.model.Artist.Artist;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Track {

    @JsonProperty("album")
    private Album album;

    @JsonProperty("artists")
    private Artist[] artists;

    @JsonProperty("disc_number")
    private int discNumber;

    @JsonProperty("duration_ms")
    private int durationMs;

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    @JsonProperty("href")
    private String href;

    @JsonProperty("id")
    private String id;

    @JsonProperty("is_playable")
    private Boolean isPlayable;

    @JsonProperty("name")
    private String name;

    @JsonProperty("popularity")
    private int popularity;

    @JsonProperty("preview_url")
    private String previewUrl;

    @JsonProperty("track_number")
    private int trackNumber;

    @JsonProperty("type")
    private String type;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("explicit")
    private Boolean explicit;
}
