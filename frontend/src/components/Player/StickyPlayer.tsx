import React from 'react';

import {
    Box,
    IconButton
} from '@mui/material';

import CloseIcon from '@mui/icons-material/Close';

import { usePlayer } from '../../context';

const StickyPlayer: React.FC = () => {
    const { currentTrackId, isPlayerVisible, hidePlayer } = usePlayer();

    if (!isPlayerVisible || !currentTrackId) {
        return null;
    }

    return (
        <Box
            sx={{
                position: 'fixed',
                bottom: -24,
                left: 0,
                right: 0,
                zIndex: 1300,
                borderTop: 1,
                borderColor: 'divider',
                backgroundColor: 'background.paper',
                boxShadow: '0 -2px 10px rgba(0,0,0,0.1)',
                display: 'flex',
                alignItems: 'center',
                px: 2,
                py: 1,
            }}
        >
            <Box sx={{ flex: 1 }}>
                <iframe
                    style={{
                        borderRadius: '12px',
                        border: 'none'
                    }}
                    src={`https://open.spotify.com/embed/track/${currentTrackId}?utm_source=generator&theme=0`}
                    width="100%"
                    height="100"
                    allow="autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture"
                    loading="lazy"
                    title={`Spotify Player for track ${currentTrackId}`}
                />
            </Box>
            <IconButton
                onClick={hidePlayer}
                sx={{
                    ml: 2,
                    color: 'text.secondary',
                    verticalAlign: 'middle',
                    '&:hover': {
                        backgroundColor: 'action.hover',
                    },
                }}
                aria-label="Close player"
            >
                <CloseIcon />
            </IconButton>
        </Box>
    );
};

export default StickyPlayer;
