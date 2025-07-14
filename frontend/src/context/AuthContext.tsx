import { createContext, useContext, useState, type ReactNode } from 'react';
import apiClient from '../services/axios';

interface AuthContextType {
    accessToken: string | null;
    setAccessToken: (token: string | null) => void;
    isAuthenticated: boolean;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [accessToken, setAccessTokenState] = useState<string | null>(
        localStorage.getItem('accessToken')
    );

    const setAccessToken = (token: string | null) => {
        setAccessTokenState(token);
        if (token) {
            localStorage.setItem('accessToken', token);
        } else {
            localStorage.removeItem('accessToken');
        }
    };

    const isAuthenticated = !!accessToken;

    const logout = async () => {
        try {
            await apiClient.get('/auth/logout');
        } catch (error) {
            console.error('Error during logout:', error);
        } finally {
            setAccessToken(null);
        }
    };

    const value = {
        accessToken,
        setAccessToken,
        isAuthenticated,
        logout,
    };

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