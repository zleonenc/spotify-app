package com.example.spotify_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import com.example.spotify_app.model.common.ExternalUrls;
import com.example.spotify_app.model.common.Followers;
import com.example.spotify_app.model.common.Image;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyProfileResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("country")
    private String country;

    @JsonProperty("product")
    private String product;

    @JsonProperty("followers")
    private Followers followers;

    @JsonProperty("images")
    private List<Image> images;

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;
}
