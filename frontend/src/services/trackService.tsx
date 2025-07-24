import apiClient from './axios';

import type { Track } from '../types';

export const trackService = {
    getTrack: async (trackId: string): Promise<Track> => {
        const response = await apiClient.get(`/tracks/${trackId}`);
        return response.data;
    },
};
