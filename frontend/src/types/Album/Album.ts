import type {
    ExternalUrls,
    Image
} from '../Common';
import type { Artist } from '../Artist/Artist';
import type { AlbumTracks } from './AlbumTracks';

export interface Album {
    album_type: string;
    total_tracks: number;
    href: string;
    id: string;
    images: Image[];
    name: string;
    external_urls: ExternalUrls;
    release_date: string;
    type: string;
    uri: string;
    artists: Artist[];
    tracks?: AlbumTracks;
    genres?: string[];
    label?: string;
    popularity?: number;
}
