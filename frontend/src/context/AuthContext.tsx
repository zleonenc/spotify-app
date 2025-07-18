import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';

import apiClient from '../services/axios';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthContextType {
    userId: string | null;
    setUserId: (userId: string | null) => void;
    isAuthenticated: boolean;
    logout: () => void;
}

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [userId, setUserIdState] = useState<string | null>(
        localStorage.getItem('userId')
    );

    const setUserId = useCallback((id: string | null) => {
        setUserIdState(id);
        if (id) {
            localStorage.setItem('userId', id);
        } else {
            localStorage.removeItem('userId');
        }
    }, []);

    const isAuthenticated = useMemo(() => !!userId, [userId]);

    const logout = useCallback(async () => {
        if (userId) {
            try {
                await apiClient.delete('/api/auth/logout');
            } catch (error) {
                console.error('Error during logout:', error);
            }
        }
        setUserId(null);
    }, [userId, setUserId]);

    const value = useMemo(() => ({
        userId,
        setUserId,
        isAuthenticated,
        logout,
    }), [userId, setUserId, isAuthenticated, logout]);

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};