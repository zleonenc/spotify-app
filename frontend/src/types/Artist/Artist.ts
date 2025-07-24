import type {
    ExternalUrls,
    Followers,
    Image
} from '../Common';

export interface Artist {
    id: string;
    name: string;
    popularity: number;
    followers: Followers;
    genres: string[];
    external_urls: ExternalUrls;
    href: string;
    images: Image[];
    type: string;
    uri: string;
}
