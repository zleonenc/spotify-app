import {
    createContext,
    useContext,
    useState,
    useCallback,
    useMemo,
    type ReactNode
} from 'react';

import { useAuth } from './AuthContext';

import { artistService } from '../services';

import type {
    Artist,
    ArtistAlbums,
    ArtistTopTracks
} from '../types';

interface ArtistContextType {
    artist: Artist | null;
    artistLoading: boolean;
    artistError: string | null;

    artistAlbums: ArtistAlbums | null;
    artistAlbumsLoading: boolean;
    artistAlbumsError: string | null;

    artistTopTracks: ArtistTopTracks | null;
    artistTopTracksLoading: boolean;
    artistTopTracksError: string | null;

    fetchArtist: (artistId: string) => Promise<void>;
    fetchArtistAlbums: (artistId: string) => Promise<void>;
    fetchArtistTopTracks: (artistId: string) => Promise<void>;
}

const ArtistContext = createContext<ArtistContextType | undefined>(undefined);

export const ArtistProvider = ({ children }: { children: ReactNode }) => {
    const { userId, logout } = useAuth();

    const [artist, setArtist] = useState<Artist | null>(null);
    const [artistLoading, setArtistLoading] = useState(false);
    const [artistError, setArtistError] = useState<string | null>(null);

    const [artistAlbums, setArtistAlbums] = useState<ArtistAlbums | null>(null);
    const [artistAlbumsLoading, setArtistAlbumsLoading] = useState(false);
    const [artistAlbumsError, setArtistAlbumsError] = useState<string | null>(null);

    const [artistTopTracks, setArtistTopTracks] = useState<ArtistTopTracks | null>(null);
    const [artistTopTracksLoading, setArtistTopTracksLoading] = useState(false);
    const [artistTopTracksError, setArtistTopTracksError] = useState<string | null>(null);

    const fetchArtist = useCallback(async (artistId: string): Promise<void> => {
        if (!userId) {
            setArtistError('Unauthenticated');
            setArtistLoading(false);
            throw new Error('Unauthenticated');
        }

        try {
            setArtistLoading(true);
            setArtistError(null);

            const data = await artistService.getArtist(artistId);
            setArtist(data);
        } catch (err: any) {
            console.error('Error fetching artist:', err);

            if (err.response?.status === 401) {
                logout();
                setArtistError('Session expired. Log in again.');
            } else {
                setArtistError('Failed to load artist. Try again.');
            }
            throw err;
        } finally {
            setArtistLoading(false);
        }
    }, [userId, logout]);

    const fetchArtistAlbums = useCallback(async (artistId: string): Promise<void> => {
        if (!userId) {
            setArtistAlbumsError('Unauthenticated');
            setArtistAlbumsLoading(false);
            throw new Error('Unauthenticated');
        }

        try {
            setArtistAlbumsLoading(true);
            setArtistAlbumsError(null);

            const data = await artistService.getArtistAlbums(artistId);
            setArtistAlbums(data);
        } catch (err: any) {
            console.error('Error fetching artist albums:', err);
            if (err.response?.status === 401) {
                logout();
                setArtistAlbumsError('Session expired. Log in again.');
            } else {
                setArtistAlbumsError('Failed to load artist albums. Try again.');
            }
            throw err;
        } finally {
            setArtistAlbumsLoading(false);
        }
    }, [userId, logout]);

    const fetchArtistTopTracks = useCallback(async (artistId: string): Promise<void> => {
        if (!userId) {
            setArtistTopTracksError('Unauthenticated');
            setArtistTopTracksLoading(false);
            throw new Error('Unauthenticated');
        }

        try {
            setArtistTopTracksLoading(true);
            setArtistTopTracksError(null);

            const data = await artistService.getArtistTopTracks(artistId);
            setArtistTopTracks(data);
        } catch (err: any) {
            console.error('Error fetching artist top tracks:', err);
            if (err.response?.status === 401) {
                logout();
                setArtistTopTracksError('Session expired. Log in again.');
            } else {
                setArtistTopTracksError('Failed to load artist top tracks. Try again.');
            }
            throw err;
        } finally {
            setArtistTopTracksLoading(false);
        }
    }, [userId, logout]);

    const value = useMemo(() => ({
        artist,
        artistLoading,
        artistError,
        artistAlbums,
        artistAlbumsLoading,
        artistAlbumsError,
        artistTopTracks,
        artistTopTracksLoading,
        artistTopTracksError,
        fetchArtist,
        fetchArtistAlbums,
        fetchArtistTopTracks,
    }), [
        artist, artistLoading, artistError,
        artistAlbums, artistAlbumsLoading, artistAlbumsError,
        artistTopTracks, artistTopTracksLoading, artistTopTracksError,
        fetchArtist, fetchArtistAlbums, fetchArtistTopTracks
    ]);

    return (
        <ArtistContext.Provider value={value}>
            {children}
        </ArtistContext.Provider>
    );
};

export const useArtist = (): ArtistContextType => {
    const context = useContext(ArtistContext);
    if (!context) {
        throw new Error('useArtist must be used within an ArtistProvider');
    }
    return context;
};