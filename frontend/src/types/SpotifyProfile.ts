export interface ProfileImage {
    url: string;
    height?: number;
    width?: number;
}

export interface ProfileFollowers {
    href?: string;
    total: number;
}

export interface ProfileExternalUrls {
    spotify: string;
}

export interface SpotifyProfile {
    id: string;
    display_name: string;
    email?: string;
    country?: string;
    product?: string;
    followers: ProfileFollowers;
    images: ProfileImage[];
    external_urls: ProfileExternalUrls;
}
