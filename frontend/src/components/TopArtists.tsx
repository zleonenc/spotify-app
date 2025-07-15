import { Box, Typography, CircularProgress, Alert } from '@mui/material';
import { useSpotifyTopArtists } from '../context';

const TopArtists = () => {
    const { artists, loading, error } = useSpotifyTopArtists();

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box sx={{ mt: 4 }}>
                <Alert severity="error">{error}</Alert>
            </Box>
        );
    }

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h5" component="h2" gutterBottom>
                Your Top Artists
            </Typography>
            {artists.length === 0 ? (
                <Typography variant="body1" color="text.secondary">
                    No top artists found.
                </Typography>
            ) : (
                <Box sx={{ mt: 2 }}>
                    {artists.map((artist, index) => (
                        <Box key={artist.id} sx={{ mb: 1 }}>
                            <Typography variant="body1">
                                {index + 1}. {`Artist{id='${artist.id}', name='${artist.name}', popularity=${artist.popularity}, followers=${artist.followers?.total || 0}, genres=[${artist.genres?.join(', ') || ''}]}`}
                            </Typography>
                        </Box>
                    ))}
                </Box>
            )}
        </Box>
    );
};

export default TopArtists;
