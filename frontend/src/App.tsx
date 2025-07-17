import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { Box } from '@mui/material'
import LoginPage from './pages/LoginPage'
import AuthCallback from './pages/AuthCallback'
import Dashboard from './pages/DashboardPage'
import SearchPage from './pages/SearchPage'
import { AuthProvider, useAuth, MeProvider, SearchProvider } from './context'
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
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/search" element={<SearchPage />} />
                <Route path="/" element={<Navigate to="/dashboard" replace />} />
                <Route path="*" element={<Navigate to="/dashboard" replace />} />
            </Routes>
        </Box>
    );
}

function App() {
    return (
        <AuthProvider>
            <MeProvider>
                <SearchProvider>
                    <BrowserRouter>
                        <AppRoutes />
                    </BrowserRouter>
                </SearchProvider>
            </MeProvider>
        </AuthProvider>
    );
}

export default App;
