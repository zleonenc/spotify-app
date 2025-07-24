import {
    describe,
    it,
    expect,
    vi,
    beforeEach
} from 'vitest';

import apiClient from '../axios';
import { albumService } from '../albumService';

import type { Album } from '../../types';

vi.mock('../axios', () => ({
    default: {
        get: vi.fn(),
    },
}));

describe('albumService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    describe('getAlbum', () => {
        const mockAlbum: Album = {
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
            artists: [],
        };

        it('getAlbum RETURNS albumData', async () => {
            // Given
            const albumId = 'album1';
            const mockGet = vi.mocked(apiClient.get);
            mockGet.mockResolvedValue({ data: mockAlbum });

            // When
            const result = await albumService.getAlbum(albumId);

            // Then
            expect(result).toEqual(mockAlbum);
            expect(mockGet).toHaveBeenCalledWith('/albums/album1');
        });

        it('getAlbum THROWS and RETURNS error', async () => {
            // Given
            const albumId = 'album1';
            const mockGet = vi.mocked(apiClient.get);
            const error = new Error('API Error');
            mockGet.mockRejectedValue(error);

            // When & Then
            await expect(albumService.getAlbum(albumId)).rejects.toThrow('API Error');
            expect(mockGet).toHaveBeenCalledWith('/albums/album1');
        });
    });
});
