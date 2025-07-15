import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import AuthCallback from './pages/AuthCallback'
import Dashboard from './pages/Dashboard'
import { AuthProvider, useAuth, SpotifyTopArtistsProvider, ProfileProvider } from './context'

function AppRoutes() {
    const { isAuthenticated } = useAuth();

    return (
        <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/auth/callback" element={<AuthCallback />} />
            <Route
                path="/dashboard"
                element={
                    isAuthenticated ? (
                        <Dashboard />
                    ) : (
                        <Navigate to="/login" replace />
                    )
                }
            />
            <Route
                path="/"
                element={
                    isAuthenticated ? (
                        <Navigate to="/dashboard" replace />
                    ) : (
                        <Navigate to="/login" replace />
                    )
                }
            />
            <Route path="*"
                element={
                    isAuthenticated ? (
                        <Navigate to="/dashboard" replace />
                    ) : (
                        <Navigate to="/login" replace />
                    )
                }
            />
        </Routes>
    );
}

function App() {
    return (
        <AuthProvider>
            <ProfileProvider>
                <SpotifyTopArtistsProvider>
                    <BrowserRouter>
                        <AppRoutes />
                    </BrowserRouter>
                </SpotifyTopArtistsProvider>
            </ProfileProvider>
        </AuthProvider>
    );
}

export default App;
