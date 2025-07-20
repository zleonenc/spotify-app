import { useEffect } from 'react';

import {
    Box,
    Typography,
    CircularProgress,
    Alert
} from '@mui/material';
import Grid from '@mui/material/Grid';

import { useProfile } from '../../context';

import ArtistCardSmall from '../Artist/ArtistCardSmall';

const DashboardTopArtists = () => {
    const { topArtists, topArtistsLoading, topArtistsError, fetchTopArtists } = useProfile();

    useEffect(() => {
        fetchTopArtists();
    }, [fetchTopArtists]);

    if (topArtistsLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (topArtistsError) {
        return (
            <Box sx={{ mt: 4 }}>
                <Alert severity="error">{topArtistsError}</Alert>
            </Box>
        );
    }

    const artists = topArtists || [];

    const limitedArtists = artists.slice(0, 10);

    return (
        <Box sx={{
            mt: 4,
        }}>
            <Typography variant="h5" component="h2" gutterBottom>
                My top artists
            </Typography>

            {artists.length === 0 ? (
                <Box sx={{

                }}>
                    <Typography variant="body1" color="text.secondary">
                        No top artists found
                    </Typography>
                </Box>
            ) : (
                <Grid container spacing={2} sx={{ mt: 1 }}>
                    {limitedArtists.map((artist) => (
                        <Grid
                            size={{ xs: 12, sm: 6, md: 4, lg: 2.4 }}
                            key={artist.id}>
                            <ArtistCardSmall artist={artist} />
                        </Grid>
                    ))}
                </Grid>
            )}
        </Box>
    );
};

export default DashboardTopArtists;