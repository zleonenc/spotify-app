import { createContext, useContext, useState, useEffect, useCallback, useMemo, type ReactNode } from 'react';
import { useAuth } from './AuthContext';
import apiClient from '../services/axios';

import type { SpotifyProfile, Artist, TopArtists } from '../types';

interface MeContextType {
    // Profile
    profile: SpotifyProfile | null;
    profileLoading: boolean;
    profileError: string | null;

    // Top artists
    topArtists: Artist[];
    topArtistsLoading: boolean;
    topArtistsError: string | null;

    fetchProfile: () => Promise<void>;
    fetchTopArtists: (limit?: number) => Promise<void>;
}

const MeContext = createContext<MeContextType | undefined>(undefined);

export const MeProvider = ({ children }: { children: ReactNode }) => {
    const { userId, logout } = useAuth();

    // Profile
    const [profile, setProfile] = useState<SpotifyProfile | null>(null);
    const [profileLoading, setProfileLoading] = useState(true);
    const [profileError, setProfileError] = useState<string | null>(null);

    // Top artists
    const [topArtists, setTopArtists] = useState<Artist[]>([]);
    const [topArtistsLoading, setTopArtistsLoading] = useState(false);
    const [topArtistsError, setTopArtistsError] = useState<string | null>(null);

    const fetchProfile = useCallback(async (): Promise<void> => {
        if (!userId) {
            setProfileError('Unauthenticated');
            setProfileLoading(false);
            return;
        }

        try {
            setProfileLoading(true);
            setProfileError(null);

            const response = await apiClient.get('/api/me/profile');

            const data: SpotifyProfile = response.data;
            setProfile(data);
        } catch (err: any) {
            console.error('Error fetching profile:', err);

            if (err.response?.status === 401) {
                logout();
                setProfileError('Session expired. Log in again.');
            } else {
                setProfileError('Failed to load profile. Try again.');
            }
        } finally {
            setProfileLoading(false);
        }
    }, [userId, logout]);

    const fetchTopArtists = useCallback(async (limit: number = 20): Promise<void> => {
        if (!userId) {
            setTopArtistsError('Unauthenticated');
            return;
        }

        try {
            setTopArtistsLoading(true);
            setTopArtistsError(null);

            const response = await apiClient.get(`/api/me/top/artists?limit=${limit}`);

            const data: TopArtists = response.data;
            setTopArtists(data.items || []);
        } catch (err: any) {
            console.error('Error fetching top artists:', err);

            if (err.response?.status === 401) {
                logout();
                setTopArtistsError('Session expired. Log in again.');
            } else {
                setTopArtistsError('Failed to load top artists. Try again.');
            }
        } finally {
            setTopArtistsLoading(false);
        }
    }, [userId, logout]);

    useEffect(() => {
        fetchProfile();
    }, [fetchProfile]);

    const value = useMemo(() => ({
        profile,
        profileLoading,
        profileError,
        topArtists,
        topArtistsLoading,
        topArtistsError,
        fetchProfile,
        fetchTopArtists,
    }), [profile, profileLoading, profileError, topArtists, topArtistsLoading, topArtistsError, fetchProfile, fetchTopArtists]);

    return (
        <MeContext.Provider value={value}>
            {children}
        </MeContext.Provider>
    );
};

export const useMe = (): MeContextType => {
    const context = useContext(MeContext);
    if (!context) {
        throw new Error('useMe must be used within a MeProvider');
    }
    return context;
};
