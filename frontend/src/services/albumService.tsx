import apiClient from './axios';

import type { Album } from '../types';

export const albumService = {
    getAlbum: async (albumId: string): Promise<Album> => {
        const response = await apiClient.get(`/api/albums/${albumId}`);
        return response.data;
    },
};
