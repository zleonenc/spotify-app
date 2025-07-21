import apiClient from './axios';

import type {
    SpotifyProfile,
    TopArtists,
    TopTracks
} from '../types';

export const profileService = {
    getProfile: async (): Promise<SpotifyProfile> => {
        const response = await apiClient.get('/api/me/profile');
        return response.data;
    },
    getTopArtists: async (limit = 20): Promise<TopArtists> => {
        const response = await apiClient.get(`/api/me/top/artists?limit=${limit}`);
        return response.data;
    },
    getTopTracks: async (limit = 20): Promise<TopTracks> => {
        const response = await apiClient.get(`/api/me/top/tracks?limit=${limit}`);
        return response.data;
    },
};
