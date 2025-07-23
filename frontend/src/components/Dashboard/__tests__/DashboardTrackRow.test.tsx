import { MemoryRouter } from 'react-router-dom';
import {
    render,
    screen
} from '@testing-library/react';
import {
    describe,
    it,
    expect
} from 'vitest';

import { PlayerProvider } from '../../../context';

import { type Track } from '../../../types';

import DashboardTrackRow from '../DashboardTrackRow';

const mockArtist = {
    id: 'artist1',
    name: 'Test Artist',
    popularity: 80,
    followers: { href: null, total: 1000 },
    genres: ['pop'],
    external_urls: { spotify: 'https://open.spotify.com/artist/artist1' },
    href: 'https://api.spotify.com/v1/artists/artist1',
    images: [{ url: 'https://img.com/artist1.jpg', height: 640, width: 640 }],
    type: 'artist',
    uri: 'spotify:artist:artist1',
};
const mockTrack: Track = {
    id: 'track123',
    name: 'Test Track',
    artists: [mockArtist],
    album: {
        id: 'album1',
        name: 'Test Album',
        album_type: 'album',
        total_tracks: 10,
        href: 'https://api.spotify.com/v1/albums/album1',
        images: [{ url: 'https://img.com/album1.jpg', height: 640, width: 640 }],
        external_urls: { spotify: 'https://open.spotify.com/album/album1' },
        release_date: '2020-01-01',
        type: 'album',
        uri: 'spotify:album:album1',
        artists: [mockArtist],
    },
    disc_number: 1,
    duration_ms: 180000,
    external_urls: { spotify: 'https://open.spotify.com/track/track123' },
    href: 'https://api.spotify.com/v1/tracks/track123',
    is_playable: true,
    popularity: 75,
    preview_url: 'https://example.com/preview',
    track_number: 1,
    type: 'track',
    uri: 'spotify:track:track123',
    explicit: false,
};

describe('DashboardTrackRow', () => {
    it('RENDERS track NAME and ARTIST', () => {
        render(
            <MemoryRouter>
                <PlayerProvider>
                    <DashboardTrackRow track={mockTrack} index={1} />
                </PlayerProvider>
            </MemoryRouter>
        );
        expect(screen.getByText('Test Track')).toBeInTheDocument();
        expect(screen.getByText('Test Artist')).toBeInTheDocument();
    });

    it('SHOWS album IMAGE', () => {
        render(
            <MemoryRouter>
                <PlayerProvider>
                    <DashboardTrackRow track={mockTrack} index={1} />
                </PlayerProvider>
            </MemoryRouter>
        );
        expect(screen.getByRole('img')).toHaveAttribute('src', 'https://img.com/album1.jpg');
    });
});
