import { useNavigate } from 'react-router-dom';

import { Card, CardContent, Avatar, Typography, Box } from '@mui/material';

import type { Album } from '../../types';

interface AlbumCardSmallProps {
    album: Album;
}

const AlbumCardSmall = ({ album }: AlbumCardSmallProps) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/album/${album.id}`);
    };

    const albumImage = album.images && album.images.length > 0
        ? album.images[0].url
        : '';

    const albumArtists = album.artists && album.artists.length > 0
        ? album.artists.map(artist => artist.name).join(', ')
        : 'Unknown Artist';

    const releaseYear = album.release_date
        ? new Date(album.release_date).getFullYear()
        : 'Unknown Year';

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
                        src={albumImage}
                        alt={album.name}
                        sx={{
                            width: 60,
                            height: 60,
                            borderRadius: 1,
                        }}
                    >
                        {album.name.charAt(0).toUpperCase()}
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
                            {album.name}
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
                            {releaseYear}
                        </Typography>
                        <Typography
                            variant="body2"
                            color="text.secondary"
                            sx={{
                                mb: 0.5,
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                                whiteSpace: 'nowrap',
                            }}
                        >
                            {albumArtists}
                        </Typography>
                    </Box>
                </Box>
            </CardContent>
        </Card>
    );
};

export default AlbumCardSmall;