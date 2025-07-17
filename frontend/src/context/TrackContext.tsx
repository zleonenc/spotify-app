import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';
import { useAuth } from './AuthContext';
import apiClient from '../services/axios';

import type { Track } from '../types';

interface TrackContextType {
    track: Track | null;
    trackLoading: boolean;
    trackError: string | null;

    fetchTrack: (trackId: string) => Promise<void>;
}

const TrackContext = createContext<TrackContextType | undefined>(undefined);

export const TrackProvider = ({ children }: { children: ReactNode }) => {
    const { userId, logout } = useAuth();

    const [track, setTrack] = useState<Track | null>(null);
    const [trackLoading, setTrackLoading] = useState(false);
    const [trackError, setTrackError] = useState<string | null>(null);

    const fetchTrack = useCallback(async (trackId: string): Promise<void> => {
        if (!userId) {
            setTrackError('Unauthenticated');
            setTrackLoading(false);
            throw new Error('Unauthenticated');
        }

        try {
            setTrackLoading(true);
            setTrackError(null);

            const response = await apiClient.get(`/api/tracks/${trackId}`);
            setTrack(response.data);
        } catch (err: any) {
            console.error('Error fetching track:', err);

            if (err.response?.status === 401) {
                logout();
                setTrackError('Session expired. Log in again.');
            } else {
                setTrackError('Failed to load track. Try again.');
            }
            throw err;
        } finally {
            setTrackLoading(false);
        }
    }, [userId, logout]);

    const value = useMemo(() => ({
        track,
        trackLoading,
        trackError,
        fetchTrack,
    }), [track, trackLoading, trackError, fetchTrack]);

    return (
        <TrackContext.Provider value={value}>
            {children}
        </TrackContext.Provider>
    );
};

export const useTrack = (): TrackContextType => {
    const context = useContext(TrackContext);
    if (!context) {
        throw new Error('useTrack must be used within a TrackProvider');
    }
    return context;
};