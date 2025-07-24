import type {
    ExternalUrls,
    Image
} from '../Common';
import type { Artist } from '../Artist/Artist';

export interface TrackAlbum {
    id: string;
    name: string;
    album_type: string;
    total_tracks: number;
    href: string;
    images: Image[];
    external_urls: ExternalUrls;
    release_date: string;
    type: string;
    uri: string;
    artists: Artist[];
}

export interface Track {
    album: TrackAlbum;
    artists: Artist[];
    disc_number: number;
    duration_ms: number;
    external_urls: ExternalUrls;
    href: string;
    id: string;
    is_playable?: boolean;
    name: string;
    popularity: number;
    preview_url: string | null;
    track_number: number;
    type: string;
    uri: string;
    explicit?: boolean;
}
