import { useNavigate } from 'react-router-dom';

import { Avatar, Typography, Box } from '@mui/material';

import type { Artist, Album, Track } from '../../types';

type SearchItem = Artist | Album | Track;

interface SearchItemProps<T extends SearchItem> {
    item: T;
    type: 'artist' | 'album' | 'track';
}

const SearchItem = <T extends SearchItem>({ item, type }: SearchItemProps<T>) => {
    const navigate = useNavigate();

    const handleClick = () => {
        if (type === 'artist') {
            navigate(`/artist/${item.id}`);
        } else if (type === 'album') {
            navigate(`/album/${item.id}`);
        } else if (type === 'track') {
            navigate(`/track/${item.id}`);
        }
    };

    const getImage = () => {
        if (type === 'track') {
            const track = item as Track;
            if (track.album?.images && track.album.images.length > 0) {
                return track.album.images[0].url;
            }
            return '';
        }

        if ('images' in item && item.images && item.images.length > 0) {
            return item.images[0].url;
        }
        return '';
    };

    const getSubtitle = () => {
        switch (type) {
            case 'artist':
                const artist = item as Artist;
                return artist.genres && artist.genres.length > 0
                    ? artist.genres.slice(0, 3).join(', ')
                    : 'Artist';
            case 'album':
                const album = item as Album;
                const artistNames = album.artists?.map(artist => artist.name).join(', ') || 'Unknown Artist';
                const year = album.release_date ? new Date(album.release_date).getFullYear() : '';
                return `Album • ${artistNames}${year ? ` • ${year}` : ''}`;
            case 'track':
                const track = item as Track;
                const trackArtists = track.artists?.map(artist => artist.name).join(', ') || 'Unknown Artist';
                const albumName = track.album?.name || '';
                return `Track • ${trackArtists}${albumName ? ` • ${albumName}` : ''}`;
            default:
                return '';
        }
    };

    const getTitle = () => {
        if (type === 'track') {
            const track = item as Track;
            const explicitIndicator = track.explicit ? ' - E' : '';
            return `${item.name}${explicitIndicator}`;
        }
        return item.name;
    };

    const getTypeLabel = () => {
        switch (type) {
            case 'artist':
                return 'Artist';
            case 'album':
                return 'Album';
            case 'track':
                return 'Track';
            default:
                return '';
        }
    };

    return (
        <Box
            onClick={handleClick}
            sx={{
                cursor: 'pointer',
                transition: 'all 0.2s ease-in-out',
                borderTop: '1px solid',
                borderBottom: '1px solid',
                borderColor: 'divider',
                p: 2,
                width: '100%',
                overflow: 'hidden',
                '&:hover': {
                    bgcolor: 'action.hover',
                },
                '&:first-of-type': {
                    borderTop: 'none',
                },
                '&:last-of-type': {
                    borderBottom: 'none',
                },
            }}
        >
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, width: '100%', overflow: 'hidden' }}>
                <Avatar
                    src={getImage()}
                    alt={getTitle()}
                    sx={{
                        width: 60,
                        height: 60,
                        borderRadius: type === 'artist' ? '50%' : 1,
                        flexShrink: 0,
                    }}
                    variant={type === 'artist' ? 'circular' : 'square'}
                >
                    {getTitle().charAt(0).toUpperCase()}
                </Avatar>
                <Box sx={{ flex: 1, minWidth: 0, overflow: 'hidden' }}>
                    <Typography
                        variant="body2"
                        color="text.secondary"
                        sx={{
                            fontSize: '0.75rem',
                            textTransform: 'uppercase',
                            fontWeight: 500,
                            mb: 0.5,
                        }}
                    >
                        {getTypeLabel()}
                    </Typography>
                    <Typography
                        variant="subtitle1"
                        component="h3"
                        sx={{
                            fontWeight: 600,
                            mb: 0.5,
                            overflow: 'hidden',
                            textOverflow: 'ellipsis',
                            whiteSpace: 'nowrap',
                        }}
                    >
                        {getTitle()}
                    </Typography>
                    <Typography
                        variant="body2"
                        color="text.secondary"
                        sx={{
                            overflow: 'hidden',
                            textOverflow: 'ellipsis',
                            whiteSpace: 'nowrap',
                        }}
                    >
                        {getSubtitle()}
                    </Typography>
                </Box>
            </Box>
        </Box>
    );
};

export default SearchItem;