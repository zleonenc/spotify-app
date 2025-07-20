import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'

import { Box } from '@mui/material'

import { AuthProvider, useAuth, ProfileProvider, SearchProvider } from './context'

import LoginPage from './pages/LoginPage'
import DashboardPage from './pages/DashboardPage'
import SearchPage from './pages/SearchPage'
import AlbumPage from './pages/AlbumPage'
import ArtistPage from './pages/ArtistPage'
import AuthCallback from './pages/AuthCallback'

import TopBar from './components/TopBar/TopBar'

function AppRoutes() {
    const { isAuthenticated } = useAuth();

    if (!isAuthenticated) {
        return (
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/auth/callback" element={<AuthCallback />} />
                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        );
    }

    return (
        <Box>
            <TopBar />
            <Routes>
                <Route path="/dashboard" element={<DashboardPage />} />
                <Route path="/search" element={<SearchPage />} />
                <Route path="/album/:id" element={<AlbumPage />} />
                <Route path="/artist/:id" element={<ArtistPage />} />
                <Route path="/" element={<Navigate to="/dashboard" replace />} />
                <Route path="*" element={<Navigate to="/dashboard" replace />} />
            </Routes>
        </Box>
    );
}

function App() {
    return (
        <AuthProvider>
            <ProfileProvider>
                <SearchProvider>
                    <BrowserRouter>
                        <AppRoutes />
                    </BrowserRouter>
                </SearchProvider>
            </ProfileProvider>
        </AuthProvider>
    );
}

export default App;
