import type { Track } from "../Track";


export interface AlbumTracks {
  href: string | null;
  limit: number | null;
  next: string | null;
  offset: number | null;
  previous: string | null;
  total: number | null;
  items: Track[];
}