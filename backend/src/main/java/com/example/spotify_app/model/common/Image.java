package com.example.spotify_app.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @JsonProperty("url")
    private String url;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("width")
    private Integer width;

    @Override
    public String toString() {
        return "Image{url='" + url + '\'' + ", height=" + height + ", width=" + width + '}';
    }
}
