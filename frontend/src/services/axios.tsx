import axios from 'axios';

const apiClient = axios.create({
    baseURL: '/api',
});

// Include userId as Bearer token
apiClient.interceptors.request.use(
    (config) => {
        const userId = localStorage.getItem('userId');
        if (userId && !config.headers['Authorization']) {
            config.headers['Authorization'] = `Bearer ${userId}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default apiClient;