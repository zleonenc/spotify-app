import { useNavigate } from 'react-router-dom';

import { Card, CardContent, Avatar, Typography, Box } from '@mui/material';

import type { Artist } from '../../types';

interface ArtistCardSmallProps {
    artist: Artist;
}

const ArtistCardSmall = ({ artist }: ArtistCardSmallProps) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/artist/${artist.id}`);
    };

    const artistImage = artist.images && artist.images.length > 0
        ? artist.images[0].url
        : '';

    const artistGenres = artist.genres && artist.genres.length > 0
        ? artist.genres.slice(0, 3).join(', ')
        : 'No genres available';

    return (
        <Card
            onClick={handleClick}
            sx={{
                cursor: 'pointer',
                transition: 'all 0.2s ease-in-out',
                '&:hover': {
                    transform: 'translateY(-2px)',
                    boxShadow: 3,
                },
                width: '100%',
                maxWidth: 280,
            }}
        >
            <CardContent sx={{ p: 2 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    <Avatar
                        src={artistImage}
                        alt={artist.name}
                        sx={{
                            width: 60,
                            height: 60,
                            borderRadius: 1,
                        }}
                    >
                        {artist.name.charAt(0).toUpperCase()}
                    </Avatar>
                    <Box sx={{ flex: 1, minWidth: 0 }}>
                        <Typography
                            variant="subtitle1"
                            component="h3"
                            sx={{
                                fontWeight: 600,
                                mb: 0.5,
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                                whiteSpace: 'nowrap',
                            }}
                        >
                            {artist.name}
                        </Typography>
                        <Typography
                            variant="body2"
                            color="text.secondary"
                            sx={{
                                fontStyle: 'italic',
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                                whiteSpace: 'nowrap',
                            }}
                        >
                            {artistGenres.charAt(0).toUpperCase() + artistGenres.slice(1)}
                        </Typography>
                    </Box>
                </Box>
            </CardContent>
        </Card>
    );
};

export default ArtistCardSmall;