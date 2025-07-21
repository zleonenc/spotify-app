import apiClient from './axios';

export const authService = {
    logout: async (): Promise<void> => {
        await apiClient.delete('/api/auth/logout');
    },
};
