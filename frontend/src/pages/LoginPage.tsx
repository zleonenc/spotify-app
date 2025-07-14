import { Button, Container, Typography, Box } from '@mui/material';

const LoginPage = () => {
    const handleLogin = async () => {
        try {
            window.location.href = 'http://localhost:8080/auth/spotify';
        } catch (error) {
            console.error('Error during login:', error);
        }
    };

    return (
        <Container maxWidth="sm" style={{ textAlign: 'center', marginTop: '50px' }}>
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