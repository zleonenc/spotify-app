import {
    describe,
    it,
    expect,
    vi,
    beforeEach
} from 'vitest';

import apiClient from '../axios';
import { authService } from '../authService';

// Mock the axios module
vi.mock('../axios', () => ({
    default: {
        delete: vi.fn(),
    },
}));

describe('authService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    describe('logout', () => {
        it('logout CALLS delete endpoint', async () => {
            // Given
            const mockDelete = vi.mocked(apiClient.delete);
            mockDelete.mockResolvedValue({ data: {} });

            // When
            await authService.logout();

            // Then
            expect(mockDelete).toHaveBeenCalledWith('/auth/logout');
            expect(mockDelete).toHaveBeenCalledTimes(1);
        });

        it('logout THROWS and RETURNS error', async () => {
            // Given
            const mockDelete = vi.mocked(apiClient.delete);
            const error = new Error('Network error');
            mockDelete.mockRejectedValue(error);

            // When & Then
            await expect(authService.logout()).rejects.toThrow('Network error');
            expect(mockDelete).toHaveBeenCalledWith('/auth/logout');
        });

        it('logout THROWS and RETURNS unauthorizedError', async () => {
            // Given
            const mockDelete = vi.mocked(apiClient.delete);
            const unauthorizedError = {
                response: { status: 401, data: { message: 'Unauthorized' } }
            };
            mockDelete.mockRejectedValue(unauthorizedError);

            // When & Then
            await expect(authService.logout()).rejects.toEqual(unauthorizedError);
            expect(mockDelete).toHaveBeenCalledWith('/auth/logout');
        });

        it('logout THROWS and RETURNS serverError', async () => {
            // Given
            const mockDelete = vi.mocked(apiClient.delete);
            const serverError = {
                response: { status: 500, data: { message: 'Internal Server Error' } }
            };
            mockDelete.mockRejectedValue(serverError);

            // When & Then
            await expect(authService.logout()).rejects.toEqual(serverError);
            expect(mockDelete).toHaveBeenCalledWith('/auth/logout');
        });

        it('logout THROWS and RETURNS networkError', async () => {
            // Given
            const mockDelete = vi.mocked(apiClient.delete);
            const networkError = new Error('Network Error');
            networkError.name = 'NetworkError';
            mockDelete.mockRejectedValue(networkError);

            // When & Then
            await expect(authService.logout()).rejects.toThrow('Network Error');
            expect(mockDelete).toHaveBeenCalledWith('/auth/logout');
        });
    });
});
