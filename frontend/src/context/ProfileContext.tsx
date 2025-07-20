import {
    createContext,
    useContext,
    useState,
    useEffect,
    useCallback,
    useMemo,
    type ReactNode
} from 'react';

import apiClient from '../services/axios';

import { useAuth } from './AuthContext';

import type {
    SpotifyProfile,
    Artist,
    TopArtists,
    Track,
    TopTracks
} from '../types';

interface ProfileContextType {
    // Profile
    profile: SpotifyProfile | null;
    profileLoading: boolean;
    profileError: string | null;

    // Top artists
    topArtists: Artist[] | null;
    topArtistsLoading: boolean;
    topArtistsError: string | null;

    // Top Tracks
    topTracks: Track[] | null;
    topTracksLoading: boolean;
    topTracksError: string | null;

    fetchProfile: () => Promise<void>;
    fetchTopArtists: (limit?: number) => Promise<void>;
    fetchTopTracks: (limit?: number) => Promise<void>;
}

const ProfileContext = createContext<ProfileContextType | undefined>(undefined);

export const ProfileProvider = ({ children }: { children: ReactNode }) => {
    const { userId, logout } = useAuth();

    // Profile
    const [profile, setProfile] = useState<SpotifyProfile | null>(null);
    const [profileLoading, setProfileLoading] = useState(true);
    const [profileError, setProfileError] = useState<string | null>(null);

    // Top artists
    const [topArtists, setTopArtists] = useState<Artist[]>([]);
    const [topArtistsLoading, setTopArtistsLoading] = useState(false);
    const [topArtistsError, setTopArtistsError] = useState<string | null>(null);

    // Top tracks
    const [topTracks, setTopTracks] = useState<Track[]>([]);
    const [topTracksLoading, setTopTracksLoading] = useState(false);
    const [topTracksError, setTopTracksError] = useState<string | null>(null);

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

    const fetchTopTracks = useCallback(async (limit: number = 20): Promise<void> => {
        if (!userId) {
            setTopTracksError('Unauthenticated');
            return;
        }

        try {
            setTopTracksLoading(true);
            setTopTracksError(null);

            const response = await apiClient.get(`/api/me/top/tracks?limit=${limit}`);

            const data: TopTracks = response.data;
            setTopTracks(data.items || []);
        } catch (err: any) {
            console.error('Error fetching top tracks:', err);

            if (err.response?.status === 401) {
                logout();
                setTopTracksError('Session expired. Log in again.');
            } else {
                setTopTracksError('Failed to load top tracks. Try again.');
            }
        } finally {
            setTopTracksLoading(false);
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
        topTracks,
        topTracksLoading,
        topTracksError,
        fetchProfile,
        fetchTopArtists,
        fetchTopTracks
    }), [profile, profileLoading, profileError, topArtists, topArtistsLoading, topArtistsError, topTracks, topTracksLoading, topTracksError, fetchProfile, fetchTopArtists, fetchTopTracks]);

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
