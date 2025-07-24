import { useNavigate } from 'react-router-dom';

import {
    TableRow,
    TableCell,
    Avatar,
    Tooltip,
    Typography,
    Box,
    Chip
} from '@mui/material';

import AlbumIcon from '@mui/icons-material/Album';

import { usePlayer } from '../../context';
import type { Track } from '../../types';

interface ArtistTrackRowProps {
    track: Track;
    index: number;
}

const ArtistTrackRow = ({ track, index }: ArtistTrackRowProps) => {
    const navigate = useNavigate();
    const { playTrack } = usePlayer();

    const handleClick = () => {
        playTrack(track.id);
    };

    const handleAlbumClick = (e: React.MouseEvent, albumId: string) => {
        e.stopPropagation(); // Prevent row click
        navigate(`/album/${albumId}`);
    };

    const trackImage = track.album?.images && track.album.images.length > 0
        ? track.album.images[0].url
        : '';

    const formatDuration = (durationMs: number) => {
        const minutes = Math.floor(durationMs / 60000);
        const seconds = Math.floor((durationMs % 60000) / 1000);
        return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    };

    const formatPopularity = (popularity: number) => {
        return `${popularity}/100`;
    };

    const getTrackYear = () => {
        if (track.album?.release_date) {
            return new Date(track.album.release_date).getFullYear();
        }
        return null;
    };

    return (
        <TableRow
            onClick={handleClick}
            sx={{
                cursor: 'pointer',
                transition: 'all 0.2s ease-in-out',
                '&:hover': {
                    bgcolor: 'action.hover',
                },
            }}
        >
            {/* Number */}
            <TableCell align="center">
                <Typography
                    variant="body2"
                    sx={{
                        fontWeight: 500,
                        color: 'text.secondary',
                    }}
                >
                    {index}
                </Typography>
            </TableCell>

            {/* Track Image */}
            <TableCell>
                <Avatar
                    src={trackImage}
                    alt={track.name}
                    sx={{
                        width: 40,
                        height: 40,
                        borderRadius: 2, // Required by Spotify
                    }}
                    variant="square"
                >
                    {track.name.charAt(0).toUpperCase()}
                </Avatar>
            </TableCell>

            {/* Song Name */}
            <TableCell>
                <Box sx={{ minWidth: 0 }}>
                    <Typography
                        variant="body1"
                        sx={{
                            fontWeight: 500,
                            overflow: 'hidden',
                            textOverflow: 'ellipsis',
                            whiteSpace: 'nowrap',
                            color: 'text.primary',
                        }}
                    >
                        {track.name}
                        {track.explicit && (
                            <Tooltip title="Explicit lyrics" arrow>
                                <Typography
                                    component="span"
                                    sx={{
                                        ml: 1,
                                        fontSize: '0.75rem',
                                        bgcolor: 'grey.300',
                                        color: 'grey.700',
                                        px: 0.5,
                                        py: 0.25,
                                        borderRadius: 0.5,
                                        fontWeight: 500,
                                        cursor: 'help',
                                    }}
                                >
                                    E
                                </Typography>
                            </Tooltip>
                        )}
                    </Typography>
                </Box>
            </TableCell>

            {/* Album */}
            <TableCell>
                {track.album && (
                    <Chip
                        label={track.album.name}
                        variant="outlined"
                        icon={<AlbumIcon />}
                        onClick={(e) => handleAlbumClick(e, track.album.id)}
                        sx={{
                            borderRadius: 2,
                            cursor: 'pointer',
                            maxWidth: '200px',
                            '& .MuiChip-label': {
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                                whiteSpace: 'nowrap',
                            },
                            '&:hover': {
                                backgroundColor: 'action.hover',
                            },
                        }}
                    />
                )}
            </TableCell>

            {/* Year */}
            <TableCell align="center">
                <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{
                        fontFamily: 'monospace',
                        fontSize: '0.875rem',
                    }}
                >
                    {getTrackYear() || '-'}
                </Typography>
            </TableCell>

            {/* Popularity */}
            <TableCell align="center">
                <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{
                        fontFamily: 'monospace',
                        fontSize: '0.875rem',
                    }}
                >
                    {formatPopularity(track.popularity)}
                </Typography>
            </TableCell>

            {/* Duration */}
            <TableCell align="right">
                <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{
                        fontFamily: 'monospace',
                        fontSize: '0.875rem',
                    }}
                >
                    {formatDuration(track.duration_ms)}
                </Typography>
            </TableCell>
        </TableRow>
    );
};

export default ArtistTrackRow;
