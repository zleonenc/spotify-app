import {
    describe,
    it,
    expect,
    vi,
    beforeEach
} from 'vitest';

import apiClient from '../axios';
import { profileService } from '../profileService';

import type {
    SpotifyProfile,
    TopArtists,
    TopTracks
} from '../../types';

vi.mock('../axios', () => ({
    default: {
        get: vi.fn(),
    },
}));

describe('profileService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    describe('getProfile', () => {
        const mockProfile: SpotifyProfile = { id: 'user1', display_name: 'User One' } as any;

        it('getProfile RETURNS profileData', async () => {
            // Given
            const mockGet = vi.mocked(apiClient.get);
            mockGet.mockResolvedValue({ data: mockProfile });

            // When
            const result = await profileService.getProfile();

            // Then
            expect(result).toEqual(mockProfile);
            expect(mockGet).toHaveBeenCalledWith('/me/profile');
        });

        it('getProfile THROWS and RETURNS error', async () => {
            // Given
            const mockGet = vi.mocked(apiClient.get);
            const error = new Error('API Error');
            mockGet.mockRejectedValue(error);

            // When & Then
            await expect(profileService.getProfile()).rejects.toThrow('API Error');
            expect(mockGet).toHaveBeenCalledWith('/me/profile');
        });
    });

    describe('getTopArtists', () => {
        const mockTopArtists: TopArtists = { items: [], total: 0 } as any;

        it('getTopArtists RETURNS topArtistsData', async () => {
            // Given
            const mockGet = vi.mocked(apiClient.get);
            mockGet.mockResolvedValue({ data: mockTopArtists });

            // When
            const result = await profileService.getTopArtists();

            // Then
            expect(result).toEqual(mockTopArtists);
            expect(mockGet).toHaveBeenCalledWith('/me/top/artists?limit=20');
        });

        it('getTopArtists THROWS and RETURNS error', async () => {
            // Given
            const mockGet = vi.mocked(apiClient.get);
            const error = new Error('API Error');
            mockGet.mockRejectedValue(error);

            // When & Then
            await expect(profileService.getTopArtists()).rejects.toThrow('API Error');
            expect(mockGet).toHaveBeenCalledWith('/me/top/artists?limit=20');
        });
    });

    describe('getTopTracks', () => {
        const mockTopTracks: TopTracks = { items: [], total: 0 } as any;

        it('getTopTracks RETURNS topTracksData', async () => {
            // Given
            const mockGet = vi.mocked(apiClient.get);
            mockGet.mockResolvedValue({ data: mockTopTracks });

            // When
            const result = await profileService.getTopTracks();

            // Then
            expect(result).toEqual(mockTopTracks);
            expect(mockGet).toHaveBeenCalledWith('/me/top/tracks?limit=20');
        });

        it('getTopTracks THROWS and RETURNS error', async () => {
            // Given
            const mockGet = vi.mocked(apiClient.get);
            const error = new Error('API Error');
            mockGet.mockRejectedValue(error);

            // When & Then
            await expect(profileService.getTopTracks()).rejects.toThrow('API Error');
            expect(mockGet).toHaveBeenCalledWith('/me/top/tracks?limit=20');
        });
    });
});
