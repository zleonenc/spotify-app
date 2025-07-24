import { useEffect } from 'react';

import {
    Box,
    Typography,
    CircularProgress,
    Alert,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper
} from '@mui/material';

import { AccessTime } from '@mui/icons-material';

import { useAlbum } from '../../context';

import AlbumTrackRow from './AlbumTrackRow';

interface AlbumTracksTableProps {
    albumId: string;
}

const AlbumTracksTable = ({ albumId }: AlbumTracksTableProps) => {
    const { album, albumLoading, albumError, fetchAlbum } = useAlbum();

    useEffect(() => {
        if (albumId) {
            fetchAlbum(albumId);
        }
    }, [albumId, fetchAlbum]);

    if (albumLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (albumError) {
        return (
            <Box sx={{ mt: 4 }}>
                <Alert severity="error">{albumError}</Alert>
            </Box>
        );
    }

    const tracks = album?.tracks?.items || [];

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h4" component="h2" sx={{ fontWeight: 'bold' }} gutterBottom>
                Album songs
            </Typography>

            {tracks.length === 0 ? (
                <Box>
                    <Typography variant="body1" color="text.secondary">
                        No songs found
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
                            <TableRow sx={{
                                bgcolor: 'grey.300',
                                borderBottom: '2px solid',
                                borderColor: 'divider'
                            }}>
                                <TableCell
                                    align="center"
                                    sx={{
                                        fontWeight: 800,
                                        width: 60,
                                    }}
                                >
                                    #
                                </TableCell>
                                <TableCell
                                    sx={{
                                        fontWeight: 800,
                                    }}
                                >
                                    Song Name
                                </TableCell>
                                <TableCell
                                    align="right"
                                    sx={{
                                        fontWeight: 800,
                                        width: 120,
                                    }}
                                >
                                    <AccessTime sx={{ fontSize: '1.2rem' }} />
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {tracks.map((track, index) => (
                                <AlbumTrackRow
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

export default AlbumTracksTable;
