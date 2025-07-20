import { useNavigate } from 'react-router-dom';

import { TableRow, TableCell, Avatar, Tooltip, Typography, Box } from '@mui/material';

import type { Track } from '../../types';

interface TrackRowWithPopularityProps {
    track: Track;
    index: number;
}

const TrackRowWithPopularity = ({ track, index }: TrackRowWithPopularityProps) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/track/${track.id}`);
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
                        borderRadius: 1,
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

export default TrackRowWithPopularity;
