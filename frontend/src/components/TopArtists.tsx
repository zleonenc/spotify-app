import { useEffect, useState } from 'react';
import { Box, Typography, CircularProgress, Alert } from '@mui/material';
import { useAuth } from '../context/AuthContext';
import apiClient from '../services/axios';

interface Artist {
    id: string;
    name: string;
    popularity: number;
    followers: {
        total: number;
    };
    genres: string[];
}

interface SpotifyArtistsResponse {
    items: Artist[];
    total: number;
    limit: number;
    offset: number;
}

const TopArtists = () => {
    const { accessToken, logout } = useAuth();
    const [artists, setArtists] = useState<Artist[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchTopArtists = async () => {
            if (!accessToken) {
                setError('No access token available');
                setLoading(false);
                return;
            }

            try {
                setLoading(true);
                setError(null);

                const response = await apiClient.get('/api/me/top/artists', {
                    headers: {
                        'Authorization': `Bearer ${accessToken}`
                    }
                });

                const data: SpotifyArtistsResponse = response.data;
                setArtists(data.items || []);
            } catch (err: any) {
                console.error('Error fetching top artists:', err);
                
                if (err.response?.status === 401) {
                    logout();
                    setError('Session expired. Please log in again.');
                } else {
                    setError('Failed to load top artists. Please try again.');
                }
            } finally {
                setLoading(false);
            }
        };

        fetchTopArtists();
    }, [accessToken, logout]);

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
