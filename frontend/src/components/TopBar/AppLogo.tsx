import { useNavigate } from 'react-router-dom';

import {
    Box,
    Typography
} from '@mui/material';

import logoImage from '../../assets/encora-logo.webp';

const AppLogo = () => {
    const navigate = useNavigate();

    return (
        <Box
            sx={{
                display: 'flex',
                alignItems: 'center',
                gap: 1,
                cursor: 'pointer',
                flex: '0 0 auto'
            }}
            onClick={() => navigate('/')}
        >
            <img
                src={logoImage}
                alt="Logo"
                style={{
                    height: '32px',
                    width: 'auto',
                    objectFit: 'contain'
                }}
            />
            <Typography
                variant="h6"
                sx={{
                    fontWeight: 700,
                    color: '#684697'
                }}
            >
                Spark Music
            </Typography>
        </Box>
    );
};

export default AppLogo;
