package com.example.spotify_app.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    @JsonProperty("href")
    private String href;

    @JsonProperty("limit")
    private Integer limit;

    @JsonProperty("next")
    private String next;

    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("previous")
    private String previous;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("items")
    private T[] items;
}
