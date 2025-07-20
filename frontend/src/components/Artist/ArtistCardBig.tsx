import { useEffect } from 'react';

import { Box, Typography, Avatar, Chip, CircularProgress, Alert } from '@mui/material';

import PeopleIcon from '@mui/icons-material/People';

import { useArtist } from '../../context';

interface ArtistCardBigProps {
    artistId: string;
}

const ArtistCardBig = ({ artistId }: ArtistCardBigProps) => {
    const { artist, artistLoading, artistError, fetchArtist } = useArtist();

    const maxGenres = 5;

    useEffect(() => {
        if (artistId) {
            fetchArtist(artistId);
        }
    }, [artistId, fetchArtist]);

    if (artistLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
                <CircularProgress size={60} />
            </Box>
        );
    }

    if (artistError) {
        return (
            <Box sx={{ py: 4 }}>
                <Alert severity="error">{artistError}</Alert>
            </Box>
        );
    }

    if (!artist) {
        return (
            <Box sx={{ py: 4 }}>
                <Alert severity="info">Artist not found</Alert>
            </Box>
        );
    }

    const artistImage = artist.images && artist.images.length > 0
        ? artist.images[0].url
        : '';

    const formatFollowers = (followers: number): string => {
        if (followers >= 1000000) {
            return `${(followers / 1000000).toFixed(1)}M`;
        } else if (followers >= 1000) {
            return `${(followers / 1000).toFixed(1)}K`;
        }
        return followers.toString();
    };

    return (
        <Box sx={{
            py: 4,
            px: 2,
            display: 'flex',
            flexDirection: { xs: 'column', md: 'row' },
            gap: 4,
            alignItems: { xs: 'center', md: 'flex-start' },
            textAlign: { xs: 'center', md: 'left' }
        }}>
            {/* Image */}
            <Avatar
                src={artistImage}
                alt={artist.name}
                sx={{
                    width: { xs: 200, md: 250 },
                    height: { xs: 200, md: 250 },
                    borderRadius: 2,
                    boxShadow: 3,
                    flexShrink: 0,
                }}
            >
                <Typography variant="h1" sx={{ fontSize: '4rem', fontWeight: 'bold' }}>
                    {artist.name.charAt(0).toUpperCase()}
                </Typography>
            </Avatar>

            {/* Info */}
            <Box sx={{ flex: 1, minWidth: 0 }}>
                <Typography
                    variant="h2"
                    component="h1"
                    sx={{
                        fontWeight: 'bold',
                        mb: 2,
                        fontSize: { xs: '2.5rem', md: '3.5rem' },
                        lineHeight: 1.1,
                    }}
                >
                    {artist.name}
                </Typography>

                <Box sx={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: 1,
                    mb: 3,
                    justifyContent: { xs: 'center', md: 'flex-start' }
                }}>
                    <PeopleIcon color="action" />
                    <Typography
                        variant="h6"
                        color="text.secondary"
                        sx={{ fontWeight: 500 }}
                    >
                        {formatFollowers(artist.followers.total)} followers
                    </Typography>
                </Box>

                {artist.genres && artist.genres.length > 0 && (
                    <Box sx={{ mb: 2 }}>
                        <Typography
                            variant="subtitle1"
                            sx={{
                                mb: 1.5,
                                fontWeight: 600,
                                color: 'text.secondary'
                            }}
                        >
                            Genres
                        </Typography>
                        <Box sx={{
                            display: 'flex',
                            flexWrap: 'wrap',
                            gap: 1,
                            justifyContent: { xs: 'center', md: 'flex-start' }
                        }}>
                            {artist.genres.slice(0, maxGenres).map((genre, index) => (
                                <Chip
                                    key={index}
                                    label={genre.charAt(0).toUpperCase() + genre.slice(1)}
                                    variant="outlined"
                                    sx={{
                                        borderRadius: 2,
                                        '&:hover': {
                                            backgroundColor: 'action.hover',
                                        },
                                    }}
                                />
                            ))}
                        </Box>
                    </Box>
                )}

                <Box sx={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: 1,
                    justifyContent: { xs: 'center', md: 'flex-start' }
                }}>
                    <Typography
                        variant="body2"
                        color="text.secondary"
                        sx={{ fontWeight: 500 }}
                    >
                        Popularity: {artist.popularity}/100
                    </Typography>
                    <Box
                        sx={{
                            width: 100,
                            height: 6,
                            backgroundColor: 'grey.300',
                            borderRadius: 3,
                            overflow: 'hidden',
                        }}
                    >
                        <Box
                            sx={{
                                width: `${artist.popularity}%`,
                                height: '100%',
                                backgroundColor: 'primary.main',
                                borderRadius: 3,
                            }}
                        />
                    </Box>
                </Box>
            </Box>
        </Box>
    );
};

export default ArtistCardBig;
