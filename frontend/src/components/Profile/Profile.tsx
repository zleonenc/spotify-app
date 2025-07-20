import { Box, Card, CardContent, Typography, CircularProgress } from '@mui/material';

import { useProfile } from '../../context';

const Profile = () => {
    const { profile, profileLoading, profileError } = useProfile();

    if (profileLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: 200 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (profileError) {
        return (
            <Box sx={{ p: 2 }}>
                <Typography color="error">{profileError}</Typography>
            </Box>
        );
    }

    if (!profile) {
        return (
            <Box sx={{ p: 2 }}>
                <Typography>No profile data available</Typography>
            </Box>
        );
    }

    return (
        <Card sx={{ mb: 4 }}>
            <CardContent>
                <Typography variant="h5" component="h2" gutterBottom>
                    Profile Information
                </Typography>

                <Box sx={{ fontFamily: 'monospace', fontSize: '0.875rem', lineHeight: 1.6 }}>
                    <div><strong>ID:</strong> {profile.id}</div>
                    <div><strong>Display Name:</strong> {profile.display_name}</div>
                    {profile.email && <div><strong>Email:</strong> {profile.email}</div>}
                    {profile.country && <div><strong>Country:</strong> {profile.country}</div>}
                    {profile.product && <div><strong>Product:</strong> {profile.product}</div>}
                    <div><strong>Followers:</strong> {profile.followers.total}</div>
                    {profile.followers.href && <div><strong>Followers Href:</strong> {profile.followers.href}</div>}
                    <div><strong>External URLs - Spotify:</strong> {profile.external_urls.spotify}</div>

                    {profile.images && profile.images.length > 0 && (
                        <>
                            <div><strong>Images:</strong></div>
                            {profile.images.map((image, index) => (
                                <Box key={index} sx={{ ml: 2 }}>
                                    <div><strong>Image {index + 1}:</strong></div>
                                    <div style={{ marginLeft: 16 }}>
                                        <div><strong>URL:</strong> {image.url}</div>
                                        {image.height && <div><strong>Height:</strong> {image.height}</div>}
                                        {image.width && <div><strong>Width:</strong> {image.width}</div>}
                                    </div>
                                </Box>
                            ))}
                        </>
                    )}
                </Box>
            </CardContent>
        </Card>
    );
};

export default Profile;
