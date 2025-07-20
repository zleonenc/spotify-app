import { useState, useEffect } from 'react';

import { Box, Typography, CircularProgress, Alert, Button } from '@mui/material';
import Grid from '@mui/material/Grid';

import { useArtist } from '../../context';

import AlbumCardSmall from '../Album/AlbumCardSmall';

interface ArtistTopAlbumsProps {
    artistId: string;
}

const ArtistTopAlbums = ({ artistId }: ArtistTopAlbumsProps) => {
    const { artistAlbums, artistAlbumsLoading, artistAlbumsError, fetchArtistAlbums } = useArtist();
    const [showAllAlbums, setShowAllAlbums] = useState(false);
    const maxAlbumsToShow = 5;

    useEffect(() => {
        if (artistId) {
            fetchArtistAlbums(artistId);
        }
    }, [artistId, fetchArtistAlbums]);

    useEffect(() => {
        setShowAllAlbums(false);
    }, [artistId]);

    if (artistAlbumsLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (artistAlbumsError) {
        return (
            <Box sx={{ mt: 4 }}>
                <Alert severity="error">{artistAlbumsError}</Alert>
            </Box>
        );
    }

    const albums = artistAlbums?.items || [];
    const hasMoreThan10Albums = albums.length > maxAlbumsToShow;
    const albumsToShow = showAllAlbums ? albums : albums.slice(0, maxAlbumsToShow);

    const handleLoadAllAlbums = () => {
        setShowAllAlbums(true);
    };

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h5" component="h2" gutterBottom>
                Discography
            </Typography>

            {albums.length === 0 ? (
                <Box>
                    <Typography variant="body1" color="text.secondary">
                        No albums found for this artist
                    </Typography>
                </Box>
            ) : (
                <>
                    <Grid container spacing={2} sx={{ mt: 1 }}>
                        {albumsToShow.map((album) => (
                            <Grid
                                size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}
                                key={album.id}
                            >
                                <AlbumCardSmall album={album} />
                            </Grid>
                        ))}
                    </Grid>

                    {hasMoreThan10Albums && !showAllAlbums && (
                        <Box sx={{ mt: 3, display: 'flex', justifyContent: 'center' }}>
                            <Button
                                variant="outlined"
                                onClick={handleLoadAllAlbums}
                                sx={{
                                    px: 4,
                                    py: 1.5,
                                    borderRadius: 2,
                                    textTransform: 'none',
                                    fontSize: '0.95rem',
                                    fontWeight: 500,
                                }}
                            >
                                Load all albums ({albums.length})
                            </Button>
                        </Box>
                    )}
                </>
            )}
        </Box>
    );
};

export default ArtistTopAlbums;