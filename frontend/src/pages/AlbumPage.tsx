import { useParams } from 'react-router-dom';

import { Container } from '@mui/material';

import { AlbumProvider } from '../context';

import AlbumCardBig from "../components/Album/AlbumCardBig";
import AlbumTracksTable from "../components/Album/AlbumTracksTable";


const AlbumPage = () => {
    const { id } = useParams<{ id: string }>();

    if (!id) {
        return (
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <div>Album ID not found</div>
            </Container>
        );
    }


    return (<AlbumProvider>
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <AlbumCardBig albumId={id} />
            <AlbumTracksTable albumId={id} />
        </Container>
    </AlbumProvider>
    );
}

export default AlbumPage;