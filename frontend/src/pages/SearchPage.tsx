import {
    Container,
    Typography,
    Box,
    Paper,
    CircularProgress,
    Alert,
    Tabs,
    Tab
} from '@mui/material';

import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';

import { useSearch } from '../context/SearchContext';
import SearchCard from '../components/Search/SearchResultItem';

const SearchPage = () => {
    const { searchResults, searchLoading, searchError, search, filters } = useSearch();
    const [selectedTab, setSelectedTab] = useState(0);
    const [searchParams] = useSearchParams();

    const searchResultTypes = [
        { key: 'tracks', title: 'Tracks', type: 'track' as const },
        { key: 'artists', title: 'Artists', type: 'artist' as const },
        { key: 'albums', title: 'Albums', type: 'album' as const }
    ];

    const activeSearchResultTypes = searchResultTypes.filter(resultType => {
        return filters[resultType.type as keyof typeof filters];
    });

    useEffect(() => {
        const query = searchParams.get('q');
        if (query && !searchLoading) {
            const activeTypes = Object.entries(filters)
                .filter(([_, isActive]) => isActive)
                .map(([type, _]) => type);

            if (activeTypes.length > 0) {
                search(query, activeTypes.join(','), 20, 0);
            }
        }
    }, [searchParams, search, filters]);

    useEffect(() => {
        if (activeSearchResultTypes.length > 0 && selectedTab >= activeSearchResultTypes.length) {
            setSelectedTab(0);
        }
    }, [filters, activeSearchResultTypes.length, selectedTab]);

    const getResults = () => {
        if (!searchResults) return { tracks: [], artists: [], albums: [] };

        return {
            tracks: searchResults.tracks?.items ?? [],
            artists: searchResults.artists?.items ?? [],
            albums: searchResults.albums?.items ?? []
        };
    };

    const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
        setSelectedTab(newValue);
    };

    const results = getResults();
    const activeTabData = activeSearchResultTypes[selectedTab];
    const activeItems = activeTabData ? results[activeTabData.key as keyof typeof results] : [];

    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Box sx={{ mb: 3 }}>
                <Typography variant="h4" component="h1" sx={{ fontWeight: 600 }}>
                    Search Results
                </Typography>
                <Typography variant="body1" color="text.secondary" sx={{ mt: 1 }}>
                    Discover music across tracks, artists, and albums
                </Typography>
            </Box>

            <Paper
                elevation={0}
                sx={{
                    p: 3,
                    borderRadius: 3,
                    border: '1px solid',
                    borderColor: 'divider'
                }}
            >
                {searchLoading ? (
                    <Box
                        sx={{
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            py: 8
                        }}
                    >
                        <CircularProgress />
                    </Box>
                ) : searchError ? (
                    <Alert severity="error" sx={{ mb: 2 }}>
                        {searchError}
                    </Alert>
                ) : !searchResults ? (
                    <Box sx={{ textAlign: 'center', py: 8 }}>
                        <Typography variant="h6" color="text.secondary">
                            Search for tracks, artists, or albums
                        </Typography>
                        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                            Use the search bar above to find your favorite music
                        </Typography>
                    </Box>
                ) : (() => {
                    const results = getResults();
                    const hasResults = results.tracks.length > 0 ||
                        results.artists.length > 0 ||
                        results.albums.length > 0;

                    if (!hasResults) {
                        return (
                            <Box sx={{ textAlign: 'center', py: 8 }}>
                                <Typography variant="h6" color="text.secondary">
                                    No results found
                                </Typography>
                                <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                                    Try adjusting your search terms or filters
                                </Typography>
                            </Box>
                        );
                    }

                    if (activeSearchResultTypes.length === 0) {
                        return (
                            <Box sx={{ textAlign: 'center', py: 8 }}>
                                <Typography variant="h6" color="text.secondary">
                                    No filters selected
                                </Typography>
                                <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                                    Enable at least one filter (tracks, artists, or albums) to see results
                                </Typography>
                            </Box>
                        );
                    }

                    return (
                        <Box sx={{ width: '100%' }}>
                            <Tabs
                                value={selectedTab}
                                onChange={handleTabChange}
                                sx={{
                                    borderBottom: 1,
                                    borderColor: 'divider',
                                    mb: 3
                                }}
                            >
                                {activeSearchResultTypes.map((resultType) => {
                                    const count = searchResults[resultType.key as keyof typeof searchResults]?.total ?? 0;
                                    return (
                                        <Tab
                                            key={resultType.key}
                                            label={`${resultType.title}`}
                                            //label={`${resultType.title} (${count})`}
                                            disabled={count === 0}
                                            sx={{
                                                textTransform: 'none',
                                                fontWeight: 500,
                                                fontSize: '1rem'
                                            }}
                                        />
                                    );
                                })}
                            </Tabs>

                            <Box sx={{ width: '100%' }}>
                                {activeItems.map((item) => (
                                    <SearchCard
                                        key={item.id}
                                        item={item}
                                        type={activeTabData?.type}
                                    />
                                ))}
                            </Box>
                        </Box>
                    );
                })()}
            </Paper>
        </Container>
    );
};

export default SearchPage;
