import React from 'react';

import {
    Box,
    IconButton,
    Popover,
    Paper,
    Typography,
    Badge
} from '@mui/material';

import {
    Tune as FilterIcon,
    MusicNote as TrackIcon,
    Person as ArtistIcon,
    Album as AlbumIcon
} from '@mui/icons-material';

import type { SearchFilters } from '../../../context/SearchContext';

interface SearchFilterProps {
    filters: SearchFilters;
    onFilterChange: (filterType: keyof SearchFilters) => void;
    anchorEl: HTMLButtonElement | null;
    onOpen: (event: React.MouseEvent<HTMLButtonElement>) => void;
    onClose: () => void;
}

const SearchFilter = ({
    filters,
    onFilterChange,
    anchorEl,
    onOpen,
    onClose
}: SearchFilterProps) => {
    const filterOptions = [
        { key: 'track' as const, label: 'Tracks', icon: TrackIcon },
        { key: 'artist' as const, label: 'Artists', icon: ArtistIcon },
        { key: 'album' as const, label: 'Albums', icon: AlbumIcon }
    ];

    const activeFiltersCount = Object.values(filters).filter(Boolean).length;

    return (
        <>
            <Badge
                badgeContent={activeFiltersCount}
                color="primary"
                variant="dot"
                invisible={activeFiltersCount === filterOptions.length}
            >
                <IconButton
                    onClick={onOpen}
                    size="small"
                    sx={{
                        borderRadius: 1,
                        bgcolor: Boolean(anchorEl) ? 'action.selected' : 'transparent',
                        color: activeFiltersCount < 3 ? 'primary.main' : 'text.secondary',
                        '&:hover': {
                            bgcolor: 'action.hover'
                        }
                    }}
                >
                    <FilterIcon fontSize="small" />
                </IconButton>
            </Badge>

            <Popover
                open={Boolean(anchorEl)}
                anchorEl={anchorEl}
                onClose={onClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'right',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                slotProps={{
                    paper: {
                        elevation: 8,
                        sx: {
                            borderRadius: 2,
                            minWidth: 240,
                        }
                    }
                }}
            >
                <Paper sx={{ p: 2 }}>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 0.5 }}>
                        {filterOptions.map(({ key, label, icon: Icon }) => (
                            <Box
                                key={key}
                                onClick={() => onFilterChange(key)}
                                sx={{
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: 1.5,
                                    p: 1.5,
                                    borderRadius: 1.5,
                                    cursor: 'pointer',
                                    bgcolor: filters[key] ? 'primary.50' : 'transparent',
                                    border: '1px solid',
                                    borderColor: filters[key] ? 'primary.200' : 'transparent',
                                    transition: 'all 0.2s ease-in-out',
                                    '&:hover': {
                                        bgcolor: filters[key] ? 'primary.100' : 'grey.50',
                                        borderColor: filters[key] ? 'primary.300' : 'grey.200'
                                    }
                                }}
                            >
                                <Icon
                                    fontSize="small"
                                    sx={{
                                        color: filters[key] ? 'primary.main' : 'text.secondary'
                                    }}
                                />
                                <Typography
                                    variant="body2"
                                    sx={{
                                        flexGrow: 1,
                                        fontWeight: filters[key] ? 500 : 400,
                                        color: filters[key] ? 'primary.main' : 'text.primary'
                                    }}
                                >
                                    {label}
                                </Typography>
                                <Box
                                    sx={{
                                        width: 18,
                                        height: 18,
                                        borderRadius: '50%',
                                        border: '2px solid',
                                        borderColor: filters[key] ? 'primary.main' : 'grey.300',
                                        bgcolor: filters[key] ? 'primary.main' : 'transparent',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        transition: 'all 0.2s ease-in-out'
                                    }}
                                >
                                    {filters[key] && (
                                        <Box
                                            sx={{
                                                width: 6,
                                                height: 6,
                                                borderRadius: '50%',
                                                bgcolor: 'white'
                                            }}
                                        />
                                    )}
                                </Box>
                            </Box>
                        ))}
                    </Box>
                </Paper>
            </Popover>
        </>
    );
};

export default SearchFilter;
