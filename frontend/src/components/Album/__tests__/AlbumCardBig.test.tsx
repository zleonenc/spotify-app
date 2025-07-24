
import {
    render,
    screen
} from '@testing-library/react';
import {
    vi,
    describe,
    it,
    expect
} from 'vitest';

import { AlbumProvider, AuthProvider } from '../../../context';
import * as AlbumContextModule from '../../../context';

import AlbumCardBig from '../AlbumCardBig';

const mockAlbum = {
    id: 'album1',
    name: 'Test Album',
    album_type: 'album',
    total_tracks: 10,
    href: 'https://api.spotify.com/v1/albums/album1',
    images: [{ url: 'https://img.com/album1.jpg', height: 640, width: 640 }],
    external_urls: { spotify: 'https://open.spotify.com/album/album1' },
    release_date: '2020-10-10',
    type: 'album',
    uri: '',
    artists: [{
        id: 'a1',
        name: 'Artist 1',
        popularity: 80,
        followers: { href: null, total: 1000 },
        genres: ['pop'],
        external_urls: { spotify: 'https://spotify.com/artist/a1' },
        href: '',
        images: [],
        type: 'artist',
        uri: ''
    }],
    tracks: {
        href: null,
        limit: null,
        next: null,
        offset: null,
        previous: null,
        total: null,
        items: []
    },
};

import { MemoryRouter } from 'react-router-dom';

const renderWithMockedAlbum = (album = mockAlbum, albumLoading = false, albumError = null) => {
    vi.spyOn(AlbumContextModule, 'useAlbum').mockReturnValue({
        album,
        albumLoading,
        albumError,
        fetchAlbum: vi.fn(),
    });
    return render(
        <MemoryRouter>
            <AuthProvider>
                <AlbumProvider>
                    <AlbumCardBig albumId="1" />
                </AlbumProvider>
            </AuthProvider>
        </MemoryRouter>
    );
};

describe('AlbumCardBig', () => {
    it('RENDERS album NAME and IMAGE', () => {
        renderWithMockedAlbum();
        expect(screen.getByText('Test Album')).toBeInTheDocument();
        expect(screen.getByRole('img')).toHaveAttribute('src', 'https://img.com/album1.jpg');
    });

    it('SHOWS ARTIST and RELEASE_YEAR', () => {
        renderWithMockedAlbum();
        expect(screen.getByText('Artist 1')).toBeInTheDocument();
        expect(screen.getByText('2020')).toBeInTheDocument();
    });
});
