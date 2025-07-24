import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import {
    Box,
    Typography,
    Avatar,
    Chip,
    CircularProgress,
    Alert
} from '@mui/material';

import AlbumIcon from '@mui/icons-material/Album';
import PersonIcon from '@mui/icons-material/Person';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import MusicNoteIcon from '@mui/icons-material/MusicNote';
import AccessTimeIcon from '@mui/icons-material/AccessTime';

import { useAlbum } from '../../context';

interface AlbumCardBigProps {
    albumId: string;
}

const AlbumCardBig = ({ albumId }: AlbumCardBigProps) => {
    const { album, albumLoading, albumError, fetchAlbum } = useAlbum();
    const navigate = useNavigate();

    const handleArtistClick = (artistId: string) => {
        navigate(`/artist/${artistId}`);
    };

    useEffect(() => {
        if (albumId) {
            fetchAlbum(albumId);
        }
    }, [albumId, fetchAlbum]);

    if (albumLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
                <CircularProgress size={60} />
            </Box>
        );
    }

    if (albumError) {
        return (
            <Box sx={{ py: 4 }}>
                <Alert severity="error">{albumError}</Alert>
            </Box>
        );
    }

    if (!album) {
        return (
            <Box sx={{ py: 4 }}>
                <Alert severity="info">Album not found</Alert>
            </Box>
        );
    }

    const albumImage = album.images && album.images.length > 0
        ? album.images[0].url
        : '';

    const releaseYear = new Date(album.release_date).getFullYear();

    const formatDuration = (totalMs: number): string => {
        const totalMinutes = Math.floor(totalMs / 60000);
        const hours = Math.floor(totalMinutes / 60);
        const minutes = totalMinutes % 60;

        if (hours > 0) {
            return `${hours}h ${minutes}m`;
        }
        return `${minutes}m`;
    };

    const getTotalDuration = (): number => {
        if (!album.tracks?.items) return 0;
        return album.tracks.items.reduce((total, track) => total + (track.duration_ms || 0), 0);
    };

    const totalDuration = getTotalDuration();

    return (
        <Box sx={{
            py: 4,
            px: 2,
            display: 'flex',
            flexDirection: { xs: 'column', md: 'row' },
            gap: 4,
            alignItems: { xs: 'center', md: 'flex-start' },
            textAlign: { xs: 'center', md: 'left' }
        }}>
            {/* Image */}
            <Avatar
                src={albumImage}
                alt={album.name}
                sx={{
                    width: { xs: 200, md: 250 },
                    height: { xs: 200, md: 250 },
                    borderRadius: {xs: 2, md: 4 }, // Required by Spotify
                    boxShadow: 3,
                    flexShrink: 0,
                }}
            >
                <AlbumIcon sx={{ fontSize: '4rem', color: 'text.secondary' }} />
            </Avatar>

            {/* Info */}
            <Box sx={{ flex: 1, minWidth: 0 }}>
                {/* Type */}
                <Typography
                    variant="body2"
                    sx={{
                        fontWeight: 600,
                        color: 'text.secondary',
                        mb: 1,
                        textTransform: 'uppercase',
                        letterSpacing: 1,
                    }}
                >
                    Album
                </Typography>

                <Typography
                    variant="h2"
                    component="h1"
                    sx={{
                        fontWeight: 'bold',
                        mb: 2,
                        fontSize: { xs: '2.5rem', md: '3.5rem' },
                        lineHeight: 1.1,
                    }}
                >
                    {album.name}
                </Typography>

                {/* Details */}
                <Box sx={{
                    display: 'flex',
                    flexDirection: { xs: 'column', sm: 'row' },
                    gap: 2,
                    mb: 3,
                    alignItems: { xs: 'center', sm: 'flex-start' },
                }}>
                    <Box sx={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: 1,
                    }}>
                        <CalendarTodayIcon color="action" fontSize="small" />
                        <Typography
                            variant="body1"
                            color="text.secondary"
                            sx={{ fontWeight: 500 }}
                        >
                            {releaseYear}
                        </Typography>
                    </Box>

                    <Box sx={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: 1,
                    }}>
                        <MusicNoteIcon color="action" fontSize="small" />
                        <Typography
                            variant="body1"
                            color="text.secondary"
                            sx={{ fontWeight: 500 }}
                        >
                            {album.total_tracks} {album.total_tracks === 1 ? 'song' : 'songs'}
                        </Typography>
                    </Box>

                    {totalDuration > 0 && (
                        <Box sx={{
                            display: 'flex',
                            alignItems: 'center',
                            gap: 1,
                        }}>
                            <AccessTimeIcon color="action" fontSize="small" />
                            <Typography
                                variant="body1"
                                color="text.secondary"
                                sx={{ fontWeight: 500 }}
                            >
                                {formatDuration(totalDuration)}
                            </Typography>
                        </Box>
                    )}
                </Box>

                {/* Artists */}
                {album.artists && album.artists.length > 0 && (
                    <Box sx={{ mb: 2 }}>
                        <Typography
                            variant="subtitle1"
                            sx={{
                                mb: 1.5,
                                fontWeight: 600,
                                color: 'text.secondary'
                            }}
                        >
                            {album.artists.length === 1 ? 'Artist' : 'Artists'}
                        </Typography>
                        <Box sx={{
                            display: 'flex',
                            flexWrap: 'wrap',
                            gap: 1,
                            justifyContent: { xs: 'center', md: 'flex-start' }
                        }}>
                            {album.artists.map((artist, index) => (
                                <Chip
                                    icon={<PersonIcon />}
                                    key={artist.id || index}
                                    label={artist.name}
                                    variant="outlined"
                                    onClick={() => handleArtistClick(artist.id)}
                                    sx={{
                                        borderRadius: 2,
                                        cursor: 'pointer',
                                        '&:hover': {
                                            backgroundColor: 'action.hover',
                                        },
                                    }}
                                />
                            ))}
                        </Box>
                    </Box>
                )}

                {/* Type & info */}
                <Box sx={{
                    display: 'flex',
                    flexDirection: { xs: 'column', sm: 'row' },
                    gap: 2,
                    alignItems: { xs: 'center', sm: 'flex-start' },
                }}>
                    {album.popularity !== undefined && (
                        <Box sx={{
                            display: 'flex',
                            alignItems: 'center',
                            gap: 1,
                        }}>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                                sx={{ fontWeight: 500 }}
                            >
                                Popularity: {album.popularity}/100
                            </Typography>
                            <Box
                                sx={{
                                    width: 100,
                                    height: 6,
                                    backgroundColor: 'grey.300',
                                    borderRadius: 3,
                                    overflow: 'hidden',
                                }}
                            >
                                <Box
                                    sx={{
                                        width: `${album.popularity}%`,
                                        height: '100%',
                                        backgroundColor: 'primary.main',
                                        borderRadius: 3,
                                    }}
                                />
                            </Box>
                        </Box>
                    )}
                </Box>
            </Box>
        </Box>
    );
};

export default AlbumCardBig;
