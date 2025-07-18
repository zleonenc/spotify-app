import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';

import { useAuth } from './AuthContext';

import apiClient from '../services/axios';

import type { Album } from '../types';

interface AlbumContextType {
    album: Album | null;
    albumLoading: boolean;
    albumError: string | null;

    fetchAlbum: (albumId: string) => Promise<void>;
}

const AlbumContext = createContext<AlbumContextType | undefined>(undefined);

export const AlbumProvider = ({ children }: { children: ReactNode }) => {
    const { userId, logout } = useAuth();

    const [album, setAlbum] = useState<Album | null>(null);
    const [albumLoading, setAlbumLoading] = useState(false);
    const [albumError, setAlbumError] = useState<string | null>(null);

    const fetchAlbum = useCallback(async (albumId: string): Promise<void> => {
        if (!userId) {
            setAlbumError('Unauthenticated');
            setAlbumLoading(false);
            throw new Error('Unauthenticated');
        }

        try {
            setAlbumLoading(true);
            setAlbumError(null);

            const response = await apiClient.get(`/api/albums/${albumId}`);
            setAlbum(response.data);
        } catch (err: any) {
            console.error('Error fetching album:', err);

            if (err.response?.status === 401) {
                logout();
                setAlbumError('Session expired. Log in again.');
            } else {
                setAlbumError('Failed to load album. Try again.');
            }
            throw err;
        } finally {
            setAlbumLoading(false);
        }
    }, [userId, logout]);

    const value = useMemo(() => ({
        album,
        albumLoading,
        albumError,
        fetchAlbum,
    }), [album, albumLoading, albumError, fetchAlbum]);

    return (
        <AlbumContext.Provider value={value}>
            {children}
        </AlbumContext.Provider>
    );
};

export const useAlbum = (): AlbumContextType => {
    const context = useContext(AlbumContext);
    if (!context) {
        throw new Error('useAlbum must be used within an AlbumProvider');
    }
    return context;
};