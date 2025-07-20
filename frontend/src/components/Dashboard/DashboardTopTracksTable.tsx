import { useEffect } from 'react';

import { Box, Typography, CircularProgress, Alert, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import { AccessTime } from '@mui/icons-material';

import { useProfile } from '../../context';

import { TrackRow } from '../Track';

const DashboardTopTracksTable = () => {
    const { topTracks, topTracksLoading, topTracksError, fetchTopTracks } = useProfile();

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

    const maxTracksToShow = 5;
    const tracks = topTracks || [];
    const limitedTracks = tracks.slice(0, maxTracksToShow);

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h5" component="h2" gutterBottom>
                My top tracks
            </Typography>

            {tracks.length === 0 ? (
                <Box>
                    <Typography variant="body1" color="text.secondary">
                        No top tracks found
                    </Typography>
                </Box>
            ) : (
                <TableContainer
                    component={Paper}
                    sx={{
                        mt: 2,
                        borderRadius: 2,
                        border: '1px solid',
                        borderColor: 'divider',
                    }}
                >
                    <Table>
                        <TableHead>
                            <TableRow sx={{ bgcolor: 'grey.50' }}>
                                <TableCell
                                    align="center"
                                    sx={{
                                        fontWeight: 600,
                                        color: 'text.secondary',
                                        width: 60,
                                    }}
                                >
                                    #
                                </TableCell>
                                <TableCell
                                    sx={{
                                        fontWeight: 600,
                                        color: 'text.secondary',
                                    }}
                                >
                                    Image
                                </TableCell>
                                <TableCell
                                    sx={{
                                        fontWeight: 600,
                                        color: 'text.secondary',
                                    }}
                                >
                                    Song Name
                                </TableCell>
                                <TableCell
                                    sx={{
                                        fontWeight: 600,
                                        color: 'text.secondary',
                                    }}
                                >
                                    Artist(s)
                                </TableCell>
                                <TableCell
                                    align="right"
                                    sx={{
                                        fontWeight: 600,
                                        color: 'text.secondary',
                                        width: 120,
                                    }}
                                >
                                    <AccessTime sx={{ fontSize: '1.2rem' }} />
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {limitedTracks.map((track, index) => (
                                <TrackRow
                                    key={track.id}
                                    track={track}
                                    index={index + 1}
                                />
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
        </Box>
    );
};

export default DashboardTopTracksTable;