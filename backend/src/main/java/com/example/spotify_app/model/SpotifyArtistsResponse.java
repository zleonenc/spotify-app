package com.example.spotify_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SpotifyArtistsResponse {
    @JsonProperty("items")
    private List<Artist> items;

    @JsonProperty("total")
    private int total;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("offset")
    private int offset;

    public SpotifyArtistsResponse() {
    }

    public SpotifyArtistsResponse(List<Artist> items, int total, int limit, int offset) {
        this.items = items;
        this.total = total;
        this.limit = limit;
        this.offset = offset;
    }

    public List<Artist> getItems() {
        return items;
    }

    public void setItems(List<Artist> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
