import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import { useAuth } from './AuthContext';
import apiClient from '../services/axios';
import type { Artist, SpotifyArtistsResponse } from '../types';

interface SpotifyTopArtistsContextType {
    artists: Artist[];
    loading: boolean;
    error: string | null;
    refetchArtists: () => void;
}

const SpotifyTopArtistsContext = createContext<SpotifyTopArtistsContextType | undefined>(undefined);

export const SpotifyTopArtistsProvider = ({ children }: { children: ReactNode }) => {
    const { userId, logout } = useAuth();
    const [artists, setArtists] = useState<Artist[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchTopArtists = async () => {
        if (!userId) {
            setError('No user ID available');
            setLoading(false);
            return;
        }

        try {
            setLoading(true);
            setError(null);

            const response = await apiClient.get('/api/me/top/artists');

            const data: SpotifyArtistsResponse = response.data;
            setArtists(data.items || []);
        } catch (err: any) {
            console.error('Error fetching top artists:', err);
            
            if (err.response?.status === 401) {
                logout();
                setError('Session expired. Please log in again.');
            } else {
                setError('Failed to load top artists. Please try again.');
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTopArtists();
    }, [userId, logout]);

    const refetchArtists = () => {
        fetchTopArtists();
    };

    const value = {
        artists,
        loading,
        error,
        refetchArtists,
    };

    return (
        <SpotifyTopArtistsContext.Provider value={value}>
            {children}
        </SpotifyTopArtistsContext.Provider>
    );
};

export const useSpotifyTopArtists = (): SpotifyTopArtistsContextType => {
    const context = useContext(SpotifyTopArtistsContext);
    if (!context) {
        throw new Error('useSpotifyTopArtists must be used within a SpotifyTopArtistsProvider');
    }
    return context;
};
