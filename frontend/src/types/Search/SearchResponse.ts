import type { PagedResponse } from '../Common';
import type { Track } from '../Track/Track';
import type { Artist } from '../Artist/Artist';
import type { Album } from '../Album/Album';

export interface SearchResponse {
  tracks: PagedResponse<Track>;
  artists: PagedResponse<Artist>;
  albums: PagedResponse<Album>;
}
