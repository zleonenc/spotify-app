import type { Artist } from '../Artist/Artist';

export interface TopArtists {
  items: Artist[];
  total: number;
  limit: number;
  offset: number;
}
