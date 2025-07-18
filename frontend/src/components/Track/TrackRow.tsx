import { useNavigate } from 'react-router-dom';

import { Box, Typography, Avatar, Tooltip } from '@mui/material';

import type { Track } from '../../types';

interface TrackRowProps {
    track: Track;
    index: number;
}

const TrackRow = ({ track, index }: TrackRowProps) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/track/${track.id}`);
    };

    const trackImage = track.album?.images && track.album.images.length > 0
        ? track.album.images[0].url
        : '';

    const artistNames = track.artists?.map(artist => artist.name).join(', ') || 'Unknown Artist';

    const formatDuration = (durationMs: number) => {
        const minutes = Math.floor(durationMs / 60000);
        const seconds = Math.floor((durationMs % 60000) / 1000);
        return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    };

    return (
        <Box
            onClick={handleClick}
            sx={{
                display: 'flex',
                alignItems: 'center',
                py: 1.5,
                px: 2,
                cursor: 'pointer',
                borderRadius: 1,
                transition: 'all 0.2s ease-in-out',
                '&:hover': {
                    bgcolor: 'action.hover',
                },
            }}
        >
            {/* Number */}
            <Typography
                variant="body2"
                sx={{
                    fontWeight: 500,
                    color: 'text.secondary',
                    minWidth: 32,
                    textAlign: 'center',
                    mr: 2,
                }}
            >
                {index}
            </Typography>

            <Box sx={{ display: 'flex', alignItems: 'center', flex: 1, minWidth: 0, mr: 2 }}>
                <Avatar
                    src={trackImage}
                    alt={track.name}
                    sx={{
                        width: 40,
                        height: 40,
                        borderRadius: 1,
                        mr: 2,
                    }}
                    variant="square"
                >
                    {track.name.charAt(0).toUpperCase()}
                </Avatar>
                <Box sx={{ minWidth: 0, flex: 1 }}>
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
                    <Typography
                        variant="body2"
                        color="text.secondary"
                        sx={{
                            overflow: 'hidden',
                            textOverflow: 'ellipsis',
                            whiteSpace: 'nowrap',
                        }}
                    >
                        {artistNames}
                    </Typography>
                </Box>
            </Box>

            {/* Duration */}
            <Typography
                variant="body2"
                color="text.secondary"
                sx={{
                    fontFamily: 'monospace',
                    fontSize: '0.875rem',
                    minWidth: 48,
                    textAlign: 'right',
                }}
            >
                {formatDuration(track.duration_ms)}
            </Typography>
        </Box>
    );
};

export default TrackRow;
