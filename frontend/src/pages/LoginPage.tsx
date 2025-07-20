import {
    Button,
    Container,
    Typography,
    Box,
    Alert
} from '@mui/material';
import { useEffect, useState } from 'react';
import { useAuth } from '../context';

const LoginPage = () => {
    const { setUserId } = useAuth();
    const [showLogoutMessage, setShowLogoutMessage] = useState(false);

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('logged_out') === 'true') {
            setShowLogoutMessage(true);
            setUserId(null);
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    }, [setUserId]);

    const handleLogin = async () => {
        try {
            window.location.href = 'http://localhost:8080/auth/spotify';
        } catch (error) {
            console.error('Error during login:', error);
        }
    };

    return (
        <Container maxWidth="sm" style={{ textAlign: 'center', marginTop: '50px' }}>
            {showLogoutMessage && (
                <Alert
                    severity="success"
                    sx={{ mb: 3 }}
                    onClose={() => setShowLogoutMessage(false)}
                >
                    You have been successfully logged out.
                </Alert>
            )}
            <Box sx={{ mb: 4 }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    Welcome to Spotify App
                </Typography>
                <Typography variant="body1" gutterBottom>
                    Please log in to continue.
                </Typography>
            </Box>
            <Button
                variant="contained"
                color="primary"
                size="large"
                onClick={handleLogin}>
                Login with Spotify
            </Button>
        </Container>
    );
};

export default LoginPage;