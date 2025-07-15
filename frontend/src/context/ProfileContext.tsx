import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import { useAuth } from './AuthContext';
import apiClient from '../services/axios';
import type { SpotifyProfile } from '../types';

interface ProfileContextType {
    profile: SpotifyProfile | null;
    loading: boolean;
    error: string | null;
    refetchProfile: () => void;
}

const ProfileContext = createContext<ProfileContextType | undefined>(undefined);

export const ProfileProvider = ({ children }: { children: ReactNode }) => {
    const { userId, logout } = useAuth();
    const [profile, setProfile] = useState<SpotifyProfile | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchProfile = async () => {
        if (!userId) {
            setError('No user ID available');
            setLoading(false);
            return;
        }

        try {
            setLoading(true);
            setError(null);

            const response = await apiClient.get('/api/me/profile');

            const data: SpotifyProfile = response.data;
            setProfile(data);
        } catch (err: any) {
            console.error('Error fetching profile:', err);
            
            if (err.response?.status === 401) {
                logout();
                setError('Session expired. Please log in again.');
            } else {
                setError('Failed to load profile. Please try again.');
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchProfile();
    }, [userId, logout]);

    const refetchProfile = () => {
        fetchProfile();
    };

    const value = {
        profile,
        loading,
        error,
        refetchProfile,
    };

    return (
        <ProfileContext.Provider value={value}>
            {children}
        </ProfileContext.Provider>
    );
};

export const useProfile = (): ProfileContextType => {
    const context = useContext(ProfileContext);
    if (!context) {
        throw new Error('useProfile must be used within a ProfileProvider');
    }
    return context;
};
