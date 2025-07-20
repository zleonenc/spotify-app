import { useEffect } from 'react';

import { Box, Typography, CircularProgress, Alert, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import { AccessTime } from '@mui/icons-material';

import { useArtist } from '../../context';

import { TrackRowWithPopularity } from '../Track';

interface ArtistTopTracksProps {
    artistId: string;
}

const ArtistTopTracksTable = ({ artistId }: ArtistTopTracksProps) => {
    const { artistTopTracks, artistTopTracksLoading, artistTopTracksError, fetchArtistTopTracks } = useArtist();

    useEffect(() => {
        if (artistId) {
            fetchArtistTopTracks(artistId);
        }
    }, [artistId, fetchArtistTopTracks]);

    if (artistTopTracksLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (artistTopTracksError) {
        return (
            <Box sx={{ mt: 4 }}>
                <Alert severity="error">{artistTopTracksError}</Alert>
            </Box>
        );
    }

    const maxTracksToShow = 5;
    const tracks = artistTopTracks?.tracks || [];
    const limitedTracks = tracks.slice(0, maxTracksToShow);

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h5" component="h2" gutterBottom>
                Popular songs
            </Typography>

            {tracks.length === 0 ? (
                <Box>
                    <Typography variant="body1" color="text.secondary">
                        No popular songs found
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
                                        width: 60,
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
                                    align="center"
                                    sx={{
                                        fontWeight: 600,
                                        color: 'text.secondary',
                                        width: 120,
                                    }}
                                >
                                    Popularity
                                </TableCell>
                                <TableCell
                                    align="right"
                                    sx={{
                                        fontWeight: 600,
                                        color: 'text.secondary',
                                        width: 100,
                                    }}
                                >
                                    <AccessTime sx={{ fontSize: '1.2rem' }} />
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {limitedTracks.slice(0, 10).map((track, index) => (
                                <TrackRowWithPopularity
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

export default ArtistTopTracksTable;
