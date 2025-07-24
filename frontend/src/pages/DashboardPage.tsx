import { Container } from '@mui/material';

import DashboardTopArtists from '../components/Dashboard/DashboardTopArtists';
import DashboardTopTracksTable from '../components/Dashboard/DashboardTopTracksTable';

const DashboardPage = () => {
    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <DashboardTopArtists />
            <DashboardTopTracksTable />
        </Container>
    );
};

export default DashboardPage;
