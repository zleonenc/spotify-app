import { Box, IconButton } from '@mui/material';
import { Home as HomeIcon } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import SearchBar from '../Search/Bar/SearchBar';

const SearchSection = () => {
    const navigate = useNavigate();

    return (
        <Box sx={{
            display: 'flex',
            alignItems: 'center',
            gap: 2,
            flex: '1 1 auto',
            justifyContent: 'center',
            maxWidth: 800
        }}>
            <IconButton
                onClick={() => navigate('/')}
                sx={{
                    borderRadius: 2,
                    px: 2
                }}
            >
                <HomeIcon />
            </IconButton>

            <Box sx={{ flex: 1, maxWidth: 600 }}>
                <SearchBar />
            </Box>
        </Box>
    );
};

export default SearchSection;
