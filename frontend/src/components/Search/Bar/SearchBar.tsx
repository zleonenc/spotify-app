import React, { useState, useCallback, useRef, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

import {
    Box,
    TextField,
    InputAdornment,
    IconButton,
    Paper,
    Typography,
    Stack,
    Button,
    Divider,
    CircularProgress
} from '@mui/material';

import {
    Search as SearchIcon,
    Clear as ClearIcon
} from '@mui/icons-material';

import { useSearch } from '../../../context';

import SearchResultSection from '../SearchResultSection';
import SearchFilter, { type SearchFilters } from './SearchFilter';

const SearchBar = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { search, searchResults } = useSearch();

    const [query, setQuery] = useState('');
    const [filters, setFilters] = useState<SearchFilters>({
        track: true,
        artist: true,
        album: true
    });

    const [filterAnchor, setFilterAnchor] = useState<HTMLButtonElement | null>(null);
    const [showQuickResults, setShowQuickResults] = useState(false);
    const [isSearching, setIsSearching] = useState(false);

    const searchTimeoutRef = useRef<number | null>(null);
    const currentSearchRef = useRef<string>('');

    const isOnSearchPage = location.pathname === '/search';
    const maxResults = 3; // Limit for quick results
    const searchDebounceDelay = 500; // Time before triggering search
    const mouseLeaveDelay = 500; // Delay before closing dropdown on mouse leave

    const searchResultTypes = [
        { key: 'tracks', title: 'Tracks', type: 'track' as const },
        { key: 'artists', title: 'Artists', type: 'artist' as const },
        { key: 'albums', title: 'Albums', type: 'album' as const }
    ];

    useEffect(() => {
        return () => {
            if (searchTimeoutRef.current) {
                clearTimeout(searchTimeoutRef.current);
            }
        };
    }, []);

    // Debounced search
    const debouncedSearch = useCallback(async (searchQuery: string, showDropdown = false) => {
        if (!searchQuery.trim()) {
            setShowQuickResults(false);
            setIsSearching(false);
            return;
        }

        currentSearchRef.current = searchQuery;

        const activeTypes = Object.entries(filters)
            .filter(([_, isActive]) => isActive)
            .map(([type, _]) => type);

        if (activeTypes.length === 0) {
            setIsSearching(false);
            return;
        }

        try {
            setIsSearching(true);

            const limit = showDropdown ? maxResults : 20;
            await search(searchQuery, activeTypes.join(','), limit);

            // User has not typed something else
            if (currentSearchRef.current === searchQuery) {
                if (showDropdown && !isOnSearchPage) {
                    setShowQuickResults(true);
                } else {
                    setShowQuickResults(false);
                    if (!isOnSearchPage) {
                        navigate(`/search?q=${encodeURIComponent(searchQuery)}`);
                    }
                }
            }
        } catch (error) {
            console.error('Search failed:', error);
            // Update if query is different
            if (currentSearchRef.current === searchQuery) {
                setShowQuickResults(false);
            }
        } finally {
            // Update if query is different
            if (currentSearchRef.current === searchQuery) {
                setIsSearching(false);
            }
        }
    }, [filters, search, navigate, isOnSearchPage]);


    const handleSearch = useCallback(async (showDropdown = false) => {
        if (searchTimeoutRef.current) {
            clearTimeout(searchTimeoutRef.current);
        }

        // Immediate search for enter key or button click
        await debouncedSearch(query, showDropdown);
    }, [query, debouncedSearch]);

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newQuery = event.target.value;
        setQuery(newQuery);

        if (searchTimeoutRef.current) {
            clearTimeout(searchTimeoutRef.current);
        }

        currentSearchRef.current = newQuery;

        if (newQuery.trim()) {
            searchTimeoutRef.current = window.setTimeout(() => {
                debouncedSearch(newQuery, !isOnSearchPage);
            }, searchDebounceDelay);
        } else {
            setShowQuickResults(false);
            setIsSearching(false);
        }
    };

    const handleKeyDown = (event: React.KeyboardEvent) => {
        if (event.key === 'Enter') {
            handleSearch(false); // Search on Enter
        }
    };

    const handleSearchIconClick = () => {
        handleSearch(false); // Search on click
    };

    const handleSeeMoreResults = () => {
        setShowQuickResults(false);
        navigate(`/search?q=${encodeURIComponent(query)}`);
    };

    const handleFilterChange = (filterType: keyof SearchFilters) => {
        const newFilters = {
            ...filters,
            [filterType]: !filters[filterType]
        };
        setFilters(newFilters);
    };

    const handleFilterClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setFilterAnchor(event.currentTarget);
    };

    const handleFilterClose = () => {
        setFilterAnchor(null);
    };

    const clearSearch = () => {
        setQuery('');
        setShowQuickResults(false);
    };

    const getQuickResults = () => {
        type QuickResultsType = Record<string, any[]>;

        if (!searchResults) {
            return searchResultTypes.reduce((acc, { key }) => {
                acc[key] = [];
                return acc;
            }, {} as QuickResultsType);
        }

        return searchResultTypes.reduce((acc, { key }) => {
            acc[key] = searchResults[key as keyof typeof searchResults]?.items?.slice(0, maxResults) || [];
            return acc;
        }, {} as QuickResultsType);
    };

    return (
        <Box sx={{ position: 'relative', display: 'flex', alignItems: 'center', gap: 1, width: '100%' }}>
            <TextField
                fullWidth
                size="small"
                variant="outlined"
                placeholder="Search for tracks, artists, albums..."
                value={query}
                onChange={handleInputChange}
                onKeyDown={handleKeyDown}
                onFocus={() => {
                    if (query.trim() && searchResults && !isOnSearchPage) {
                        setShowQuickResults(true);
                    }
                }}
                slotProps={{
                    input: {
                        startAdornment: (
                            <InputAdornment position="start">
                                <IconButton
                                    size="small"
                                    onClick={handleSearchIconClick}
                                    disabled={isSearching}
                                >
                                    <SearchIcon color="action" fontSize="small" />
                                </IconButton>
                            </InputAdornment>
                        ),
                        endAdornment: (
                            <InputAdornment position="end">
                                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                                    {query && (
                                        <IconButton
                                            size="small"
                                            onClick={clearSearch}
                                        >
                                            <ClearIcon fontSize="small" />
                                        </IconButton>
                                    )}

                                    <SearchFilter
                                        filters={filters}
                                        onFilterChange={handleFilterChange}
                                        anchorEl={filterAnchor}
                                        onOpen={handleFilterClick}
                                        onClose={handleFilterClose}
                                    />
                                </Box>
                            </InputAdornment>
                        ),
                    }
                }}
                sx={{
                    '& .MuiOutlinedInput-root': {
                        borderRadius: 3,
                        bgcolor: 'background.default',
                    }
                }}
                disabled={false}
            />

            {/* Quick Results Dropdown*/}
            {showQuickResults && searchResults && (
                <Paper
                    sx={{
                        position: 'absolute',
                        top: '100%',
                        left: 0,
                        right: 0,
                        mt: 1,
                        maxHeight: 400,
                        overflowY: 'auto',
                        overflowX: 'hidden',
                        zIndex: 1300,
                        borderRadius: 2,
                        boxShadow: 3,

                        // Scrollbar styling
                        '&::-webkit-scrollbar': {
                            width: '6px',
                        },
                        '&::-webkit-scrollbar-track': {
                            background: 'transparent',
                        },
                        '&::-webkit-scrollbar-thumb': {
                            background: 'rgba(0,0,0,0.2)',
                            borderRadius: '3px',
                        },
                        '&::-webkit-scrollbar-thumb:hover': {
                            background: 'rgba(0,0,0,0.3)',
                        },
                    }}
                    onMouseLeave={() => {
                        setTimeout(() => setShowQuickResults(false), mouseLeaveDelay);
                    }}
                >
                    <Box sx={{ p: 2, width: '94%', overflow: 'hidden' }}>
                        {isSearching ? (
                            <Box sx={{ display: 'flex', justifyContent: 'center', py: 2 }}>
                                <CircularProgress size={24} />
                            </Box>
                        ) : (
                            <>
                                {(() => {
                                    const quickResults = getQuickResults();
                                    const hasResults = quickResults.tracks.length > 0 ||
                                        quickResults.artists.length > 0 ||
                                        quickResults.albums.length > 0;

                                    if (!hasResults) {
                                        return (
                                            <Typography variant="body2" color="text.secondary" sx={{ textAlign: 'center', py: 2 }}>
                                                No results found
                                            </Typography>
                                        );
                                    }

                                    return (
                                        <Stack spacing={2}>
                                            {/* Dynamic Search Results */}
                                            {searchResultTypes.map(({ key, title, type }) => (
                                                <SearchResultSection
                                                    key={key}
                                                    title={title}
                                                    items={quickResults[key as keyof typeof quickResults]}
                                                    type={type}
                                                />
                                            ))}

                                            <Divider />

                                            {/* See More Results Button */}
                                            <Button
                                                variant="outlined"
                                                fullWidth
                                                onClick={handleSeeMoreResults}
                                                sx={{
                                                    borderRadius: 2,
                                                    textTransform: 'none'
                                                }}
                                            >
                                                See more results
                                            </Button>
                                        </Stack>
                                    );
                                })()}
                            </>
                        )}
                    </Box>
                </Paper>
            )}
        </Box>
    );
};

export default SearchBar;
