import {
    describe,
    it,
    expect,
    vi,
    beforeEach
} from 'vitest';

import apiClient from '../axios';
import { trackService } from '../trackService';

import type { Track } from '../../types';

// Mock axios module
vi.mock('../axios', () => ({
    default: {
        get: vi.fn(),
    },
}));

describe('trackService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    describe('getTrack', () => {
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

        it('getTrack RETURNS trackData', async () => {
            // Given
            const trackId = 'track123';
            const mockGet = vi.mocked(apiClient.get);
            mockGet.mockResolvedValue({ data: mockTrack });

            // When
            const result = await trackService.getTrack(trackId);

            // Then
            expect(result).toEqual(mockTrack);
            expect(mockGet).toHaveBeenCalledWith('/tracks/track123');
            expect(mockGet).toHaveBeenCalledTimes(1);
        });

        it('getTrack THROWS and RETURNS error', async () => {
            // Given
            const trackId = 'track123';
            const mockGet = vi.mocked(apiClient.get);
            const error = new Error('API Error');
            mockGet.mockRejectedValue(error);

            // When & Then
            await expect(trackService.getTrack(trackId)).rejects.toThrow('API Error');
            expect(mockGet).toHaveBeenCalledWith('/tracks/track123');
        });

        it('getTrack THROWS and RETURNS notFoundError', async () => {
            // Given
            const trackId = 'nonexistent-track';
            const mockGet = vi.mocked(apiClient.get);
            const notFoundError = {
                response: { status: 404, data: { message: 'Track not found' } }
            };
            mockGet.mockRejectedValue(notFoundError);

            // When & Then
            await expect(trackService.getTrack(trackId)).rejects.toEqual(notFoundError);
            expect(mockGet).toHaveBeenCalledWith('/tracks/nonexistent-track');
        });

        it('getTrack THROWS and RETURNS unauthorizedError', async () => {
            // Given
            const trackId = 'track123';
            const mockGet = vi.mocked(apiClient.get);
            const unauthorizedError = {
                response: { status: 401, data: { message: 'Unauthorized' } }
            };
            mockGet.mockRejectedValue(unauthorizedError);

            // When & Then
            await expect(trackService.getTrack(trackId)).rejects.toEqual(unauthorizedError);
            expect(mockGet).toHaveBeenCalledWith('/tracks/track123');
        });

        it('getTrack THROWS and RETURNS serverError', async () => {
            // Given
            const trackId = 'track123';
            const mockGet = vi.mocked(apiClient.get);
            const serverError = {
                response: { status: 500, data: { message: 'Internal Server Error' } }
            };
            mockGet.mockRejectedValue(serverError);

            // When & Then
            await expect(trackService.getTrack(trackId)).rejects.toEqual(serverError);
            expect(mockGet).toHaveBeenCalledWith('/tracks/track123');
        });

        it('getTrack THROWS and RETURNS networkError', async () => {
            // Given
            const trackId = 'track123';
            const mockGet = vi.mocked(apiClient.get);
            const networkError = new Error('Network Error');
            networkError.name = 'NetworkError';
            mockGet.mockRejectedValue(networkError);

            // When & Then
            await expect(trackService.getTrack(trackId)).rejects.toThrow('Network Error');
            expect(mockGet).toHaveBeenCalledWith('/tracks/track123');
        });
    });
});
