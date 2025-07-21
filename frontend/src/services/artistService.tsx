import apiClient from './axios';

import type {
    Artist,
    ArtistAlbums,
    ArtistTopTracks
} from '../types';

export const artistService = {
    getArtist: async (artistId: string): Promise<Artist> => {
        const response = await apiClient.get(`/api/artists/${artistId}`);
        return response.data;
    },
    getArtistAlbums: async (artistId: string): Promise<ArtistAlbums> => {
        const response = await apiClient.get(`/api/artists/${artistId}/albums`);
        return response.data;
    },
    getArtistTopTracks: async (artistId: string): Promise<ArtistTopTracks> => {
        const response = await apiClient.get(`/api/artists/${artistId}/top-tracks`);
        return response.data;
    },
};
