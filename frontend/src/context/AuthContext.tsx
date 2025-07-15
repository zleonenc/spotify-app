import { createContext, useContext, useState, type ReactNode } from 'react';
import apiClient from '../services/axios';
import type { AuthContextType } from '../types';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [userId, setUserIdState] = useState<string | null>(
        localStorage.getItem('userId')
    );

    const setUserId = (id: string | null) => {
        setUserIdState(id);
        if (id) {
            localStorage.setItem('userId', id);
        } else {
            localStorage.removeItem('userId');
        }
    };

    const isAuthenticated = !!userId;

    const logout = async () => {
        if (userId) {
            try {
                await apiClient.delete('/api/auth/logout', {
                    headers: {
                        'Authorization': `Bearer ${userId}`
                    }
                });
            } catch (error) {
                console.error('Error during logout:', error);
            }
        }
        setUserId(null);
    };

    const value = {
        userId,
        setUserId,
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