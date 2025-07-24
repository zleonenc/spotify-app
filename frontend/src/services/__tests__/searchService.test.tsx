import {
    describe,
    it,
    expect,
    vi,
    beforeEach
} from 'vitest';

import apiClient from '../axios';
import { searchService } from '../searchService';

import type { SearchResponse } from '../../types';

vi.mock('../axios', () => ({
    default: {
        get: vi.fn(),
    },
}));

describe('searchService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    describe('search', () => {
        const mockSearchResponse: SearchResponse = { tracks: { items: [] } } as any;

        it('search RETURNS searchData', async () => {
            // Given
            const query = 'test';
            const type = 'track';
            const limit = 20;
            const offset = 0;
            const mockGet = vi.mocked(apiClient.get);
            mockGet.mockResolvedValue({ data: mockSearchResponse });

            // When
            const result = await searchService.search(query, type, limit, offset);

            // Then
            expect(result).toEqual(mockSearchResponse);
            expect(mockGet).toHaveBeenCalledWith('/search?q=test&type=track&limit=20&offset=0');
        });

        it('search THROWS and RETURNS error', async () => {
            // Given
            const query = 'test';
            const type = 'track';
            const mockGet = vi.mocked(apiClient.get);
            const error = new Error('API Error');
            mockGet.mockRejectedValue(error);

            // When & Then
            await expect(searchService.search(query, type)).rejects.toThrow('API Error');
            expect(mockGet).toHaveBeenCalledWith('/search?q=test&type=track&limit=20&offset=0');
        });
    });
});
