import type { Track } from "../Track";

export interface TopTracks {
  items: Track[];
    total: number;
    limit: number;
    offset: number;
    href: string;
    next: string | null;
    previous: string | null;
}