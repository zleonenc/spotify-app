import { useEffect } from 'react';

import { Box, Typography, CircularProgress, Alert, Paper } from '@mui/material';
import { AccessTime } from '@mui/icons-material';

import { useMe } from '../../context';

import TrackRow from '../Track/TrackRow';

const DashboardTopTracks = () => {
    const { topTracks, topTracksLoading, topTracksError, fetchTopTracks } = useMe();

    useEffect(() => {
        fetchTopTracks();
    }, [fetchTopTracks]);

    if (topTracksLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (topTracksError) {
        return (
            <Box sx={{ mt: 4 }}>
                <Alert severity="error">{topTracksError}</Alert>
            </Box>
        );
    }

    const limitedTracks = topTracks.slice(0, 10);

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h5" component="h2" gutterBottom>
                My top tracks
            </Typography>

            {topTracks.length === 0 ? (
                <Box>
                    <Typography variant="body1" color="text.secondary">
                        No top tracks found
                    </Typography>
                </Box>
            ) : (
                <Paper
                    elevation={0}
                    sx={{
                        mt: 2,
                        borderRadius: 2,
                        border: '1px solid',
                        borderColor: 'divider',
                        overflow: 'hidden'
                    }}
                >
                    <Box
                        sx={{
                            display: 'flex',
                            alignItems: 'center',
                            py: 1.5,
                            px: 2,
                            bgcolor: 'grey.50',
                            borderBottom: '1px solid',
                            borderColor: 'divider',
                        }}
                    >
                        <Typography
                            variant="body2"
                            sx={{
                                fontWeight: 600,
                                color: 'text.secondary',
                                minWidth: 32,
                                textAlign: 'center',
                                mr: 2,
                            }}
                        >
                            #
                        </Typography>
                        <Typography
                            variant="body2"
                            sx={{
                                fontWeight: 600,
                                color: 'text.secondary',
                                flex: 1,
                                mr: 2,
                            }}
                        >
                            Title
                        </Typography>
                        <Typography
                            variant="body2"
                            sx={{
                                fontWeight: 600,
                                color: 'text.secondary',
                                minWidth: 48,
                                textAlign: 'right',
                            }}
                        >
                            <AccessTime sx={{ fontSize: '1.2rem' }} />
                        </Typography>
                    </Box>

                    {/* Track Rows */}
                    {limitedTracks.map((track, index) => (
                        <TrackRow
                            key={track.id}
                            track={track}
                            index={index + 1}
                        />
                    ))}
                </Paper>
            )}
        </Box>
    );
};

export default DashboardTopTracks;