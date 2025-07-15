package com.example.spotify_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.spotify_app.model.common.ExternalUrls;
import com.example.spotify_app.model.common.Followers;
import com.example.spotify_app.model.common.Image;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    @JsonProperty("href")
    private String href;

    @JsonProperty("images")
    private Image[] images;

    @JsonProperty("type")
    private String type;

    @JsonProperty("uri")
    private String uri;

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", popularity=" + popularity +
                ", followers=" + followers +
                ", genres=" + java.util.Arrays.toString(genres) +
                ", externalUrls=" + externalUrls +
                ", href='" + href + '\'' +
                ", images=" + java.util.Arrays.toString(images) +
                ", type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
