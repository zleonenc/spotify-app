import { AppBar, Toolbar } from '@mui/material';
import { AppLogo, SearchSection, UserProfile } from '.';

const TopBar = () => {
    return (
        <AppBar
            position="sticky"
            elevation={1}
            sx={{
                bgcolor: 'background.paper',
                color: 'text.primary',
                borderBottom: '1px solid',
                borderColor: 'divider'
            }}
        >
            <Toolbar sx={{ gap: 2, justifyContent: 'space-between' }}>
                <AppLogo />
                <SearchSection />
                <UserProfile />
            </Toolbar>
        </AppBar>
    );
};

export default TopBar;