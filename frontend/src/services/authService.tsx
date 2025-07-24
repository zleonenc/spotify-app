import apiClient from './axios';

export const authService = {
    logout: async (): Promise<void> => {
        await apiClient.delete('/auth/logout');
    },
};
