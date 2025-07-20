import { Container } from '@mui/material';
import { useParams } from 'react-router-dom';

import ArtistCardBig from "../components/Artist/ArtistCardBig";
import ArtistTopTracksTable from "../components/Artist/ArtistTopTracksTable";
import ArtistTopAlbums from "../components/Artist/ArtistTopAlbums";
import { ArtistProvider } from '../context';

const ArtistPage = () => {
    const { id } = useParams<{ id: string }>();

    if (!id) {
        return (
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <div>Artist ID not found</div>
            </Container>
        );
    }

    return (
        <ArtistProvider>
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <ArtistCardBig artistId={id} />
                <ArtistTopTracksTable artistId={id} />
                <ArtistTopAlbums artistId={id} />
            </Container>
        </ArtistProvider>
    );
}

export default ArtistPage;