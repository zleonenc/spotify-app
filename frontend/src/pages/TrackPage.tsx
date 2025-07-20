import { useParams } from 'react-router-dom';

import { Container } from '@mui/material';

import { TrackProvider } from '../context';

import TrackCardBig from "../components/Track/TrackCardBig";


const TrackPage = () => {
    const { id } = useParams<{ id: string }>();

    if (!id) {
        return (
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <div>Track ID not found</div>
            </Container>
        );
    }

    return (
        <TrackProvider>
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <TrackCardBig trackId={id} />
            </Container>
        </TrackProvider>
    );
}

export default TrackPage;
