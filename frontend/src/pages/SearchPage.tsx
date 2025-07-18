import {
    Container,
    Typography,
    Box,
    Paper,
    CircularProgress,
    Alert,
    Stack
} from '@mui/material';

import { useSearch } from '../context/SearchContext';
import SearchResultSection from '../components/Search/SearchResultSection';

const SearchPage = () => {
    const { searchResults, searchLoading, searchError } = useSearch();

    const searchResultTypes = [
        { key: 'tracks', title: 'Tracks', type: 'track' as const },
        { key: 'artists', title: 'Artists', type: 'artist' as const },
        { key: 'albums', title: 'Albums', type: 'album' as const }
    ];

    const getResults = () => {
        if (!searchResults) return { tracks: [], artists: [], albums: [] };

        return {
            tracks: searchResults.tracks?.items ?? [],
            artists: searchResults.artists?.items ?? [],
            albums: searchResults.albums?.items ?? []
        };
    };

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

                    return (
                        <Stack spacing={3}>
                            {searchResultTypes.map(({ key, title, type }) => (
                                <SearchResultSection
                                    key={key}
                                    title={`${title} (${searchResults[key as keyof typeof searchResults]?.total ?? 0})`}
                                    items={results[key as keyof typeof results]}
                                    type={type}
                                />
                            ))}
                        </Stack>
                    );
                })()}
            </Paper>
        </Container>
    );
};

export default SearchPage;
