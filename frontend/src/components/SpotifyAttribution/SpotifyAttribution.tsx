import {
    Box,
    Typography
} from '@mui/material';

import SpotifyLogo from '../../assets/Full_Logo_Green_RGB.svg';

const SpotifyAttribution = () => {
    const handleSpotifyClick = () => {
        window.open('https://open.spotify.com', '_blank');
    };

    return (
        <Box sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: 1,
            py: 2,
            px: 2,
            borderTop: '2px solid',
            borderColor: 'divider',
            backgroundColor: 'background.paper',
            opacity: 0.8,
            mt: 'auto'
        }}>
            <Typography variant="caption" color="text.secondary">
                Content provided by
            </Typography>
            <Box
                component="img"
                src={SpotifyLogo}
                alt="Spotify"
                onClick={handleSpotifyClick}
                sx={{
                    height: 21,
                    cursor: 'pointer',
                    transition: 'opacity 0.2s ease',
                    '&:hover': {
                        opacity: 0.8,
                    },
                }}
            />
        </Box>
    );
};

export default SpotifyAttribution;
