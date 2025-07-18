import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';

import { useAuth } from './AuthContext';

import apiClient from '../services/axios';

import type { SearchResponse } from '../types';

interface SearchContextType {
    searchResults: SearchResponse | null;
    searchLoading: boolean;
    searchError: string | null;

    search: (query: string, type: string, limit?: number, offset?: number) => Promise<void>;
}

const SearchContext = createContext<SearchContextType | undefined>(undefined);

export const SearchProvider = ({ children }: { children: ReactNode }) => {
    const { userId, logout } = useAuth();

    const [searchResults, setSearchResults] = useState<SearchResponse | null>(null);
    const [searchLoading, setSearchLoading] = useState(false);
    const [searchError, setSearchError] = useState<string | null>(null);

    const search = useCallback(async (
        query: string,
        type: string,
        limit: number = 20,
        offset: number = 0
    ): Promise<void> => {
        if (!userId) {
            setSearchError('Unauthenticated');
            setSearchLoading(false);
            throw new Error('Unauthenticated');
        }

        try {
            setSearchLoading(true);
            setSearchError(null);

            const params = new URLSearchParams({
                q: query,
                type,
                limit: limit.toString(),
                offset: offset.toString()
            });

            const response = await apiClient.get(`/api/search?${params}`);
            setSearchResults(response.data);
        } catch (err: any) {
            console.error('Error searching:', err);

            if (err.response?.status === 401) {
                logout();
                setSearchError('Session expired. Log in again.');
            } else {
                setSearchError('Failed to search. Try again.');
            }
            throw err;
        } finally {
            setSearchLoading(false);
        }
    }, [userId, logout]);

    const value = useMemo(() => ({
        searchResults,
        searchLoading,
        searchError,
        search,
    }), [searchResults, searchLoading, searchError, search]);

    return (
        <SearchContext.Provider value={value}>
            {children}
        </SearchContext.Provider>
    );
};

export const useSearch = (): SearchContextType => {
    const context = useContext(SearchContext);
    if (!context) {
        throw new Error('useSearch must be used within a SearchProvider');
    }
    return context;
};