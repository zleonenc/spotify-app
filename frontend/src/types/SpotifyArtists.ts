export interface Artist {
    id: string;
    name: string;
    popularity: number;
    followers: {
        total: number;
    };
    genres: string[];
}

export interface SpotifyArtistsResponse {
    items: Artist[];
    total: number;
    limit: number;
    offset: number;
}

