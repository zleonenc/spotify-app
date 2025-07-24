import type { Album } from '../Album/Album';

export interface ArtistAlbums {
  href: string;
  limit: number;
  offset: number;
  previous: string | null;
  total: number;
  next: string | null;
  items: Album[];
}
