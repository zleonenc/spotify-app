import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import {
    Box,
    Typography,
    Avatar,
    Menu,
    MenuItem,
    ListItemIcon,
    ListItemText,
    Divider
} from '@mui/material';

import {
    AccountCircle as ProfileIcon,
    Logout as LogoutIcon
} from '@mui/icons-material';

import { useAuth, useProfile } from '../../context';

const UserProfile = () => {
    const navigate = useNavigate();
    const { logout } = useAuth();
    const { profile } = useProfile();
    const [profileMenuAnchor, setProfileMenuAnchor] = useState<null | HTMLElement>(null);

    const handleProfileClick = (event: React.MouseEvent<HTMLElement>) => {
        setProfileMenuAnchor(event.currentTarget);
    };

    const handleProfileMenuClose = () => {
        setProfileMenuAnchor(null);
    };

    const handleProfileInfo = () => {
        handleProfileMenuClose();
        navigate('/profile');
    };

    const handleLogout = async () => {
        handleProfileMenuClose();
        try {
            await logout();
            navigate('/login');
        } catch (error) {
            console.error('Logout failed:', error);
        }
    };

    return (
        <>
            <Box
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: 1,
                    cursor: 'pointer',
                    px: 1,
                    py: 0.5,
                    borderRadius: 2,
                    flex: '0 0 auto',
                    '&:hover': {
                        bgcolor: 'action.hover'
                    }
                }}
                onClick={handleProfileClick}
            >
                <Avatar
                    src={profile?.images?.[0]?.url}
                    alt={profile?.display_name || 'User'}
                    sx={{ width: 32, height: 32 }}
                >
                    {profile?.display_name?.charAt(0).toUpperCase() || 'U'}
                </Avatar>
                <Typography
                    variant="body2"
                    sx={{
                        fontWeight: 500,
                        display: { xs: 'none', sm: 'block' }
                    }}
                >
                    {profile?.display_name || 'User'}
                </Typography>
            </Box>

            <Menu
                anchorEl={profileMenuAnchor}
                open={Boolean(profileMenuAnchor)}
                onClose={handleProfileMenuClose}
                slotProps={{
                    paper: {
                        sx: {
                            mt: 1,
                            minWidth: 200,
                            borderRadius: 2,
                            boxShadow: 3
                        }
                    }
                }}
                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
            >
                <MenuItem onClick={handleProfileInfo}>
                    <ListItemIcon>
                        <ProfileIcon fontSize="small" />
                    </ListItemIcon>
                    <ListItemText>Profile Info</ListItemText>
                </MenuItem>
                <Divider />
                <MenuItem onClick={handleLogout}>
                    <ListItemIcon>
                        <LogoutIcon fontSize="small" />
                    </ListItemIcon>
                    <ListItemText>Logout</ListItemText>
                </MenuItem>
            </Menu>
        </>
    );
};

export default UserProfile;
