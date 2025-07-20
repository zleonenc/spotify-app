import type { Artist } from '../Artist/Artist';

export interface TopArtists {
  items: Artist[];
  total: number;
  limit: number;
  offset: number;
    href: string;
    next: string | null;
    previous: string | null;
}
