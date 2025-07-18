import { Container } from '@mui/material';

import DashboardTopArtists from '../components/Dashboard/DashboardTopArtists';
import DashboardTopTracks from '../components/Dashboard/DashboardTopTracks';

const DashboardPage = () => {
    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <DashboardTopArtists />
            <DashboardTopTracks />
        </Container>
    );
};

export default DashboardPage;
