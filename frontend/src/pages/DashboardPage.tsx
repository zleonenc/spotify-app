import { Container } from '@mui/material';
import TopArtists from '../components/Profile/TopArtists';
import Profile from '../components/Profile/Profile';

const Dashboard = () => {
    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Profile />
            <TopArtists />
        </Container>
    );
};

export default Dashboard;
