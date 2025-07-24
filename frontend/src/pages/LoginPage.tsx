import {
    useEffect,
    useState
} from 'react';

import {
    Button,
    Container,
    Typography,
    Box,
    Alert,
    Card,
    CardContent,
    Stack
} from '@mui/material';

import { useAuth } from '../context';

import { AppLogo } from '../components/TopBar';
import spotifyIcon from '../assets/Primary_Logo_White_RGB.svg';

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
        <Box sx={{
            minHeight: '100vh',
            display: 'flex',
            flexDirection: 'column',
            backgroundColor: '#f5f5f5'
        }}>
            <Box sx={{ position: 'absolute', top: 16, left: 16 }}>
                <AppLogo />
            </Box>

            <Container
                maxWidth="sm"
                sx={{
                    flex: 1,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    py: 4
                }}
            >
                <Box sx={{ width: '100%', maxWidth: 400 }}>
                    {showLogoutMessage && (
                        <Alert
                            severity="success"
                            sx={{ mb: 3 }}
                            onClose={() => setShowLogoutMessage(false)}
                        >
                            You have been successfully logged out.
                        </Alert>
                    )}

                    <Card
                        elevation={8}
                        sx={{
                            borderRadius: 3,
                            overflow: 'visible',
                            background: 'linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)'
                        }}
                    >
                        <CardContent sx={{ p: 4 }}>
                            <Stack spacing={3} alignItems="center">
                                <Box sx={{ textAlign: 'center' }}>
                                    <Typography
                                        variant="h4"
                                        component="h1"
                                        gutterBottom
                                        sx={{
                                            fontWeight: 600,
                                            color: '#2e3440',
                                            mb: 1
                                        }}
                                    >
                                        Welcome to Spark Music
                                    </Typography>
                                    <Typography
                                        variant="body1"
                                        sx={{
                                            color: '#6c757d',
                                            fontSize: '1.1rem'
                                        }}
                                    >
                                        Please log in to continue.
                                    </Typography>
                                </Box>

                                <Button
                                    variant="contained"
                                    size="large"
                                    onClick={handleLogin}
                                    startIcon={
                                        <img
                                            src={spotifyIcon}
                                            alt="Spotify"
                                            style={{ width: 24, height: 24 }}
                                        />
                                    }
                                    sx={{
                                        backgroundColor: '#1DB954',
                                        color: 'white',
                                        fontWeight: 600,
                                        fontSize: '1.1rem',
                                        px: 4,
                                        py: 1.5,
                                        borderRadius: 50,
                                        textTransform: 'none',
                                        boxShadow: '0 4px 20px rgba(29, 185, 84, 0.3)',
                                        '&:hover': {
                                            backgroundColor: '#1ed760',
                                            boxShadow: '0 6px 25px rgba(29, 185, 84, 0.4)',
                                            transform: 'translateY(-2px)'
                                        },
                                        transition: 'all 0.3s ease-in-out',
                                    }}
                                >
                                    Sign in with Spotify
                                </Button>
                            </Stack>
                        </CardContent>
                    </Card>
                </Box>
            </Container>
        </Box>
    );
};

export default LoginPage;