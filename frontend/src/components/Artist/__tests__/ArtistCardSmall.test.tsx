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

import ArtistCardSmall from '../ArtistCardSmall';

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

describe('ArtistCardSmall', () => {
    it('RENDERS artist NAME and IMAGE', () => {
        render(
            <MemoryRouter>
                <ArtistCardSmall artist={mockArtist} />
            </MemoryRouter>
        );
        expect(screen.getByText('Test Artist')).toBeInTheDocument();
        expect(screen.getByRole('img')).toHaveAttribute('src', 'https://img.com/artist1.jpg');
    });
});
