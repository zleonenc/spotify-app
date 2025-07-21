import apiClient from './axios';

import type { SearchResponse } from '../types';

export const searchService = {
    search: async (
        query: string,
        type: string,
        limit = 20,
        offset = 0
    ): Promise<SearchResponse> => {
        const params = new URLSearchParams({
            q: query,
            type,
            limit: limit.toString(),
            offset: offset.toString()
        });

        const response = await apiClient.get(`/api/search?${params}`);
        return response.data;
    },
};
