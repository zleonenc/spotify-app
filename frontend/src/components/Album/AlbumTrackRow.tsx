import { useNavigate } from 'react-router-dom';

import {
    TableRow,
    TableCell,
    Typography
} from '@mui/material';

import type { Track } from '../../types';

interface AlbumTrackRowProps {
    track: Track;
    index: number;
}

const AlbumTrackRow = ({ track, index }: AlbumTrackRowProps) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/track/${track.id}`);
    };

    const formatDuration = (durationMs: number) => {
        const minutes = Math.floor(durationMs / 60000);
        const seconds = Math.floor((durationMs % 60000) / 1000);
        return `${minutes}:${seconds.toString().padStart(2, '0')}`;
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

            {/* Song Name */}
            <TableCell>
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

export default AlbumTrackRow;
