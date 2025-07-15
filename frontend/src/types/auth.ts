export interface AuthContextType {
    userId: string | null;
    setUserId: (userId: string | null) => void;
    isAuthenticated: boolean;
    logout: () => void;
}
