import { Box, Button, Container, Typography } from '@mui/material';
import { useAuth } from '../context';
import TopArtists from '../components/Profile/TopArtists';
import Profile from '../components/Profile/Profile';

const Dashboard = () => {
    const { logout } = useAuth();

    const handleLogout = () => {
        logout();
    };

    return (
        <Container maxWidth="lg" sx={{ mt: 4 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
                <Typography variant="h4" component="h1">
                    Spotify Dashboard
                </Typography>
                <Button
                    variant="outlined"
                    color="secondary"
                    onClick={handleLogout}
                >
                    Logout
                </Button>
            </Box>

            <Box sx={{ mt: 4 }}>
                <Profile />
            </Box>

            <Box sx={{ mt: 4 }}>
                <TopArtists />
            </Box>
        </Container>
    );
};

export default Dashboard;
