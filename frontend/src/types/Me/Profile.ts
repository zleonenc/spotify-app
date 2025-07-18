import type { ExternalUrls, Followers, Image } from '../Common';

export interface SpotifyProfile {
  id: string;
  display_name: string;
  email: string;
  country: string;
  product: string;
  followers: Followers;
  images: Image[];
  external_urls: ExternalUrls;
}
