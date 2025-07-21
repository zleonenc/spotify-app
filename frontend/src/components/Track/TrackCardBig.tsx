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

import MusicNoteIcon from '@mui/icons-material/MusicNote';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import AlbumIcon from '@mui/icons-material/Album';
import PersonIcon from '@mui/icons-material/Person';

import { useTrack } from '../../context';
import SpotifyLogo from '../../assets/Primary_Logo_Green_RGB.svg';

interface TrackCardBigProps {
    trackId: string;
}

const TrackCardBig = ({ trackId }: TrackCardBigProps) => {
    const { track, trackLoading, trackError, fetchTrack } = useTrack();
    const navigate = useNavigate();

    const handleArtistClick = (artistId: string) => {
        navigate(`/artist/${artistId}`);
    };

    const handleAlbumClick = (albumId: string) => {
        navigate(`/album/${albumId}`);
    };

    useEffect(() => {
        if (trackId) {
            fetchTrack(trackId);
        }
    }, [trackId, fetchTrack]);

    if (trackLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
                <CircularProgress size={60} />
            </Box>
        );
    }

    if (trackError) {
        return (
            <Box sx={{ py: 4 }}>
                <Alert severity="error">{trackError}</Alert>
            </Box>
        );
    }

    if (!track) {
        return (
            <Box sx={{ py: 4 }}>
                <Alert severity="info">Track not found</Alert>
            </Box>
        );
    }

    const trackImage = (track.album?.images && track.album.images.length > 0)
        ? track.album.images[0].url
        : '';

    const releaseYear = track.album?.release_date
        ? new Date(track.album.release_date).getFullYear()
        : null;

    const formatDuration = (durationMs: number): string => {
        const totalSeconds = Math.floor(durationMs / 1000);
        const minutes = Math.floor(totalSeconds / 60);
        const seconds = totalSeconds % 60;
        return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    };

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
                src={trackImage}
                alt={track.name}
                sx={{
                    width: { xs: 200, md: 250 },
                    height: { xs: 200, md: 250 },
                    borderRadius: { xs: 2, md: 4 }, // Required by Spotify
                    boxShadow: 3,
                    flexShrink: 0,
                    cursor: track.album?.id ? 'pointer' : 'default'
                }}
                onClick={() => track.album?.id && handleAlbumClick(track.album.id)}
            >
                {trackImage ? null : <MusicNoteIcon sx={{ fontSize: '4rem', color: 'text.secondary' }} />}
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
                    Song
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
                    {track.name}
                </Typography>

                {/* Details */}
                <Box sx={{
                    display: 'flex',
                    flexDirection: { xs: 'column', sm: 'row' },
                    gap: 2,
                    mb: 3,
                    alignItems: { xs: 'center', sm: 'flex-start' },
                }}>
                    {releaseYear && (
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
                    )}

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
                            {formatDuration(track.duration_ms)}
                        </Typography>
                    </Box>

                    <Box sx={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: 1,
                    }}>
                        <Typography
                            variant="body1"
                            color="text.secondary"
                            sx={{ fontWeight: 500 }}
                        >
                            Popularity: {track.popularity}/100
                        </Typography>
                        <Box
                            sx={{
                                width: 60,
                                height: 6,
                                backgroundColor: 'grey.300',
                                borderRadius: 3,
                                overflow: 'hidden',
                            }}
                        >
                            <Box
                                sx={{
                                    width: `${track.popularity}%`,
                                    height: '100%',
                                    backgroundColor: 'primary.main',
                                    borderRadius: 3,
                                }}
                            />
                        </Box>
                    </Box>
                </Box>

                <Box sx={{
                    display: 'flex',
                    flexDirection: { xs: 'column', sm: 'row' },
                    gap: { xs: 2, sm: 4 },
                    mb: 2,
                }}>
                    {/* Artists */}
                    {track.artists && track.artists.length > 0 && (
                        <Box sx={{ flex: 1 }}>
                            <Typography
                                variant="subtitle1"
                                sx={{
                                    mb: 1.5,
                                    fontWeight: 600,
                                    color: 'text.secondary'
                                }}
                            >
                                {track.artists.length === 1 ? 'Artist' : 'Artists'}
                            </Typography>
                            <Box sx={{
                                display: 'flex',
                                flexWrap: 'wrap',
                                gap: 1,
                                justifyContent: { xs: 'center', sm: 'flex-start' }
                            }}>
                                {track.artists.map((artist, index) => (
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

                    {/* Album */}
                    {track.album && (
                        <Box sx={{ flex: 1 }}>
                            <Typography
                                variant="subtitle1"
                                sx={{
                                    mb: 1.5,
                                    fontWeight: 600,
                                    color: 'text.secondary'
                                }}
                            >
                                Album
                            </Typography>
                            <Box sx={{
                                display: 'flex',
                                justifyContent: { xs: 'center', sm: 'flex-start' }
                            }}>
                                <Chip
                                    label={track.album.name}
                                    variant="outlined"
                                    icon={<AlbumIcon />}
                                    onClick={() => handleAlbumClick(track.album.id)}
                                    sx={{
                                        borderRadius: 2,
                                        cursor: 'pointer',
                                        '&:hover': {
                                            backgroundColor: 'action.hover',
                                        },
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

export default TrackCardBig;
