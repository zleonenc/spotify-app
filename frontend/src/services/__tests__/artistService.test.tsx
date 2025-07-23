import {
    describe,
    it,
    expect,
    vi,
    beforeEach
} from 'vitest';

import apiClient from '../axios';
import { artistService } from '../artistService';

import type {
    Artist,
    ArtistAlbums,
    ArtistTopTracks
} from '../../types';

vi.mock('../axios', () => ({
    default: {
        get: vi.fn(),
    },
}));

describe('artistService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    describe('getArtist', () => {
        const mockArtist: Artist = {
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

        it('getArtist RETURNS artistData', async () => {
            // Given
            const artistId = 'artist1';
            const mockGet = vi.mocked(apiClient.get);
            mockGet.mockResolvedValue({ data: mockArtist });

            // When
            const result = await artistService.getArtist(artistId);

            // Then
            expect(result).toEqual(mockArtist);
            expect(mockGet).toHaveBeenCalledWith('/api/artists/artist1');
        });

        it('getArtist THROWS and RETURNS error', async () => {
            // Given
            const artistId = 'artist1';
            const mockGet = vi.mocked(apiClient.get);
            const error = new Error('API Error');
            mockGet.mockRejectedValue(error);

            // When & Then
            await expect(artistService.getArtist(artistId)).rejects.toThrow('API Error');
            expect(mockGet).toHaveBeenCalledWith('/api/artists/artist1');
        });
    });

    describe('getArtistAlbums', () => {
        const mockAlbums: ArtistAlbums = { items: [], total: 0 } as any;

        it('getArtistAlbums RETURNS albumsData', async () => {
            // Given
            const artistId = 'artist1';
            const mockGet = vi.mocked(apiClient.get);
            mockGet.mockResolvedValue({ data: mockAlbums });

            // When
            const result = await artistService.getArtistAlbums(artistId);

            // Then
            expect(result).toEqual(mockAlbums);
            expect(mockGet).toHaveBeenCalledWith('/api/artists/artist1/albums');
        });

        it('getArtistAlbums THROWS and RETURNS error', async () => {
            // Given
            const artistId = 'artist1';
            const mockGet = vi.mocked(apiClient.get);
            const error = new Error('API Error');
            mockGet.mockRejectedValue(error);

            // When & Then
            await expect(artistService.getArtistAlbums(artistId)).rejects.toThrow('API Error');
            expect(mockGet).toHaveBeenCalledWith('/api/artists/artist1/albums');
        });
    });

    describe('getArtistTopTracks', () => {
        const mockTopTracks: ArtistTopTracks = { tracks: [] } as any;

        it('getArtistTopTracks RETURNS topTracksData', async () => {
            // Given
            const artistId = 'artist1';
            const mockGet = vi.mocked(apiClient.get);
            mockGet.mockResolvedValue({ data: mockTopTracks });

            // When
            const result = await artistService.getArtistTopTracks(artistId);

            // Then
            expect(result).toEqual(mockTopTracks);
            expect(mockGet).toHaveBeenCalledWith('/api/artists/artist1/top-tracks');
        });

        it('getArtistTopTracks THROWS and RETURNS error', async () => {
            // Given
            const artistId = 'artist1';
            const mockGet = vi.mocked(apiClient.get);
            const error = new Error('API Error');
            mockGet.mockRejectedValue(error);

            // When & Then
            await expect(artistService.getArtistTopTracks(artistId)).rejects.toThrow('API Error');
            expect(mockGet).toHaveBeenCalledWith('/api/artists/artist1/top-tracks');
        });
    });
});
